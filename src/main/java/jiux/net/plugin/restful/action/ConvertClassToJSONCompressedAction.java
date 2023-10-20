package jiux.net.plugin.restful.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import java.awt.datatransfer.StringSelection;
import jiux.net.plugin.restful.common.PsiClassHelper;

public class ConvertClassToJSONCompressedAction extends ConvertClassToJSONAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
    PsiClass psiClass = getPsiClass(psiElement);

    if (psiClass == null) {
      return;
    }

    String json = PsiClassHelper.create(psiClass).convertClassToJSON(myProject(e), false);
    CopyPasteManager.getInstance().setContents(new StringSelection(json));
  }
}
