package jiux.net.plugin.restful.annotations;

public enum JakartaRequestParamAnnotation {
  /**
   * QueryParam jakarta.ws.rs.QueryParam
   */
  QUERY_PARAM("QueryParam", "jakarta.ws.rs.QueryParam"),
  /**
   * PathParam jakarta.ws.rs.PathParam
   */
  PATH_PARAM("PathParam", "jakarta.ws.rs.PathParam"),

  /**
   * FormParam jakarta.ws.rs.FormParam
   */
  FORM_PARAM("FormParam", "jakarta.ws.rs.FormParam"),

  /**
   * MatrixParam jakarta.ws.rs.MatrixParam
   */
  MATRIX_PARAM("MatrixParam", "jakarta.ws.rs.MatrixParam");

  private final String shortName;
  private final String qualifiedName;

  JakartaRequestParamAnnotation(String shortName, String qualifiedName) {
    this.shortName = shortName;
    this.qualifiedName = qualifiedName;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public String getShortName() {
    return shortName;
  }
}
