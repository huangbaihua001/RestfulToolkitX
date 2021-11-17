package jiux.net.plugin.restful.common.resolver;


import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import jiux.net.plugin.restful.method.RequestPath;
import jiux.net.plugin.restful.method.action.PropertiesHandler;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;
import jiux.net.plugin.restful.annotations.PathMappingAnnotation;
import jiux.net.plugin.restful.annotations.SpringControllerAnnotation;
import jiux.net.plugin.restful.annotations.SpringRequestMethodAnnotation;
import jiux.net.plugin.restful.common.spring.RequestMappingAnnotationHelper;
import org.jetbrains.kotlin.idea.stubindex.KotlinAnnotationsIndex;
import org.jetbrains.kotlin.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SpringResolver extends BaseServiceResolver {
    /*    Module myModule;
        Project myProject;*/
    PropertiesHandler propertiesHandler;

    public SpringResolver(Module module) {
        myModule = module;
        propertiesHandler = new PropertiesHandler(module);
    }

    public SpringResolver(Project project) {
        myProject = project;

    }


    //Note: 当Controller 类上没有标记@RequestMapping 注解时，方法上的@RequestMapping 都是绝对路径。
    /*@Override
    public List<RestServiceItem> getServiceItemList(PsiMethod psiMethod) {
        List<RestServiceItem> itemList = new ArrayList<>();

        RequestPath[] methodRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiMethod);

        //TODO:  controller 没有设置requestMapping，默认“/”
        // TODO : controller 设置了(rest)controller 所有方法未指定 requestMapping，所有方法均匹配； 存在方法指定 requestMapping，则只解析这些方法；
//        String[] controllerPaths = RequestMappingAnnotationHelper.getRequestPaths(psiMethod.getContainingClass());
        List<RequestPath> classRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiMethod.getContainingClass());
        for (RequestPath classRequestPath : classRequestPaths) {
            for (RequestPath methodRequestPath : methodRequestPaths) {
                String path =  classRequestPath.getPath();
//                String path = tryReplacePlaceholderValueInPath( classRequestPath.getPath() );

                RestServiceItem item = createRestServiceItem(psiMethod, path, methodRequestPath);
                itemList.add(item);
            }
        }

        return itemList;
    }*/


    /*
    private String  tryReplacePlaceholderValueInPath(String path) {
        if (!path.contains("${")) return path;

        StringBuilder cleanerPath = new StringBuilder();

        for (String s : path.split("\\$\\{")) {
            if (s.contains("}")) {
                String placeholder = s.substring(0, s.indexOf("}"));
                String theOther = s.substring(s.indexOf("}")+1);

                String propertyValue = propertiesHandler.getProperty(placeholder);

                cleanerPath.append(StringUtils.isEmpty(propertyValue) ? s : propertyValue.trim()).append(theOther);
            } else {
                cleanerPath.append(s);
            }
        }
        return cleanerPath.toString();
    }*/


/*    @NotNull
//    @Override
    public List<PsiMethod> getServicePsiMethodList(Project project, GlobalSearchScope globalSearchScope) {
        List<PsiMethod> psiMethodList = new ArrayList<>();
        //TODO : 这块需要改写，支持更多注解方式。
        // TODO: 这种实现的局限了其他方式实现的url映射（xml（类似struts），webflux routers）
        SpringControllerAnnotation[] supportedAnnotations = SpringControllerAnnotation.values();
        //        for (SpringControllerAnnotation controllerAnnotations : SpringControllerAnnotation.values()) {
        for (PathMappingAnnotation controllerAnnotation : supportedAnnotations) {

            // 标注了 (Rest)Controller 注解的类，即 Controller 类
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(controllerAnnotation.getShortName(), project, globalSearchScope);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                PsiElement psiElement = psiModifierList.getParent();

                *//*if (!(psiElement instanceof PsiClass)) continue; // RestController annotation 只出现在class*//*
                PsiClass psiClass = (PsiClass) psiElement;
                PsiMethod[] psiMethods = psiClass.getMethods();
                // FIXME: 这里应该只包含 设置了，requstmapping的方法，除非所有方法都没标注 requstmapping
                if (psiMethods == null) {
                    continue;
                }

                for (PsiMethod psiMethod : psiMethods) {
                    // todo: 没有处理同时标注了 GET 和 POST 两种方法的方法，定义一个 RequestMapping 类{method，path}
                    psiMethodList.add(psiMethod);
                }
            }
        }
        return psiMethodList;
    }*/

    @Override
    public List<RestServiceItem> getRestServiceItemList(Project project, GlobalSearchScope globalSearchScope) {
        List<RestServiceItem> itemList = new ArrayList<>();

        // TODO: 这种实现的局限了其他方式实现的url映射（xml（类似struts），webflux routers）
        SpringControllerAnnotation[] supportedAnnotations = SpringControllerAnnotation.values();
        for (PathMappingAnnotation controllerAnnotation : supportedAnnotations) {

            // java: 标注了 (Rest)Controller 注解的类，即 Controller 类
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(controllerAnnotation.getShortName(), project, globalSearchScope);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                PsiElement psiElement = psiModifierList.getParent();
                /*if (!(psiElement instanceof PsiClass)) continue; // RestController annotation 只出现在 class*/
                PsiClass psiClass = (PsiClass) psiElement;
                List<RestServiceItem> serviceItemList = getServiceItemList(psiClass);
                itemList.addAll(serviceItemList);
            }
            // kotlin:
            Collection<KtAnnotationEntry> ktAnnotationEntries = KotlinAnnotationsIndex.getInstance().get(controllerAnnotation.getShortName(), project, globalSearchScope);
            for (KtAnnotationEntry ktAnnotationEntry : ktAnnotationEntries) {
                KtClass ktClass = (KtClass) ktAnnotationEntry.getParent().getParent();

                List<RequestPath> classRequestPaths = getRequestPaths(ktClass);

                List<KtNamedFunction> ktNamedFunctions = getKtNamedFunctions(ktClass);
                for (KtNamedFunction fun : ktNamedFunctions) {
                    List<RequestPath> requestPaths = getRequestPaths(fun);

                    for (RequestPath classRequestPath : classRequestPaths) {
                        for (RequestPath requestPath : requestPaths) {
                            requestPath.concat(classRequestPath);
                            itemList.add(createRestServiceItem(fun, "", requestPath));
                        }
                    }
                }

            }

        }

        return itemList;
    }

    protected List<RestServiceItem> getServiceItemList(PsiClass psiClass) {
        PsiMethod[] psiMethods = psiClass.getMethods();
        if (psiMethods == null) {
            return new ArrayList<>();
        }

        List<RestServiceItem> itemList = new ArrayList<>();
        List<RequestPath> classRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiClass);

        for (PsiMethod psiMethod : psiMethods) {
            RequestPath[] methodRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiMethod);

            for (RequestPath classRequestPath : classRequestPaths) {
                for (RequestPath methodRequestPath : methodRequestPaths) {
                    String path = classRequestPath.getPath();
                    RestServiceItem item = createRestServiceItem(psiMethod, path, methodRequestPath);
                    itemList.add(item);
                }
            }

        }
        return itemList;
    }

    private List<KtNamedFunction> getKtNamedFunctions(KtClass ktClass) {
        List<KtNamedFunction> ktNamedFunctions = new ArrayList<>();
        List<KtDeclaration> declarations = ktClass.getDeclarations();

        for (KtDeclaration declaration : declarations) {
            if (declaration instanceof KtNamedFunction) {
                KtNamedFunction fun = (KtNamedFunction) declaration;
                ktNamedFunctions.add(fun);

            }
        }
        return ktNamedFunctions;
    }

    private List<RequestPath> getRequestPaths(KtClass ktClass) {
        String defaultPath = "/";
        //方法注解
        List<KtAnnotationEntry> annotationEntries = ktClass.getModifierList().getAnnotationEntries();

        List<RequestPath> requestPaths = getRequestMappings(defaultPath, annotationEntries);
        return requestPaths;
    }

    private List<RequestPath> getRequestPaths(KtNamedFunction fun) {
        String defaultPath = "/";
        //方法注解
        KtModifierList modifierList = fun.getModifierList();
        if (modifierList != null) {
            List<KtAnnotationEntry> annotationEntries = modifierList.getAnnotationEntries();
            List<RequestPath> requestPaths = getRequestMappings(defaultPath, annotationEntries);
            return requestPaths;
        } else {
            return new ArrayList<>();
        }
    }

    private List<RequestPath> getRequestMappings(String defaultPath, List<KtAnnotationEntry> annotationEntries) {
        List<RequestPath> requestPaths = new ArrayList<>();
        for (KtAnnotationEntry entry : annotationEntries) {
            List<RequestPath> requestMappings = getRequestMappings(defaultPath, entry);
            requestPaths.addAll(requestMappings);
        }
        return requestPaths;
    }


    private List<RequestPath> getRequestMappings(String defaultPath, KtAnnotationEntry entry) {
        List<RequestPath> requestPaths = new ArrayList<>();
        List<String> methodList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();

        String annotationName = entry.getCalleeExpression().getText();
        SpringRequestMethodAnnotation requestMethodAnnotation = SpringRequestMethodAnnotation.getByShortName(annotationName);
        if (requestMethodAnnotation == null) {
            return new ArrayList<>();
        }

        if (requestMethodAnnotation.methodName() != null) { // GetMapping PostMapping ...
            methodList.add(requestMethodAnnotation.methodName());
        } else {
            methodList.addAll(getAttributeValues(entry, "method")); // RequestMapping
        }

        //注解参数值
        //KtValueArgumentList valueArgumentList = entry.getValueArgumentList();
        if (entry.getValueArgumentList() != null) {
            List<String> mappingValues = getAttributeValues(entry, null);
            if (!mappingValues.isEmpty()) {
                pathList.addAll(mappingValues);
            } else {
                // path
                pathList.addAll(getAttributeValues(entry, "value"));
            }
// path
            pathList.addAll(getAttributeValues(entry, "path"));
        }

        if (pathList.isEmpty()) {
            //没指定参数
            pathList.add(defaultPath);
        }

        if (methodList.size() > 0) {
            for (String method : methodList) {
                for (String path : pathList) {
                    requestPaths.add(new RequestPath(path, method));
                }
            }
        } else {
            for (String path : pathList) {
                requestPaths.add(new RequestPath(path, null));
            }
        }

        return requestPaths;
    }

    private List<String> getAttributeValues(KtAnnotationEntry entry, String attribute) {
        KtValueArgumentList valueArgumentList = entry.getValueArgumentList();

        if (valueArgumentList == null) return Collections.emptyList();

        List<KtValueArgument> arguments = valueArgumentList.getArguments();

        for (int i = 0; i < arguments.size(); i++) {
            KtValueArgument ktValueArgument = arguments.get(i);
            KtValueArgumentName argumentName = ktValueArgument.getArgumentName();

            KtExpression argumentExpression = ktValueArgument.getArgumentExpression();

            if ((argumentName == null && attribute == null) || (argumentName != null && argumentName.getText().equals(attribute))) {
                List<String> methodList = new ArrayList<>();
                // array, kotlin 1.1-
                if (argumentExpression.getText().startsWith("arrayOf")) {
                    List<KtValueArgument> pathValueArguments = ((KtCallExpression) argumentExpression).getValueArguments();
                    for (KtValueArgument pathValueArgument : pathValueArguments) {
                        methodList.add(pathValueArgument.getText().replace("\"", ""));
                    }
                    // array, kotlin 1.2+
                } else if (argumentExpression.getText().startsWith("[")) {
                    List<KtExpression> innerExpressions = ((KtCollectionLiteralExpression) argumentExpression).getInnerExpressions();
                    for (KtExpression ktExpression : innerExpressions) {
                        methodList.add(ktExpression.getText().replace("\"", ""));
                    }
                } else {
                    PsiElement[] paths = ktValueArgument.getArgumentExpression().getChildren();
                    methodList.add(paths.length == 0 ? "" : paths[0].getText());
                }

                return methodList;
            }
        }

        return new ArrayList<>();
    }


}
