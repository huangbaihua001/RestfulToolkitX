package jiux.net.plugin.restful.annotations;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

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

  private static final Map<String, SpringRequestMethodAnnotation> QUALIFIED_MAP = new HashMap<>();

  static {
    for (SpringRequestMethodAnnotation annotation : SpringRequestMethodAnnotation.values()) {
      QUALIFIED_MAP.put(annotation.getQualifiedName(), annotation);
    }
  }

  private final String qualifiedName;
  private final String methodName;

  SpringRequestMethodAnnotation(String qualifiedName, String methodName) {
    this.qualifiedName = qualifiedName;
    this.methodName = methodName;
  }

  public static SpringRequestMethodAnnotation getByQualifiedName(String qualifiedName) {
    if (StringUtils.isEmpty(qualifiedName)) {
      return null;
    }

    return QUALIFIED_MAP.get(qualifiedName);
  }

  public static boolean isRequestMapping(String qualifiedName) {
    return getByQualifiedName(qualifiedName) != null;
  }

  public static boolean isNotRequestMapping(String qualifiedName) {
    return !isRequestMapping(qualifiedName);
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
