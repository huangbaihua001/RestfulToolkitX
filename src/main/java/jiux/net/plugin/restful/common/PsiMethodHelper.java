package jiux.net.plugin.restful.common;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import java.util.Objects;
import jiux.net.plugin.restful.annotations.JaxrsRequestAnnotation;
import jiux.net.plugin.restful.annotations.SpringControllerAnnotation;
import jiux.net.plugin.restful.common.jaxrs.JaxrsAnnotationHelper;
import jiux.net.plugin.restful.common.spring.RequestMappingAnnotationHelper;
import jiux.net.plugin.restful.method.Parameter;
import jiux.net.plugin.restful.method.action.ModuleHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jiux.net.plugin.restful.annotations.SpringRequestParamAnnotations.*;


public class PsiMethodHelper {

    PsiMethod psiMethod;
    Project myProject;
    Module myModule;

    private final String pathSeparator = "/";

    protected PsiMethodHelper(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
    }

    public static PsiMethodHelper create(@NotNull PsiMethod psiMethod) {
        return new PsiMethodHelper(psiMethod);
    }

    //contains "RestController" "Controller"
    public static boolean isSpringRestSupported(PsiClass containingClass) {
        PsiModifierList modifierList = containingClass.getModifierList();

        /*return modifierList.findAnnotation(SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName()) != null ||
                modifierList.findAnnotation(SpringControllerAnnotation.CONTROLLER.getQualifiedName()) != null ;*/

        return modifierList.findAnnotation(SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName()) != null
            || modifierList.findAnnotation(SpringControllerAnnotation.CONTROLLER.getQualifiedName()) != null;
    }

    //contains "RestController" "Controller"
    public static boolean isJaxrsRestSupported(PsiClass containingClass) {
        PsiModifierList modifierList = containingClass.getModifierList();

        return modifierList.findAnnotation(JaxrsRequestAnnotation.PATH.getQualifiedName()) != null;
    }

    public PsiMethodHelper withModule(Module module) {
        this.myModule = module;
        return this;
    }

    @NotNull
    protected Project getProject() {
        myProject = psiMethod.getProject();
        return myProject;
    }


    public String buildParamString() {
        StringBuilder param = new StringBuilder("");
        Map<String, Object> baseTypeParamMap = getBaseTypeParameterMap();

        if (baseTypeParamMap != null && baseTypeParamMap.size() > 0) {
            baseTypeParamMap.forEach((s, o) -> param.append(s).append("=").append(o).append("&"));
        }

        return param.length() > 0 ? param.deleteCharAt(param.length() - 1).toString() : "";
    }


    @NotNull
    public Map<String, Object> getBaseTypeParameterMap() {
        List<Parameter> parameterList = getParameterList();

        Map<String, Object> baseTypeParamMap = new LinkedHashMap();
        for (Parameter parameter : parameterList) {
            if (parameter.isRequestBodyFound()) {
                continue;
            }

            // todo type check
            // 8 PsiPrimitiveType
            // 8 boxed types; String,Date:PsiClassReferenceType == field.getType().getPresentableText()
            String shortTypeName = parameter.getShortTypeName();
            Object defaultValue = PsiClassHelper.getJavaBaseTypeDefaultValue(shortTypeName);
            if (defaultValue != null) {
                baseTypeParamMap.put(parameter.getParamName(), (defaultValue));
                continue;
            }

            PsiClassHelper psiClassHelper = PsiClassHelper.create(psiMethod.getContainingClass());
            PsiClass psiClass = psiClassHelper.findOnePsiClassByClassName(parameter.getParamType(), getProject());

            if (psiClass != null) {
                PsiField[] fields = psiClass.getFields();
                for (PsiField field : fields) {
                    Object fieldDefaultValue = PsiClassHelper.getJavaBaseTypeDefaultValue(
                        field.getType().getPresentableText());
                    if (fieldDefaultValue != null) {
                        baseTypeParamMap.put(field.getName(), fieldDefaultValue);
                    }
                }
            }
        }
        return baseTypeParamMap;
    }


