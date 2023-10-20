package jiux.net.plugin.restful.common.spring;

import com.intellij.psi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import jiux.net.plugin.restful.annotations.SpringRequestMethodAnnotation;
import jiux.net.plugin.restful.common.PsiAnnotationHelper;
import jiux.net.plugin.restful.common.RestSupportedAnnotationHelper;
import jiux.net.plugin.restful.method.RequestPath;
import org.apache.commons.lang.StringUtils;

public class RequestMappingAnnotationHelper implements RestSupportedAnnotationHelper {

  /**
   * Filter all annotations
   *
   * @param psiClass
   * @return
   */
  public static List<RequestPath> getRequestPaths(PsiClass psiClass) {
    PsiAnnotation[] annotations = psiClass.getModifierList().getAnnotations();
    if (annotations == null) {
      return null;
    }

    PsiAnnotation requestMappingAnnotation = null;
    List<RequestPath> list = new ArrayList<>();
    for (PsiAnnotation annotation : annotations) {
      for (SpringRequestMethodAnnotation mappingAnnotation : SpringRequestMethodAnnotation.values()) {
        if (annotation.getQualifiedName().equals(mappingAnnotation.getQualifiedName())) {
          requestMappingAnnotation = annotation;
        }
      }
    }

    if (requestMappingAnnotation != null) {
      List<RequestPath> requestMappings = getRequestMappings(
        requestMappingAnnotation,
        ""
      );
      if (requestMappings.size() > 0) {
        list.addAll(requestMappings);
      }
    } else {
      // TODO : Inheritance RequestMapping
      PsiClass superClass = psiClass.getSuperClass();
      if (
        superClass != null && !superClass.getQualifiedName().equals("java.lang.Object")
      ) {
        list = getRequestPaths(superClass);
      } else {
        list.add(new RequestPath("/", null));
      }
    }

    return list;
  }

  public static String[] getRequestMappingValues(PsiClass psiClass) {
    PsiAnnotation[] annotations = psiClass.getModifierList().getAnnotations();
    if (annotations == null) {
      return null;
    }

    for (PsiAnnotation annotation : annotations) {
      if (
        annotation
          .getQualifiedName()
          .equals(SpringRequestMethodAnnotation.REQUEST_MAPPING.getQualifiedName())
      ) {
        return getRequestMappingValues(annotation);
      }
      /*            //fixme: annotation.getQualifiedName() under mac is not the full path ?
            if (annotation.getQualifiedName().equals(requestMapping.getShortName())) {
                return getRequestMappingValues(annotation);
            }*/
    }

    return new String[] { "/" };
  }

  /**
   * @param annotation
   * @param defaultValue
   * @return
   */
  private static List<RequestPath> getRequestMappings(
    PsiAnnotation annotation,
    String defaultValue
  ) {
    List<RequestPath> mappingList = new ArrayList<>();
    SpringRequestMethodAnnotation requestAnnotation =
      SpringRequestMethodAnnotation.getByQualifiedName(annotation.getQualifiedName());

    if (requestAnnotation == null) {
      return new ArrayList<>();
    }

    List<String> methodList;
    if (requestAnnotation.methodName() != null) {
      methodList = Arrays.asList(requestAnnotation.methodName());
    } else {
      methodList = PsiAnnotationHelper.getAnnotationAttributeValues(annotation, "method");
    }

    List<String> pathList = PsiAnnotationHelper.getAnnotationAttributeValues(
      annotation,
      "value"
    );
    if (pathList.size() == 0) {
      pathList = PsiAnnotationHelper.getAnnotationAttributeValues(annotation, "path");
    }

    if (pathList.size() == 0) {
      pathList.add(defaultValue);
    }

    // todo: Handle RequestMapping without setting value or path

    if (methodList.size() > 0) {
      for (String method : methodList) {
        for (String path : pathList) {
          mappingList.add(new RequestPath(path, method));
        }
      }
    } else {
      for (String path : pathList) {
        mappingList.add(new RequestPath(path, null));
      }
    }

    return mappingList;
  }

  public static RequestPath[] getRequestPaths(PsiMethod psiMethod) {
    PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();

    List<RequestPath> list = new ArrayList<>();

    for (PsiAnnotation annotation : annotations) {
      for (SpringRequestMethodAnnotation mappingAnnotation : SpringRequestMethodAnnotation.values()) {
        if (mappingAnnotation.getQualifiedName().equals(annotation.getQualifiedName())) {
          String defaultValue = "/";
          List<RequestPath> requestMappings = getRequestMappings(
            annotation,
            defaultValue
          );
          if (requestMappings.size() > 0) {
            list.addAll(requestMappings);
          }
        }
      }
    }

    return list.toArray(new RequestPath[0]);
  }

  private static String getRequestMappingValue(PsiAnnotation annotation) {
    String value = PsiAnnotationHelper.getAnnotationAttributeValue(annotation, "value");
    if (StringUtils.isEmpty(value)) {
      value = PsiAnnotationHelper.getAnnotationAttributeValue(annotation, "path");
    }
    return value;
  }

  public static String[] getRequestMappingValues(PsiAnnotation annotation) {
    String[] values;
    //one value class com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
    //many value class com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl
    PsiAnnotationMemberValue attributeValue = annotation.findDeclaredAttributeValue(
      "value"
    );
    if (attributeValue instanceof PsiLiteralExpression) {
      return new String[] {
        ((PsiLiteralExpression) attributeValue).getValue().toString(),
      };
    }
    if (attributeValue instanceof PsiArrayInitializerMemberValue) {
      PsiAnnotationMemberValue[] initializers =
        ((PsiArrayInitializerMemberValue) attributeValue).getInitializers();
      values = new String[initializers.length];
      for (PsiAnnotationMemberValue initializer : initializers) {}
      for (int i = 0; i < initializers.length; i++) {
        values[i] = ((PsiLiteralExpression) (initializers[i])).getValue().toString();
      }
    }
    return new String[] {};
  }

  public static String getOneRequestMappingPath(PsiClass psiClass) {
    // todo: Is it necessary to handle PostMapping,GetMapping?
    PsiAnnotation annotation = psiClass
      .getModifierList()
      .findAnnotation(SpringRequestMethodAnnotation.REQUEST_MAPPING.getQualifiedName());

    String path = null;
    if (annotation != null) {
      path = RequestMappingAnnotationHelper.getRequestMappingValue(annotation);
    }

    return path != null ? path : "";
  }

  public static String getOneRequestMappingPath(PsiMethod psiMethod) {
    SpringRequestMethodAnnotation requestAnnotation = null;
    List<SpringRequestMethodAnnotation> springRequestAnnotations = Arrays
      .stream(SpringRequestMethodAnnotation.values())
      .filter(annotation ->
        psiMethod.getModifierList().findAnnotation(annotation.getQualifiedName()) != null
      )
      .collect(Collectors.toList());

    if (springRequestAnnotations.size() > 0) {
      requestAnnotation = springRequestAnnotations.get(0);
    }

    String mappingPath;
    if (requestAnnotation != null) {
      PsiAnnotation annotation = psiMethod
        .getModifierList()
        .findAnnotation(requestAnnotation.getQualifiedName());
      mappingPath = RequestMappingAnnotationHelper.getRequestMappingValue(annotation);
    } else {
      String methodName = psiMethod.getName();
      mappingPath = StringUtils.uncapitalize(methodName);
    }
    return mappingPath;
  }
}
