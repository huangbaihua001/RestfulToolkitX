package jiux.net.plugin.restful.method;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
  GET,
  POST,
  PUT,
  DELETE,
  PATCH,
  HEAD,
  OPTIONS,
  TRACE,
  CONNECT;

  private static final Map<String, HttpMethod> methodMap = new HashMap(8);

  static {
    for (HttpMethod httpMethod : values()) {
      methodMap.put(httpMethod.name(), httpMethod);
    }
  }

  public static HttpMethod getByRequestMethod(String method) {
    if (method == null || method.isEmpty()) {
      return null;
    }

    String[] split = method.split("\\.");

    if (split.length > 1) {
      method = split[split.length - 1].toUpperCase();
      return HttpMethod.valueOf(method);
    }

    return HttpMethod.valueOf(method.toUpperCase());
  }
}
