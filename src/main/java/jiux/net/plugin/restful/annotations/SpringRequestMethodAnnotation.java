package jiux.net.plugin.restful.annotations;

public enum SpringRequestMethodAnnotation {
  /**
   * RequestMapping
   */
  REQUEST_MAPPING("org.springframework.web.bind.annotation.RequestMapping", null),
  /**
   * GetMapping
   */
  GET_MAPPING("org.springframework.web.bind.annotation.GetMapping", "GET"),
  /**
   * PostMapping
   */
  POST_MAPPING("org.springframework.web.bind.annotation.PostMapping", "POST"),
  /**
   * PutMapping
   */
  PUT_MAPPING("org.springframework.web.bind.annotation.PutMapping", "PUT"),
  /**
   * DeleteMapping
   */
  DELETE_MAPPING("org.springframework.web.bind.annotation.DeleteMapping", "DELETE"),
  /**
   * PatchMapping
   */
  PATCH_MAPPING("org.springframework.web.bind.annotation.PatchMapping", "PATCH");

  private final String qualifiedName;
  private final String methodName;

  SpringRequestMethodAnnotation(String qualifiedName, String methodName) {
    this.qualifiedName = qualifiedName;
    this.methodName = methodName;
  }

  public static SpringRequestMethodAnnotation getByQualifiedName(String qualifiedName) {
    for (SpringRequestMethodAnnotation springRequestAnnotation : SpringRequestMethodAnnotation.values()) {
      if (springRequestAnnotation.getQualifiedName().equals(qualifiedName)) {
        return springRequestAnnotation;
      }
    }
    return null;
  }

  public static SpringRequestMethodAnnotation getByShortName(String requestMapping) {
    for (SpringRequestMethodAnnotation springRequestAnnotation : SpringRequestMethodAnnotation.values()) {
      if (springRequestAnnotation.getQualifiedName().endsWith(requestMapping)) {
        return springRequestAnnotation;
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
    return qualifiedName.substring(qualifiedName.lastIndexOf(".") - 1);
  }
}
