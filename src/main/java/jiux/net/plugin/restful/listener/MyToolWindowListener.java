package jiux.net.plugin.restful.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import jiux.net.plugin.restful.navigator.RestServicesNavigator;

/**
 * @author baihua.huang
 * @date 2023/3/31
 */

public class MyToolWindowListener implements ToolWindowManagerListener {
    private final Project project;

    public MyToolWindowListener(Project project) {
        this.project = project;
    }

    @Override
    public void stateChanged(ToolWindowManager toolWindowManager) {
        ToolWindow toolWindow = toolWindowManager.getToolWindow(RestServicesNavigator.TOOL_WINDOW_ID);
        if (toolWindow == null) {
            return ;
        }

        if (toolWindow.isDisposed()) {
            return;
        }
        boolean visible = toolWindow.isVisible();
        if (!visible) {
            return;
        }
        RestServicesNavigator servicesNavigator = RestServicesNavigator.getInstance(project);
        servicesNavigator.scheduleStructureUpdate();
    }
}
