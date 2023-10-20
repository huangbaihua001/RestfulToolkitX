package jiux.net.plugin.restful.method.action;

import com.intellij.openapi.module.Module;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

//PathAndQuery  AbsolutePath AbsoluteUri Query
public class ModuleHelper {

  // URL
  private static final String SCHEME = "http://"; //PROTOCOL
  private static final String HOST = "localhost";
  private static final String PORT = "8080"; // int
  public static String DEFAULT_URI = "http://localhost" + ":" + PORT;
  Module module;
  //    private static final String PATH = "http://localhost"+":"+PORT; // PATH or FILE
  PropertiesHandler propertiesHandler;

  public ModuleHelper(Module module) {
    this.module = module;
    propertiesHandler = new PropertiesHandler(module);
  }

  public static String getAUTHORITY() {
    return null;
  }

  public static ModuleHelper create(Module module) {
    return new ModuleHelper(module);
  }

  @NotNull
  public String getServiceHostPrefix() {
    if (module == null) {
      return DEFAULT_URI;
    }

    String port = propertiesHandler.getServerPort();
    if (StringUtils.isEmpty(port)) {
      port = PORT;
    }

    String contextPath = propertiesHandler.getContextPath();
    return SCHEME + HOST + ":" + port + contextPath;
  }
}
