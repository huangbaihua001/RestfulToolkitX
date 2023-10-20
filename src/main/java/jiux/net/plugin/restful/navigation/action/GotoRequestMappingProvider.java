package jiux.net.plugin.restful.navigation.action;

import com.intellij.ide.util.gotoByName.ChooseByNameViewModel;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.util.Processor;
import java.util.List;
import jiux.net.plugin.utils.ToolkitUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GotoRequestMappingProvider extends DefaultChooseByNameItemProvider {

  public GotoRequestMappingProvider(@Nullable PsiElement context) {
    super(context);
  }

  @NotNull
  private static MinusculeMatcher buildPatternMatcher(
    @NotNull String pattern,
    @NotNull NameUtil.MatchingCaseSensitivity caseSensitivity
  ) {
    return NameUtil.buildMatcher(pattern, caseSensitivity);
  }

  @NotNull
  @Override
  public List<String> filterNames(
    @NotNull ChooseByNameViewModel base,
    @NotNull String[] names,
    @NotNull String pattern
  ) {
    return super.filterNames(base, names, pattern);
  }

  @Override
  public boolean filterElements(
    @NotNull ChooseByNameViewModel base,
    @NotNull String pattern,
    boolean everywhere,
    @NotNull ProgressIndicator indicator,
    @NotNull Processor<Object> consumer
  ) {
    pattern = ToolkitUtil.removeRedundancyMarkup(pattern);

    return super.filterElements(base, pattern, everywhere, indicator, consumer);
  }
}
