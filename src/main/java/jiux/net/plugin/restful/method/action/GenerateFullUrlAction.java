package jiux.net.plugin.restful.method.action;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_ELEMENT;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import jiux.net.plugin.restful.common.PsiMethodHelper;
import org.jetbrains.kotlin.asJava.LightClassUtilsKt;
import org.jetbrains.kotlin.psi.KtClassOrObject;
import org.jetbrains.kotlin.psi.KtNamedFunction;

/**
 * Generate & Copy full url
 * TODO: RequestMapping many value
 */
public class GenerateFullUrlAction extends SpringAnnotatedMethodAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    Module module = myModule(e);
    PsiElement psiElement = e.getData(PSI_ELEMENT);
    if (psiElement instanceof PsiMethod) {
      PsiMethod psiMethod = (PsiMethod) psiElement;
      ModuleHelper moduleHelper = ModuleHelper.create(module);

      String url = PsiMethodHelper
        .create(psiMethod)
        .withModule(module)
        .buildFullUrlWithParams();

      CopyPasteManager.getInstance().setContents(new StringSelection(url));
    }

    if (psiElement instanceof KtNamedFunction) {
      KtNamedFunction ktNamedFunction = (KtNamedFunction) psiElement;
      PsiElement parentPsi = psiElement.getParent().getParent();
      if (parentPsi instanceof KtClassOrObject) {
        List<PsiMethod> psiMethods = LightClassUtilsKt.toLightMethods(ktNamedFunction);
        PsiMethod psiMethod = psiMethods.get(0);
        ModuleHelper moduleHelper = ModuleHelper.create(module);

        String url = PsiMethodHelper
          .create(psiMethod)
          .withModule(module)
          .buildFullUrlWithParams();
        CopyPasteManager.getInstance().setContents(new StringSelection(url));
      }
    }
  }
}
