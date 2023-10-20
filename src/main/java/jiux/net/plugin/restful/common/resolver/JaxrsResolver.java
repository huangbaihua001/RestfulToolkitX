package jiux.net.plugin.restful.common.resolver;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jiux.net.plugin.restful.annotations.JaxrsPathAnnotation;
import jiux.net.plugin.restful.common.jaxrs.JaxrsAnnotationHelper;
import jiux.net.plugin.restful.method.RequestPath;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;

public class JaxrsResolver extends BaseServiceResolver {

  public JaxrsResolver(Module module) {
    myModule = module;
  }

  public JaxrsResolver(Project project) {
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
      .get(JaxrsPathAnnotation.PATH.getShortName(), project, globalSearchScope);

    for (PsiAnnotation psiAnnotation : psiAnnotations) {
      PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
      PsiElement psiElement = psiModifierList.getParent();

      if (!(psiElement instanceof PsiClass)) {
        continue;
      }

      PsiClass psiClass = (PsiClass) psiElement;
      PsiMethod[] psiMethods = psiClass.getMethods();

      if (psiMethods == null) {
        continue;
      }

      String classUriPath = JaxrsAnnotationHelper.getClassUriPath(psiClass);

      for (PsiMethod psiMethod : psiMethods) {
        RequestPath[] methodUriPaths = JaxrsAnnotationHelper.getRequestPaths(psiMethod);

        for (RequestPath methodUriPath : methodUriPaths) {
          RestServiceItem item = createRestServiceItem(
            psiMethod,
            classUriPath,
            methodUriPath
          );
          itemList.add(item);
        }
      }
    }
    return itemList;
  }
}
