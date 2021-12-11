package jiux.net.plugin.restful.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaShortClassNameIndex;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.util.text.DateFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;


public class PsiClassHelper {
    private static int autoCorrelationCount = 0;
    PsiClass psiClass;
    private int listIterateCount = 0;
    private Module myModule;

    protected PsiClassHelper(@NotNull PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public static PsiClassHelper create(@NotNull PsiClass psiClass) {
        return new PsiClassHelper(psiClass);
    }

    @Nullable
    public static Object getJavaBaseTypeDefaultValue(String paramType) {
        Object paramValue = null;
//        todo: using map later
        switch (paramType.toLowerCase()) {
            case "byte":
                paramValue = Byte.valueOf("1");
                break;
            case "char":
            case "character":
                paramValue = 'Z';
                break;
            case "boolean":
                paramValue = Boolean.TRUE;
                break;
            case "int":
            case "integer":
                paramValue = 1;
                break;
            case "double":
                paramValue = 1.0;
                break;
            case "float":
                paramValue = 1.0F;
                break;
            case "long":
                paramValue = 1L;
                break;
            case "short":
                paramValue = Short.valueOf("1");
                break;
            case "decimal":
                return BigDecimal.ONE;
            case "string":
                paramValue = "demoData";
                break;
            case "date":
                paramValue = DateFormatUtil.formatDateTime(new Date());
                break; // todo: format date
//            default: paramValue = paramType;
        }
        return paramValue;
    }


    private static boolean isListFieldType(PsiType psiFieldType) {
        if (!(psiFieldType instanceof PsiClassReferenceType)) {
            return false;
        }

        PsiClass resolvePsiClass = ((PsiClassReferenceType) psiFieldType).resolve();
        if (resolvePsiClass.getQualifiedName().equals("java.util.List")) {
            return true;
        }

        for (PsiType psiType : ((PsiClassReferenceType) psiFieldType).rawType().getSuperTypes()) {
            if (psiType.getCanonicalText().equals("java.util.List")) {
                return true;
            }
        }

        return false;

/*        ((PsiClassReferenceType) psiFieldType).rawType().getCanonicalText().equals("java.util.List");

        resolvePsiClass.getInterfaces();*/

//        rawType().getSuperTypes()/*
    }


    private static boolean isEnum(PsiType psiFieldType) {
        if (!(psiFieldType instanceof PsiClassReferenceType)) {
            return false;
        }
        return ((PsiClassReferenceType) psiFieldType).resolve().isEnum();
    }

    @NotNull
    protected Project getProject() {
        return psiClass.getProject();
    }

    public String convertClassToJSON(String className, Project project) {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        String queryJson;
        //list params
        if (className.contains("List<")) {
            List<Map<String, Object>> jsonList = new ArrayList<>();

            // didn't handle generic nesting.
            String entityName = className.substring(className.indexOf("<") + 1, className.lastIndexOf(">"));

            // build RequestBody Json
            Map<String, Object> jsonMap = assembleClassToMap(entityName, project);
            jsonList.add(jsonMap);
            queryJson = gson.toJson(jsonList);
        } else {
            // build RequestBody Json
            queryJson = convertPojoEntityToJSON(className, project);
        }
        return queryJson;
    }

    private String convertPojoEntityToJSON(String className, Project project) {
        String queryJson;
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        Map<String, Object> jsonMap = assembleClassToMap(className, project);
        queryJson = gson.toJson(jsonMap);
        return queryJson;
    }

    public String convertClassToJSON(Project project, boolean prettyFormat) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (prettyFormat) {
            gsonBuilder.setPrettyPrinting();
        }
        Gson gson = gsonBuilder.create();
        Map<String, Object> jsonMap = new HashMap<>();

        if (psiClass != null) {
            jsonMap = assembleClassToMap(psiClass, project);
        }
        return gson.toJson(jsonMap);
    }

    private boolean isJavaBaseType(String typeName) {
        return getJavaBaseTypeDefaultValue(typeName) != null;
    }

