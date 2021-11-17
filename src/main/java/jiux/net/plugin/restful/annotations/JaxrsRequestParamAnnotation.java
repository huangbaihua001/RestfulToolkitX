package jiux.net.plugin.restful.annotations;


public enum JaxrsRequestParamAnnotation {
    QUERY_PARAM("QueryParam", "javax.ws.rs.QueryParam"),
    PATH_PARAM("PathParam", "javax.ws.rs.PathParam");

    private String shortName;
    private String qualifiedName;

    JaxrsRequestParamAnnotation(String shortName, String qualifiedName) {
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
