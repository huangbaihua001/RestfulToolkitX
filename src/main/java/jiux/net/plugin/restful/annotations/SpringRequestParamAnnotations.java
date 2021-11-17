package jiux.net.plugin.restful.annotations;


public enum SpringRequestParamAnnotations {
    REQUEST_PARAM("RequestParam", "org.springframework.web.bind.annotation.RequestParam"),
    REQUEST_BODY("RequestBody", "org.springframework.web.bind.annotation.RequestBody"),
    PATH_VARIABLE("PathVariable", "org.springframework.web.bind.annotation.PathVariable");

    private String shortName;
    private String qualifiedName;

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