    private Object setFieldDefaultValue(PsiType psiFieldType, Project project) {
        String typeName = psiFieldType.getPresentableText();
        Object baseTypeDefaultValue = getJavaBaseTypeDefaultValue(typeName);
        if (baseTypeDefaultValue != null) {
            return baseTypeDefaultValue;
        }


        if (psiFieldType instanceof PsiClassReferenceType) {
            String className = ((PsiClassReferenceType) psiFieldType).getClassName();
//            PsiUtil.getTopLevelClass(psiFieldType);
            if (className.equalsIgnoreCase("List") || className.equalsIgnoreCase("ArrayList")) {

                PsiType[] parameters = ((PsiClassReferenceType) psiFieldType).getParameters();
                if (parameters != null && parameters.length > 0) {
                    PsiType parameter = parameters[0];
//                    if(parameter.getPresentableText().equals(psiclass.getname))
                }

                return handleListParam(psiFieldType, project);
            }

            ((PsiClassReferenceType) psiFieldType).resolve().getFields(); // if is Enum

            String fullName = psiFieldType.getCanonicalText();
            PsiClass fieldClass = findOnePsiClassByClassName(fullName, project);


            if (fieldClass != null) {
//                todo:  autoCorrelationCount
                if (autoCorrelationCount > 0) {
                    return new HashMap();
                }
                if (fullName.equals(fieldClass.getQualifiedName())) {
                    autoCorrelationCount++;
                }

                return assembleClassToMap(fieldClass, project);
            }
        }


        return typeName;
    }

    @Nullable
    public PsiClass findOnePsiClassByClassName(String qualifiedClassName, Project project) {
        return JavaPsiFacade.getInstance(project).findClass(qualifiedClassName, GlobalSearchScope.allScope(project));
    }

    @Nullable
    protected PsiClass findOnePsiClassByClassName_deprecated(String className, Project project) {
        PsiClass psiClass = null;

        String shortClassName = className.substring(className.lastIndexOf(".") + 1, className.length());
        Collection<PsiClass> psiClassCollection = tryDetectPsiClassByShortClassName(project, shortClassName);
        if (psiClassCollection.size() == 0) {

            return null;
        }
        if (psiClassCollection.size() == 1) {
            psiClass = psiClassCollection.iterator().next();
        }

        if (psiClassCollection.size() > 1) {
            //找import中对应的class
//            psiClass = psiClassCollection.stream().filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findFirst().get();

            Optional<PsiClass> any = psiClassCollection.stream().filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findAny();

            if (any.isPresent()) {
                psiClass = any.get();
            }

        }
        return psiClass;
    }

    public Collection<PsiClass> tryDetectPsiClassByShortClassName(Project project, String shortClassName) {
        Collection<PsiClass> psiClassCollection = JavaShortClassNameIndex.getInstance().get(shortClassName, project, GlobalSearchScope.projectScope(project));

        if (psiClassCollection.size() > 0) {
            return psiClassCollection;
        }

        if (myModule != null) {
            psiClassCollection = JavaShortClassNameIndex.getInstance().get(shortClassName, project, GlobalSearchScope.allScope(project));
        }

        return psiClassCollection;
    }

    @Nullable
    public PsiClass findOnePsiClassByClassName2(String className, Project project) {
        PsiClass psiClass = null;
        String shortClassName = className.substring(className.lastIndexOf(".") + 1, className.length());

        PsiClass[] psiClasses = tryDetectPsiClassByShortClassName2(shortClassName, project);
        if (psiClasses.length == 0) {

            return null;
        }
        if (psiClasses.length == 1) {
            psiClass = psiClasses[0];
            return psiClass;
        }

        if (psiClasses.length > 1) {
            Optional<PsiClass> any = Arrays.stream(psiClasses).filter(tempPsiClass -> tempPsiClass.getQualifiedName().equals(className)).findAny();
            if (any.isPresent()) {
                psiClass = any.get();
            }

            for (PsiClass aClass : psiClasses) {

            }

        }
        return psiClass;
    }

