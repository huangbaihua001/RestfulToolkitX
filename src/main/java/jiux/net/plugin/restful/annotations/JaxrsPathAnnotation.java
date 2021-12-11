package jiux.net.plugin.restful.annotations;


public enum JaxrsPathAnnotation implements PathMappingAnnotation {
    /**
     * Path javax.ws.rs.Path
     */
    PATH("Path", "javax.ws.rs.Path");

    private final String shortName;
    private final String qualifiedName;

    JaxrsPathAnnotation(String shortName, String qualifiedName) {
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