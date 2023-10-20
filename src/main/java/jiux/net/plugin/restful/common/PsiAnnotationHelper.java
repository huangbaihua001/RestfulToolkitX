package jiux.net.plugin.restful.common;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceExpression;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PsiAnnotationHelper {

  @NotNull
  public static List<String> getAnnotationAttributeValues(
    PsiAnnotation annotation,
    String attr
  ) {
    List<String> values = new ArrayList<>();
    if (annotation == null) {
      return values;
    }
    PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attr);

    // class com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
    // class com.intellij.psi.impl.source.tree.java.PsiArrayInitializerMemberValueImpl
    if (value instanceof PsiReferenceExpression) {
      PsiReferenceExpression expression = (PsiReferenceExpression) value;
      values.add(expression.getText());
    } else if (value instanceof PsiLiteralExpression) {
      //            values.add(psiNameValuePair.getLiteralValue());
      values.add(((PsiLiteralExpression) value).getValue().toString());
    } else if (value instanceof PsiArrayInitializerMemberValue) {
      PsiAnnotationMemberValue[] initializers =
        ((PsiArrayInitializerMemberValue) value).getInitializers();

      for (PsiAnnotationMemberValue initializer : initializers) {
        values.add(initializer.getText().replaceAll("\\\"", ""));
      }
    }

    return values;
  }

  public static String getAnnotationAttributeValue(
    PsiAnnotation annotation,
    String attr
  ) {
    List<String> values = getAnnotationAttributeValues(annotation, attr);
    if (!values.isEmpty()) {
      return values.get(0);
    }
    return null;
  }
}
