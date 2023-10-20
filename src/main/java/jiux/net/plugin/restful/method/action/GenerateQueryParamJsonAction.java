package jiux.net.plugin.restful.method.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import jiux.net.plugin.restful.common.PsiMethodHelper;
import jiux.net.plugin.restful.method.Parameter;
import org.jetbrains.kotlin.asJava.LightClassUtilsKt;
import org.jetbrains.kotlin.psi.KtClassOrObject;
import org.jetbrains.kotlin.psi.KtNamedFunction;

/**
 * Generate Request Body JSON string
 */
public class GenerateQueryParamJsonAction extends SpringAnnotatedMethodAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    //   @RequestBody entity generates json

    PsiMethod psiMethod = null;
    PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
    if (psiElement instanceof PsiMethod) {
      psiMethod = (PsiMethod) psiElement;
    }

    if (psiElement instanceof KtNamedFunction) {
      KtNamedFunction ktNamedFunction = (KtNamedFunction) psiElement;
      PsiElement parentPsi = psiElement.getParent().getParent();
      if (parentPsi instanceof KtClassOrObject) {
        List<PsiMethod> psiMethods = LightClassUtilsKt.toLightMethods(ktNamedFunction);
        psiMethod = psiMethods.get(0);
      }
    }

    PsiMethodHelper psiMethodHelper = PsiMethodHelper.create(psiMethod);
    List<Parameter> parameterList = psiMethodHelper.getParameterList();
    for (Parameter parameter : parameterList) {
      if (parameter.isRequestBodyFound()) {
        String queryJson = psiMethodHelper.buildRequestBodyJson(parameter);
        CopyPasteManager.getInstance().setContents(new StringSelection(queryJson));
        break;
      }
    }
  }

  @Override
  public boolean displayTextInToolbar() {
    return true;
  }
}
