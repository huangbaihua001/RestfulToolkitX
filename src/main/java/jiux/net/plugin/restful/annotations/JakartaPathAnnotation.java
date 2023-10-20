package jiux.net.plugin.restful.annotations;

public enum JakartaPathAnnotation implements PathMappingAnnotation {
  /**
   * Path jakarta.ws.rs.Path
   */
  PATH("Path", "jakarta.ws.rs.Path");

  private final String shortName;
  private final String qualifiedName;

  JakartaPathAnnotation(String shortName, String qualifiedName) {
    this.shortName = shortName;
    this.qualifiedName = qualifiedName;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public String getShortName() {
    return shortName;
  }
}
