package jiux.net.plugin.restful.annotations;


public enum JaxrsRequestAnnotation {

    PATH("Path", "javax.ws.rs.Path", null);

    private String shortName;
    private String qualifiedName;
    private String methodName;

    JaxrsRequestAnnotation(String shortName, String qualifiedName, String methodName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
        this.methodName = methodName;
    }

    public static JaxrsRequestAnnotation getByShortName(String shortName) {
        for (JaxrsRequestAnnotation requestAnnotation : JaxrsRequestAnnotation.values()) {
            if (requestAnnotation.getShortName().equals(shortName)) {
                return requestAnnotation;
            }
        }
        return null;
    }

    public static JaxrsRequestAnnotation getByQualifiedName(String qualifiedName) {
        for (JaxrsRequestAnnotation requestAnnotation : JaxrsRequestAnnotation.values()) {
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