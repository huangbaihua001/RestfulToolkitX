package jiux.net.plugin.restful.annotations;


public enum JaxrsRequestParamAnnotation {
    /**
     * QueryParam javax.ws.rs.QueryParam
     */
    QUERY_PARAM("QueryParam", "javax.ws.rs.QueryParam"),
    /**
     * PathParam javax.ws.rs.PathParam
     */
    PATH_PARAM("PathParam", "javax.ws.rs.PathParam");

    private final String shortName;
    private final String qualifiedName;

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
