package jiux.net.plugin.restful.navigator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.util.PsiNavigateUtil;
import java.util.List;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;
import jiux.net.plugin.utils.RestServiceDataKeys;

public class EditSourceAction extends AnAction implements DumbAware {

  @Override
  public void update(AnActionEvent e) {
    super.update(e);
    Presentation p = e.getPresentation();
    p.setVisible(isVisible(e));
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    List<RestServiceItem> serviceItems = RestServiceDataKeys.SERVICE_ITEMS.getData(
      e.getDataContext()
    );

    for (RestServiceItem serviceItem : serviceItems) {
      PsiNavigateUtil.navigate(serviceItem.getPsiElement());
    }
  }

  protected boolean isVisible(AnActionEvent e) {
    return true;
  }
}
