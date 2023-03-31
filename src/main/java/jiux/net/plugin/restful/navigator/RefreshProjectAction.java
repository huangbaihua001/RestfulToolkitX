package jiux.net.plugin.restful.navigator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RefreshProjectAction extends AnAction {

    @Nullable
    public static Project getProject(DataContext context) {
        return CommonDataKeys.PROJECT.getData(context);
    }

    protected void perform(@NotNull RestServiceProjectsManager manager) {
        manager.forceUpdateAllProjects();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = getProject(e.getDataContext());
        assert project != null;
        RestServicesNavigator servicesNavigator = RestServicesNavigator.getInstance(project);

        servicesNavigator.initComponent();
        servicesNavigator.scheduleStructureUpdate();
    }
}
