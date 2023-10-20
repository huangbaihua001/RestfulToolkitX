package jiux.net.plugin.restful.annotations;

public enum JaxrsHttpMethodAnnotation {
  /**
   * javax.ws.rs.GET
   */
  GET("javax.ws.rs.GET", "GET"),
  /**
   * javax.ws.rs.POST
   */
  POST("javax.ws.rs.POST", "POST"),
  /**
   * javax.ws.rs.PUT
   */
  PUT("javax.ws.rs.PUT", "PUT"),
  /**
   * javax.ws.rs.DELETE
   */
  DELETE("javax.ws.rs.DELETE", "DELETE"),
  /**
   * javax.ws.rs.HEAD
   */
  HEAD("javax.ws.rs.HEAD", "HEAD"),
  /**
   * javax.ws.rs.PATCH
   */
  PATCH("javax.ws.rs.PATCH", "PATCH");

  private final String qualifiedName;
  private final String methodName;

  JaxrsHttpMethodAnnotation(String qualifiedName, String methodName) {
    this.qualifiedName = qualifiedName;
    this.methodName = methodName;
  }

  public static JaxrsHttpMethodAnnotation getByQualifiedName(String qualifiedName) {
    for (JaxrsHttpMethodAnnotation springRequestAnnotation : JaxrsHttpMethodAnnotation.values()) {
      if (springRequestAnnotation.getQualifiedName().equals(qualifiedName)) {
        return springRequestAnnotation;
      }
    }
    return null;
  }

  public String methodName() {
    return this.methodName;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public String getShortName() {
    return qualifiedName.substring(qualifiedName.lastIndexOf(".") - 1);
  }
}
