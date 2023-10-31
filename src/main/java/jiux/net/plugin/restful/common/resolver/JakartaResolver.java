package jiux.net.plugin.restful.common.resolver;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jiux.net.plugin.restful.annotations.JakartaPathAnnotation;
import jiux.net.plugin.restful.common.jakarta.JakartaAnnotationHelper;
import jiux.net.plugin.restful.method.RequestPath;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;

public class JakartaResolver extends BaseServiceResolver {

  public JakartaResolver(Module module) {
    myModule = module;
  }

  public JakartaResolver(Project project) {
    myProject = project;
  }

  @Override
  public List<RestServiceItem> getRestServiceItemList(
    Project project,
    GlobalSearchScope globalSearchScope
  ) {
    List<RestServiceItem> itemList = new ArrayList<>();
    Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex
      .getInstance()
      .get(JakartaPathAnnotation.PATH.getShortName(), project, globalSearchScope);

    for (PsiAnnotation psiAnnotation : psiAnnotations) {
      PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
      PsiElement psiElement = psiModifierList.getParent();

      if (!(psiElement instanceof PsiClass)) {
        continue;
      }

      PsiClass psiClass = (PsiClass) psiElement;
      PsiMethod[] psiMethods = psiClass.getMethods();

      String classUriPath = JakartaAnnotationHelper.getClassUriPath(psiClass);
      System.out.println("classUriPath->" + classUriPath);

      for (PsiMethod psiMethod : psiMethods) {
        RequestPath[] methodUriPaths = JakartaAnnotationHelper.getRequestPaths(psiMethod);

        for (RequestPath methodUriPath : methodUriPaths) {
          RestServiceItem item = createRestServiceItem(
            psiMethod,
            classUriPath,
            methodUriPath,
            false
          );
          itemList.add(item);
        }
      }
    }
    return itemList;
  }
}
