package jiux.net.plugin.restful.annotations;


public enum SpringRequestParamAnnotations {
    /**
     * org.springframework.web.bind.annotation.RequestParam
     */
    REQUEST_PARAM("RequestParam", "org.springframework.web.bind.annotation.RequestParam"),
    /**
     * org.springframework.web.bind.annotation.RequestBody
     */
    REQUEST_BODY("RequestBody", "org.springframework.web.bind.annotation.RequestBody"),
    /**
     * org.springframework.web.bind.annotation.PathVariable
     */
    PATH_VARIABLE("PathVariable", "org.springframework.web.bind.annotation.PathVariable");

    private final String shortName;
    private final String qualifiedName;

    SpringRequestParamAnnotations(String shortName, String qualifiedName) {
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