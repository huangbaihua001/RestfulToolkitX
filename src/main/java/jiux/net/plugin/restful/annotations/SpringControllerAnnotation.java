package jiux.net.plugin.restful.annotations;


public enum SpringControllerAnnotation implements PathMappingAnnotation {

    CONTROLLER("Controller", "org.springframework.stereotype.Controller"),
    REST_CONTROLLER("RestController", "org.springframework.web.bind.annotation.RestController");

    private String shortName;
    private String qualifiedName;
    SpringControllerAnnotation(String shortName, String qualifiedName) {
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

/*
    @Override
    public List<PathMappingAnnotation> getPathMappings() {
        return allPathMappingAnnotations;
    }
*/

/*    static {
        for (SpringControllerAnnotation springControllerAnnotation : SpringControllerAnnotation.values()) {
            allPathMappingAnnotations.add(springControllerAnnotation);
        }
    }*/

}