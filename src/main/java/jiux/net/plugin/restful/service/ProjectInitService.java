package jiux.net.plugin.restful.service;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import java.util.List;
import jiux.net.plugin.restful.common.ServiceHelper;
import jiux.net.plugin.restful.navigator.RestServiceProject;

/**
 * @author baihua.huang
 */
@Service(Service.Level.PROJECT)
public class ProjectInitService implements Disposable {

  private final Project myProject;

  public ProjectInitService(Project project) {
    myProject = project;
  }

  public static ProjectInitService getInstance(Project p) {
    return p.getService(ProjectInitService.class);
  }

  public List<RestServiceProject> getServiceProjects() {
    return DumbService
      .getInstance(myProject)
      .runReadActionInSmartMode(() ->
        ServiceHelper.buildRestServiceProjectListUsingResolver(myProject)
      );
  }

  @Override
  public void dispose() {}
}
