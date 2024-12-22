package jiux.net.plugin.restful.common.jaxrs;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import jiux.net.plugin.restful.annotations.JaxrsHttpMethodAnnotation;
import jiux.net.plugin.restful.annotations.JaxrsPathAnnotation;
import jiux.net.plugin.restful.common.PsiAnnotationHelper;
import jiux.net.plugin.restful.common.spring.StringUtils;
import jiux.net.plugin.restful.method.RequestPath;

public class JaxrsAnnotationHelper {

  private static String getWsPathValue(PsiAnnotation annotation) {
    String value = PsiAnnotationHelper.getAnnotationAttributeValue(annotation, "value");

    return value != null ? value : "";
  }

  public static RequestPath[] getRequestPaths(PsiMethod psiMethod) {
    PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
    List<RequestPath> list = new ArrayList<>();

    PsiAnnotation wsPathAnnotation = psiMethod
      .getModifierList()
      .findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());
    String path = wsPathAnnotation == null
      ? psiMethod.getName()
      : getWsPathValue(wsPathAnnotation);

    JaxrsHttpMethodAnnotation[] jaxrsHttpMethodAnnotations =
      JaxrsHttpMethodAnnotation.values();
    Arrays
      .stream(annotations)
      .forEach(a ->
        Arrays
          .stream(jaxrsHttpMethodAnnotations)
          .forEach(methodAnnotation -> {
            if (
              Objects.equals(a.getQualifiedName(), methodAnnotation.getQualifiedName())
            ) {
              list.add(new RequestPath(path, methodAnnotation.getShortName()));
            }
          })
      );

    return list.toArray(new RequestPath[0]);
  }

  public static String getClassUriPath(PsiClass psiClass) {
    PsiAnnotation annotation = Objects
      .requireNonNull(psiClass.getModifierList())
      .findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());
    String path = PsiAnnotationHelper.getAnnotationAttributeValue(annotation, "value");
    return path != null ? path : "";
  }

  public static String getMethodUriPath(PsiMethod psiMethod) {
    JaxrsHttpMethodAnnotation requestAnnotation = null;

    List<JaxrsHttpMethodAnnotation> httpMethodAnnotations = Arrays
      .stream(JaxrsHttpMethodAnnotation.values())
      .filter(annotation ->
        psiMethod.getModifierList().findAnnotation(annotation.getQualifiedName()) != null
      )
      .collect(Collectors.toList());

    if (!httpMethodAnnotations.isEmpty()) {
      requestAnnotation = httpMethodAnnotations.get(0);
    }

    String mappingPath;
    if (requestAnnotation != null) {
      PsiAnnotation annotation = psiMethod
        .getModifierList()
        .findAnnotation(JaxrsPathAnnotation.PATH.getQualifiedName());
      mappingPath = getWsPathValue(annotation);
    } else {
      String methodName = psiMethod.getName();
      mappingPath = StringUtils.uncapitalize(methodName);
    }
    return mappingPath;
  }
}
