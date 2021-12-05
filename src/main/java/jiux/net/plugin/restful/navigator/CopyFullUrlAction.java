package jiux.net.plugin.restful.navigator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAware;
import jiux.net.plugin.restful.navigation.action.RestServiceItem;
import jiux.net.plugin.utils.RestServiceDataKeys;

import java.awt.datatransfer.StringSelection;
import java.util.List;

public class CopyFullUrlAction extends AnAction implements DumbAware {
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        Presentation p = e.getPresentation();
        p.setEnabled(isAvailable(e));
        p.setVisible(isVisible(e));
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<RestServiceItem> serviceItems = RestServiceDataKeys.SERVICE_ITEMS.getData(e.getDataContext());
        StringBuilder sb = new StringBuilder();
        assert serviceItems != null;
        for (RestServiceItem serviceItem : serviceItems) {
            if (sb.length() > 1) {
                sb.append("\n\n");
            }
            sb.append(serviceItem.getFullUrl());
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(sb.toString()));
    }


    protected boolean isAvailable(AnActionEvent e) {
        return true;
    }

    protected boolean isVisible(AnActionEvent e) {
        return true;
    }
}