    public PsiClass[] tryDetectPsiClassByShortClassName2(String shortClassName, Project project) {
        // all
        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(shortClassName, GlobalSearchScope.allScope(project));

        if (psiClasses.length > 0) {
            return psiClasses;
        }

        if (myModule != null) {
            // all
            psiClasses = PsiShortNamesCache.getInstance(project).getClassesByName(shortClassName, GlobalSearchScope.allScope(project));
            if (psiClasses.length > 0) {
                return psiClasses;
            }
        }

        return new PsiClass[0];
    }

    public Map<String, Object> assembleClassToMap(String className, Project project) {
        PsiClass psiClass = findOnePsiClassByClassName(className, project);

        Map<String, Object> jsonMap = new HashMap<>();
        if (psiClass != null) {
            jsonMap = assembleClassToMap(psiClass, project);
        }
        return jsonMap;
    }

    public Map<String, Object> assembleClassToMap(PsiClass psiClass, Project project) {
        int defaultRecursiveCount = 1;
        return assembleClassToMap(psiClass, project, defaultRecursiveCount);
    }

    public Map<String, Object> assembleClassToMap(PsiClass psiClass, Project project, int recursiveCount) {
        Map<String, Object> map = new LinkedHashMap<>();
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiType psiFieldType = field.getType();
            String typeName = psiFieldType.getPresentableText();
            String fieldName = field.getName();

            if (isJavaBaseType(typeName)) {
                map.put(fieldName, getJavaBaseTypeDefaultValue(typeName));
                continue;
            }

            if (psiFieldType instanceof PsiArrayType) {
                PsiType psiType = ((PsiArrayType) psiFieldType).getComponentType();

                Object baseTypeDefaultValue = getJavaBaseTypeDefaultValue(psiType.getPresentableText());
                if (baseTypeDefaultValue != null) {
                    List<Object> objects = new ArrayList<>();
                    objects.add(baseTypeDefaultValue);
                    map.put(fieldName, objects);
                }

                continue;
            }

            PsiClass resolveClass = ((PsiClassReferenceType) psiFieldType).resolve();
            if (isEnum(psiFieldType)) {
                PsiField psiField = resolveClass.getFields()[0];
                map.put(fieldName, psiField.getName());
                continue;
            }

//            self recursion
            if (resolveClass.getQualifiedName().equals(psiClass.getQualifiedName())) {
                if (recursiveCount > 0) {
                    Map<String, Object> objectMap = assembleClassToMap(resolveClass, project, 0);
                    map.put(fieldName, objectMap);
                    continue;
                }
            }

            if (isListFieldType(psiFieldType)) {
                PsiType[] parameters = ((PsiClassReferenceType) psiFieldType).getParameters();
                if (parameters != null && parameters.length > 0) {
                    PsiType parameter = parameters[0];
                    if (recursiveCount <= 0) {
                        continue;
                    }

                    if (parameter.getPresentableText().equals(psiClass.getName())) {
                        Map<String, Object> objectMap = assembleClassToMap(psiClass, project, 0);
                        map.put(fieldName, objectMap);
                        continue;
                    }

                    Object baseTypeDefaultValue = getJavaBaseTypeDefaultValue(parameter.getPresentableText());
                    if (baseTypeDefaultValue != null) {
                        List<Object> objects = new ArrayList<>();
                        objects.add(baseTypeDefaultValue);
                        map.put(fieldName, objects);
                        continue;
                    }

                    // TODO TODO TODO .................
                    if (parameter instanceof PsiClassReferenceType) {
                        if (parameter.getPresentableText().contains("<")) {
                            continue;
                        }
                        PsiClass onePsiClassByClassName = findOnePsiClassByClassName(parameter.getCanonicalText(), project);

                        Map<String, Object> objectMap = assembleClassToMap(onePsiClassByClassName, project, 0);
                        map.put(fieldName, objectMap);
                    }
                }
            }
        }

        return map;
    }

    private Object handleListParam(PsiType psiType, Project project) {
        List<Object> list = new ArrayList();
        PsiClassType classType = (PsiClassType) psiType;
        PsiType[] subTypes = classType.getParameters();
        if (subTypes.length > 0) {
            PsiType subType = subTypes[0];
            list.add(setFieldDefaultValue(subType, project));
        }
        return list;
    }


    public PsiClassHelper withModule(Module module) {
        this.myModule = module;
        return this;
    }
}
