package jiux.net.plugin.restful.common.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.ArrayList;
import java.util.List;
import jiux.net.plugin.restful.method.RequestPath;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;
import org.jetbrains.annotations.NotNull;

public abstract class BaseServiceResolver implements ServiceResolver {

  public static final Logger LOG = Logger.getInstance(BaseServiceResolver.class);
  Module myModule;
  Project myProject;

  @Override
  public List<RestServiceItem> findAllSupportedServiceItemsInModule() {
    List<RestServiceItem> itemList = new ArrayList<>();
    if (myModule == null) {
      return itemList;
    }

    GlobalSearchScope globalSearchScope = GlobalSearchScope.moduleScope(myModule);

    itemList = getRestServiceItemList(myModule.getProject(), globalSearchScope);
    return itemList;
  }

  public abstract List<RestServiceItem> getRestServiceItemList(
    Project project,
    GlobalSearchScope globalSearchScope
  );

  @Override
  public List<RestServiceItem> findAllSupportedServiceItemsInProject() {
    List<RestServiceItem> itemList = null;
    if (myProject == null && myModule != null) {
      myProject = myModule.getProject();
    }

    if (myProject == null) {
      return new ArrayList<>();
    }

    GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(myProject);
    itemList = getRestServiceItemList(myProject, globalSearchScope);
    return itemList;
  }

  @NotNull
  protected RestServiceItem createRestServiceItem(
    PsiElement psiMethod,
    String classUriPath,
    RequestPath requestMapping
  ) {
    return createRestServiceItem(psiMethod, classUriPath, requestMapping, true);
  }

  @NotNull
  protected RestServiceItem createRestServiceItem(
    PsiElement psiMethod,
    String classUriPath,
    RequestPath requestMapping,
    Boolean isUrlWithoutReqMethod
  ) {
    System.out.printf("psiMethod:%s,classUriPath:%s,requestMapping:%s,isUrlWithoutReqMethod:%s%n",
    psiMethod,
    classUriPath,
    requestMapping,
    isUrlWithoutReqMethod);
    if (!classUriPath.startsWith("/")) {
      classUriPath = "/".concat(classUriPath);
    }
    if (!classUriPath.endsWith("/")) {
      classUriPath = classUriPath.concat("/");
    }

    String methodPath = requestMapping.getPath();

    if (methodPath.startsWith("/")) {
      methodPath = methodPath.substring(1);
    }
    String requestPath = classUriPath + methodPath;

    RestServiceItem item = new RestServiceItem(
      psiMethod,
      requestMapping.getMethod(),
      requestPath,
      isUrlWithoutReqMethod
    );
    if (myModule != null) {
      item.setModule(myModule);
    }
    System.err.println("item:" + item);
    return item;
  }
}
