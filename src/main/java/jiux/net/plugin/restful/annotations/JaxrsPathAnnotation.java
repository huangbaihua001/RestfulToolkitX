package jiux.net.plugin.restful.annotations;


public enum JaxrsPathAnnotation implements PathMappingAnnotation {

    PATH("Path", "javax.ws.rs.Path");

    private String shortName;
    private String qualifiedName;

    JaxrsPathAnnotation(String shortName, String qualifiedName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getShortName() {
        return shortName;
    }
/*
    @Override
    public List<PathMappingAnnotation> getPathMappings() {
        return allPathMappingAnnotations;
    }

    static {
        for (JaxrsPathAnnotation annotation : JaxrsPathAnnotation.values()) {
            allPathMappingAnnotations.add(annotation);
        }
    }*/

}