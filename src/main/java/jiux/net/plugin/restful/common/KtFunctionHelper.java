package jiux.net.plugin.restful.common;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.asJava.LightClassUtilsKt;
import org.jetbrains.kotlin.modules.Module;
import org.jetbrains.kotlin.psi.KtNamedFunction;

public class KtFunctionHelper extends PsiMethodHelper {

  KtNamedFunction ktNamedFunction;
  Project myProject;
  Module myModule;

  private final String pathSeparator = "/";

  protected KtFunctionHelper(@NotNull KtNamedFunction ktNamedFunction) {
    super(null);
    List<PsiMethod> psiMethods = LightClassUtilsKt.toLightMethods(ktNamedFunction);
    super.psiMethod = psiMethods.get(0);
    this.ktNamedFunction = ktNamedFunction;
  }

  public static KtFunctionHelper create(@NotNull KtNamedFunction psiMethod) {
    return new KtFunctionHelper(psiMethod);
  }

  public KtFunctionHelper withModule(Module module) {
    this.myModule = module;
    return this;
  }

  @NotNull
  @Override
  protected Project getProject() {
    myProject = psiMethod.getProject();
    return myProject;
  }

  @Override
  public String buildParamString() {
    StringBuilder param = new StringBuilder("");
    Map<String, Object> baseTypeParamMap = getBaseTypeParameterMap();

    if (baseTypeParamMap.size() > 0) {
      baseTypeParamMap.forEach((s, o) -> param.append(s).append("=").append(o).append("&")
      );
    }

    return param.length() > 0 ? param.deleteCharAt(param.length() - 1).toString() : "";
  }
}
