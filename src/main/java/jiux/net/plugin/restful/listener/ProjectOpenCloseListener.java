package jiux.net.plugin.restful.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import jiux.net.plugin.restful.navigator.RestServicesNavigator;
import jiux.net.plugin.utils.ToolkitUtil;
import org.jetbrains.annotations.NotNull;

public class ProjectOpenCloseListener implements ProjectManagerListener {

  @Override
  public void projectOpened(@NotNull Project project) {
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      return;
    }

    RestServicesNavigator restServicesNavigator = RestServicesNavigator.getInstance(
      project
    );
    restServicesNavigator.listenForProjectsChanges();
    ToolkitUtil.runWhenInitialized(
      project,
      () -> {
        if (project.isDisposed()) {
          return;
        }
        restServicesNavigator.initToolWindow();
      }
    );
  }
}
