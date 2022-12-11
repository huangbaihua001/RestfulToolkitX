package jiux.net.plugin.utils;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.DisposeAwareRunnable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ToolkitUtil {
    public static void runWhenInitialized(final Project project, final Runnable r) {

        if (project.isDisposed()) {
            return;
        }

        if (isNoBackgroundMode()) {
            r.run();
            return;
        }

//        if (!project.isInitialized()) {
//            StartupManager.getInstance(project).runAfterOpened(DisposeAwareRunnable.create(r, project));
//            return;
//        }
        invokeLater(project, r);
    }


    public static void runWhenProjectIsReady(final Project project, final Runnable runnable) {
        DumbService.getInstance(project).smartInvokeLater(runnable);
    }


    public static boolean isNoBackgroundMode() {
        return (ApplicationManager.getApplication().isUnitTestMode()
                || ApplicationManager.getApplication().isHeadlessEnvironment());
    }


    public static void runDumbAware(final Project project, final Runnable r) {
        if (DumbService.isDumbAware(r)) {
            r.run();
        } else {
            DumbService.getInstance(project).runWhenSmart(DisposeAwareRunnable.create(r, project));
        }
    }


    public static void invokeLater(Runnable r) {
        ApplicationManager.getApplication().invokeLater(r);
    }

    public static void invokeLater(Project p, Runnable r) {
        invokeLater(p, ModalityState.defaultModalityState(), r);
    }

    public static void invokeLater(final Project p, final ModalityState state, final Runnable r) {
        if (isNoBackgroundMode()) {
            r.run();
        } else {
            ToolWindowManager.getInstance(p).invokeLater(r);
//            ApplicationManager.getApplication().invokeLater(DisposeAwareRunnable.create(r, p), state);
        }
    }


    public static String formatHtmlImage(URL url) {
        return "<img src=\"" + url + "\"> ";
    }


    public static PsiClass findPsiClass(final String qualifiedName, final Module module, final Project project) {
        final GlobalSearchScope scope = module == null ? GlobalSearchScope.projectScope(project) : GlobalSearchScope.moduleWithDependenciesScope(module);
        return JavaPsiFacade.getInstance(project).findClass(qualifiedName, scope);
    }

    public static PsiPackage getContainingPackage(@NotNull PsiClass psiClass) {
        PsiDirectory directory = psiClass.getContainingFile().getContainingDirectory();
        return directory == null ? null : JavaDirectoryService.getInstance().getPackage(directory);
    }

    public static void runWriteAction(@NotNull Runnable action) {
        ApplicationManager.getApplication().runWriteAction(action);
    }


    @NotNull
    public static String removeRedundancyMarkup(String pattern) {
        String localhostRegex = "(http(s?)://)?(localhost)(:\\d+)?";
        String hostAndPortRegex = "(http(s?)://)?" +
                "( " +
                "([a-zA-Z0-9]([a-zA-Z0-9\\\\-]{0,61}[a-zA-Z0-9])?\\\\.)+[a-zA-Z]{2,6} |" +  // domain
                "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)" + // ip address
                ")";

        String localhost = "localhost";
        if (pattern.contains(localhost)) {
            pattern = pattern.replaceFirst(localhostRegex, "");
        }
        // quick test if reg exp should be used
        if (pattern.contains("http:") || pattern.contains("https:")) {
            pattern = pattern.replaceFirst(hostAndPortRegex, "");
        }

        //TODO : resolve RequestMapping(params="method=someMethod")
        if (!pattern.contains("?")) {
            return pattern;
        }
        pattern = pattern.substring(0, pattern.indexOf("?"));
        return pattern;
    }


    @NotNull
    public static String textToRequestParam(String text) {
        StringBuilder param = new StringBuilder();

        Map<String, String> paramMap = textToParamMap(text);

        if (paramMap.size() > 0) {
            paramMap.forEach((s, o) -> {
                try {
                    param.append(s).append("=")
                        .append(URLEncoder.encode(o, "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return param.length() == 0 ? "" : param.deleteCharAt(param.length() - 1).toString();
    }


    @NotNull
    public static Map<String, String> textToParamMap(String text) {
        Map<String, String> paramMap = new HashMap<>();
        String paramText = text;
        String[] lines = paramText.split("\n");

        for (String line : lines) {
            if (!line.startsWith("//") && line.contains(":")) {

                String[] prop = line.split(":");

                if (prop.length > 1) {
                    String key = prop[0].trim();
                    String value = prop[1].trim();
                    paramMap.put(key, value);
                }
            }
        }
        return paramMap;
    }


    @NotNull
    public static Map<String, String> textToHeaderMap(String text) {
        Map<String, String> paramMap = new HashMap<>();
        String paramText = text;
        String[] lines = paramText.split("\n");

        for (String line : lines) {
            if (!line.startsWith("//") && line.contains(":")) {

                String[] prop = line.split(":");

                if (prop.length > 1) {
                    String key = prop[0].trim();
                    String value = prop[1].trim();
                    paramMap.put(key, value);
                }
            }
        }
        return paramMap;
    }

}
