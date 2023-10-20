package jiux.net.plugin.restful.annotations;

public enum JakartaRequestAnnotation {
  PATH("Path", "jakarta.ws.rs.Path", null);

  private final String shortName;
  private final String qualifiedName;
  private final String methodName;

  JakartaRequestAnnotation(String shortName, String qualifiedName, String methodName) {
    this.shortName = shortName;
    this.qualifiedName = qualifiedName;
    this.methodName = methodName;
  }

  public static JakartaRequestAnnotation getByShortName(String shortName) {
    for (JakartaRequestAnnotation requestAnnotation : JakartaRequestAnnotation.values()) {
      if (requestAnnotation.getShortName().equals(shortName)) {
        return requestAnnotation;
      }
    }
    return null;
  }

  public static JakartaRequestAnnotation getByQualifiedName(String qualifiedName) {
    for (JakartaRequestAnnotation requestAnnotation : JakartaRequestAnnotation.values()) {
      if (requestAnnotation.getQualifiedName().equals(qualifiedName)) {
        return requestAnnotation;
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
    return shortName;
  }
}
