package jiux.net.plugin.restful.navigation.action;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.gotoByName.CustomMatcherModel;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import java.util.Collection;
import javax.swing.*;
import jiux.net.plugin.restful.common.spring.AntPathMatcher;
import jiux.net.plugin.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model for "Go to | File" action
 */
public class GotoRequestMappingModel
  extends FilteringGotoByModel<HttpMethod>
  implements DumbAware, CustomMatcherModel {

  protected GotoRequestMappingModel(
    @NotNull Project project,
    @NotNull ChooseByNameContributor[] contributors
  ) {
    super(project, contributors);
  }

  // TODO: filer module? FilteringGotoByModel.acceptItem
  //  override setFilterItems or getFilterItems()
  //  (for example: GotoClassModel2 filter language，override getFilterItems())
  @Nullable
  @Override
  protected HttpMethod filterValueFor(NavigationItem item) {
    if (item instanceof RestServiceItem) {
      return ((RestServiceItem) item).getMethod();
    }

    return null;
  }

  @Nullable
  @Override
  protected synchronized Collection<HttpMethod> getFilterItems() {
    return super.getFilterItems();
  }

  @Override
  public String getPromptText() {
    return "Enter service URL path :";
  }

  @NotNull
  @Override
  public String getNotInMessage() {
    return IdeBundle.message("label.no.matches.found.in.project", getProject().getName());
  }

  @NotNull
  @Override
  public String getNotFoundMessage() {
    return IdeBundle.message("label.no.matches.found");
  }

  @Override
  public char getCheckBoxMnemonic() {
    return SystemInfo.isMac ? 'P' : 'n';
  }

  @Override
  public boolean loadInitialCheckBoxState() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
    return propertiesComponent.isTrueValue("GoToRestService.OnlyCurrentModule");
  }

  @Override
  public void saveInitialCheckBoxState(boolean state) {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
    if (propertiesComponent.isTrueValue("GoToRestService.OnlyCurrentModule")) {
      propertiesComponent.setValue(
        "GoToRestService.OnlyCurrentModule",
        Boolean.toString(state)
      );
    }
  }

  @Nullable
  @Override
  public String getFullName(Object element) {
    return getElementName(element);
  }

  @NotNull
  @Override
  public String[] getSeparators() {
    return new String[] { "/", "?" };
  }

  /**
   * return null to hide checkbox panel
   */
  @Nullable
  @Override
  public String getCheckBoxName() {
    return "Only This Module";
  }

  @Override
  public boolean willOpenEditor() {
    return true;
  }

  //    CustomMatcherModel 接口，Allows to implement custom matcher for matching items from ChooseByName popup
  // todo: resolve PathVariable annotation
  @Override
  public boolean matches(@NotNull String popupItem, @NotNull String userPattern) {
    String pattern = userPattern;
    if (pattern.equals("/")) {
      return true;
    }
    // REST style params:  @RequestMapping(value="{departmentId}/employees/{employeeId}")  PathVariable
    // REST style params(regex) @RequestMapping(value="/{textualPart:[a-z-]+}.{numericPart:[\\d]+}")  PathVariable

    MinusculeMatcher matcher = NameUtil.buildMatcher(
      "*" + pattern,
      NameUtil.MatchingCaseSensitivity.NONE
    );
    boolean matches = matcher.matches(popupItem);
    if (!matches) {
      AntPathMatcher pathMatcher = new AntPathMatcher();
      matches = pathMatcher.match(popupItem, userPattern);
    }
    return matches;
  }

  @NotNull
  @Override
  public String removeModelSpecificMarkup(@NotNull String pattern) {
    return super.removeModelSpecificMarkup(pattern);
  }

  /* TODO : render override */
  @Override
  public ListCellRenderer getListCellRenderer() {
    return super.getListCellRenderer();
  }
}