    @Nullable
    public Map<String, Object> getJavaBaseTypeDefaultValue(String paramName, String paramType) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        Object paramValue = null;
        paramValue = PsiClassHelper.getJavaBaseTypeDefaultValue(paramType);
        if (paramValue != null) {
            paramMap.put(paramType, paramValue);
        }
        return paramMap;
    }

    @NotNull
    public List<Parameter> getParameterList() {
        List<Parameter> parameterList = new ArrayList<>();

        PsiParameterList psiParameterList = psiMethod.getParameterList();
        PsiParameter[] psiParameters = psiParameterList.getParameters();
        for (PsiParameter psiParameter : psiParameters) {
            PsiType paramPsiType = psiParameter.getType();
            String paramType = paramPsiType.getCanonicalText();
            if ("javax.servlet.http.HttpServletRequest".equals(paramType)
                || "javax.servlet.http.HttpServletResponse".equals(paramType)) {
                continue;
            }
            // @RequestParam
            PsiModifierList modifierList = psiParameter.getModifierList();
            boolean requestBodyFound = modifierList.findAnnotation(REQUEST_BODY.getQualifiedName()) != null;
            String paramName = psiParameter.getName();
            String requestName = null;

            PsiAnnotation pathVariableAnno = modifierList.findAnnotation(PATH_VARIABLE.getQualifiedName());
            if (pathVariableAnno != null) {
                requestName = getAnnotationValue(pathVariableAnno);
                Parameter parameter = new Parameter(paramType,
                    requestName != null ? requestName : paramName).setRequired(true).requestBodyFound(requestBodyFound);
                parameterList.add(parameter);
            }

            PsiAnnotation requestParamAnno = modifierList.findAnnotation(REQUEST_PARAM.getQualifiedName());
            if (requestParamAnno != null) {
                requestName = getAnnotationValue(requestParamAnno);
                Parameter parameter = new Parameter(paramType,
                    requestName != null ? requestName : paramName).setRequired(true).requestBodyFound(requestBodyFound);
                parameterList.add(parameter);
            }

            if (pathVariableAnno == null && requestParamAnno == null) {

                if (!paramType.contains("java.util.") && paramType.contains("<") && paramType.contains(">")) {
                    PsiTypeElement typeElement = psiParameter.getTypeElement();
                    if (typeElement == null) {
                        return parameterList;
                    }
                    // Generics param found.  For example: get(PageParam<VetReq> req)
                    PsiJavaCodeReferenceElement referenceElement = typeElement.getInnermostComponentReferenceElement();
                    if (referenceElement == null) {
                        return parameterList;
                    }

                    String tmpParamType = referenceElement.getCanonicalText();
//                    PsiReferenceParameterList referenceParameterList = referenceElement.getParameterList();
//                    if (referenceParameterList != null) {
//                        PsiTypeElement[] typeParameterElements = referenceParameterList.getTypeParameterElements();
//                        PsiTypeElement firstTypeEle = typeParameterElements[0];
//                        PsiJavaCodeReferenceElement tmp = firstTypeEle.getInnermostComponentReferenceElement();
//                        if (tmp != null) {
//                            tmpParamType = tmp.getCanonicalText();
//                        }
//                    }

                    if (tmpParamType != null) {
                        Parameter parameter = new Parameter(tmpParamType, paramName, true).requestBodyFound(
                            requestBodyFound);
                        parameterList.add(parameter);
                    }
                } else {

                    Parameter parameter = new Parameter(paramType, paramName).requestBodyFound(requestBodyFound);
                    parameterList.add(parameter);

                }
            }


        }
        return parameterList;
    }

    public String getAnnotationValue(PsiAnnotation annotation) {
        String paramName = null;
        PsiAnnotationMemberValue attributeValue = annotation.findDeclaredAttributeValue("value");

        if (attributeValue != null && attributeValue instanceof PsiLiteralExpression) {
            paramName = (String) ((PsiLiteralExpression) attributeValue).getValue();
        }
        return paramName;
    }


    public String buildRequestBodyJson(Parameter parameter) {
        Project project = psiMethod.getProject();
        final String className = parameter.getParamType();
        final String paramName = parameter.getParamName();

        return PsiClassHelper.create(Objects.requireNonNull(psiMethod.getContainingClass()))
            .withModule(myModule)
            .convertClassToJSON(className, paramName, project);
    }

    public String buildRequestBodyJson() {
        List<Parameter> parameterList = this.getParameterList();
        for (Parameter parameter : parameterList) {
            if (parameter.isRequestBodyFound()) {
                return buildRequestBodyJson(parameter);
            }
        }
        return null;
    }

    @NotNull
    public String buildServiceUriPath() {
        String ctrlPath = null;
        String methodPath = null;
        PsiClass containingClass = psiMethod.getContainingClass();
        RestSupportedAnnotationHelper annotationHelper;
        if (isSpringRestSupported(containingClass)) {
            ctrlPath = RequestMappingAnnotationHelper.getOneRequestMappingPath(containingClass);
            methodPath = RequestMappingAnnotationHelper.getOneRequestMappingPath(psiMethod);
        } else if (isJaxrsRestSupported(containingClass)) {
            ctrlPath = JaxrsAnnotationHelper.getClassUriPath(containingClass);
            methodPath = JaxrsAnnotationHelper.getMethodUriPath(psiMethod);
        }

        if (ctrlPath == null) {
            return null;
        }

        if (!ctrlPath.startsWith("/")) {
            ctrlPath = "/".concat(ctrlPath);
        }
        if (!ctrlPath.endsWith("/")) {
            ctrlPath = ctrlPath.concat("/");
        }
        if (methodPath.startsWith("/")) {
            methodPath = methodPath.substring(1, methodPath.length());
        }

        return ctrlPath + methodPath;
    }

    @NotNull
    public String buildServiceUriPathWithParams() {
        String serviceUriPath = buildServiceUriPath();
        String params = PsiMethodHelper.create(psiMethod).buildParamString();
        if (!params.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(serviceUriPath);
            return urlBuilder.append(serviceUriPath.contains("?") ? "&" : "?").append(params).toString();
        }
        return serviceUriPath;
    }


    @NotNull
    public String buildFullUrlWithParams() {
        String fullUrl = buildFullUrl();

        String params = buildParamString();
        if (!params.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(fullUrl);
            return urlBuilder.append(fullUrl.contains("?") ? "&" : "?").append(params).toString();
        }
        return fullUrl;
    }

    @NotNull
    public String buildFullUrl() {
        String hostUri =
            myModule != null ? ModuleHelper.create(myModule).getServiceHostPrefix() : ModuleHelper.DEFAULT_URI;

        String servicePath = buildServiceUriPath();

        return hostUri.concat(servicePath);
    }

}
