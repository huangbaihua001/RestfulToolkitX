package jiux.net.plugin.restful.navigation.action;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import javax.swing.Icon;
import jiux.net.plugin.restful.common.ToolkitIcons;
import jiux.net.plugin.restful.method.HttpMethod;
import jiux.net.plugin.restful.method.action.ModuleHelper;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtNamedFunction;

@ToString
public class RestServiceItem implements NavigationItem {

  private PsiMethod psiMethod;
  private PsiElement psiElement;
  private Module module;

  private String requestMethod;
  private HttpMethod method;

  private String url;
  private Navigatable navigationElement;
  private Boolean isUrlWithoutReqMethod = false;

  public RestServiceItem(PsiElement psiElement, String requestMethod, String urlPath, Boolean isUrlWithoutReqMethod) {
    this.psiElement = psiElement;
    if (psiElement instanceof PsiMethod) {
      this.psiMethod = (PsiMethod) psiElement;
    }
    this.requestMethod = requestMethod;
    if (requestMethod != null) {
      method = HttpMethod.getByRequestMethod(requestMethod);
    }

    this.url = urlPath;
    if (psiElement instanceof Navigatable) {
      navigationElement = (Navigatable) psiElement;
    }
    this.isUrlWithoutReqMethod = isUrlWithoutReqMethod;
  }
  public RestServiceItem(PsiElement psiElement, String requestMethod, String urlPath) {
    this(psiElement, requestMethod, urlPath, false);
  }

  @Nullable
  @Override
  public String getName() {
    return /*this.requestMethod + " " +*/this.url;
  }

  @Nullable
  @Override
  public ItemPresentation getPresentation() {
    return new RestServiceItemPresentation();
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (navigationElement != null) {
      navigationElement.navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return navigationElement.canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return true;
  }

  public boolean matches(String queryText) {
    String pattern = queryText;
    if (pattern.equals("/")) {
      return true;
    }

    com.intellij.psi.codeStyle.MinusculeMatcher matcher =
      com.intellij.psi.codeStyle.NameUtil.buildMatcher(
        "*" + pattern,
        com.intellij.psi.codeStyle.NameUtil.MatchingCaseSensitivity.NONE
      );
    return matcher.matches(this.url);
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public PsiMethod getPsiMethod() {
    return psiMethod;
  }

  public void setPsiMethod(PsiMethod psiMethod) {
    this.psiMethod = psiMethod;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFullUrl() {
    if (module == null) {
      return getUrl();
    }

    ModuleHelper moduleHelper = ModuleHelper.create(module);
    return moduleHelper.getServiceHostPrefix() + getUrl();
  }

  public PsiElement getPsiElement() {
    return psiElement;
  }

  public String getKey() {
    return this.module.getName() + this.getFullUrl() + this.getMethod();
  }

  private class RestServiceItemPresentation implements ItemPresentation {

    @Nullable
    @Override
    public String getPresentableText() {
      return url;
    }

    @Nullable
    @Override
    public String getLocationString() {
      String fileName = psiElement.getContainingFile().getName();

      String location = null;

      if (psiElement instanceof PsiMethod) {
        PsiMethod psiMethod = ((PsiMethod) psiElement);
        if (module != null) {
          location =
            module.getName() +
            "#" +
            psiMethod
              .getContainingClass()
              .getName()
              .concat("#")
              .concat(psiMethod.getName());
        } else {
          location =
            psiMethod
              .getContainingClass()
              .getName()
              .concat("#")
              .concat(psiMethod.getName());
        }
      } else if (psiElement instanceof KtNamedFunction) {
        KtNamedFunction ktNamedFunction =
          (KtNamedFunction) RestServiceItem.this.psiElement;
        String className = ((KtClass) psiElement.getParent().getParent()).getName();
        if (module != null) {
          location =
            module.getName() +
            "#" +
            className.concat("#").concat(ktNamedFunction.getName());
        } else {
          location = className.concat("#").concat(ktNamedFunction.getName());
        }
      }

      return "(" + location + ")";
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
      return ToolkitIcons.METHOD.get(method);
    }
  }
}
