package jiux.net.plugin.restful.annotations;

public enum JakartaHttpMethodAnnotation {
  /**
   * jakarta.ws.rs.GET
   */
  GET("jakarta.ws.rs.GET", "GET"),
  /**
   * jakarta.ws.rs.POST
   */
  POST("jakarta.ws.rs.POST", "POST"),
  /**
   * jakarta.ws.rs.PUT
   */
  PUT("jakarta.ws.rs.PUT", "PUT"),
  /**
   * jakarta.ws.rs.DELETE
   */
  DELETE("jakarta.ws.rs.DELETE", "DELETE"),
  /**
   * jakarta.ws.rs.HEAD
   */
  HEAD("jakarta.ws.rs.HEAD", "HEAD"),
  /**
   * jakarta.ws.rs.PATCH
   */
  PATCH("jakarta.ws.rs.PATCH", "PATCH");

  private final String qualifiedName;
  private final String methodName;

  JakartaHttpMethodAnnotation(String qualifiedName, String methodName) {
    this.qualifiedName = qualifiedName;
    this.methodName = methodName;
  }

  public static JakartaHttpMethodAnnotation getByQualifiedName(String qualifiedName) {
    for (JakartaHttpMethodAnnotation springRequestAnnotation : JakartaHttpMethodAnnotation.values()) {
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
