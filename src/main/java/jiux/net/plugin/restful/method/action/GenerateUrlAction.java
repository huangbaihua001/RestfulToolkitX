package jiux.net.plugin.restful.method.action;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_ELEMENT;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.ui.JBColor;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Objects;
import jiux.net.plugin.restful.action.AbstractBaseAction;
import jiux.net.plugin.restful.annotations.JaxrsHttpMethodAnnotation;
import jiux.net.plugin.restful.annotations.JaxrsRequestAnnotation;
import jiux.net.plugin.restful.annotations.SpringControllerAnnotation;
import jiux.net.plugin.restful.annotations.SpringRequestMethodAnnotation;
import jiux.net.plugin.restful.common.PsiMethodHelper;

/**
 * Generate and copy restful url
 * TODO: Not considering the RequestMapping multiple values case
 */
public class GenerateUrlAction
  /*extends RestfulMethodSpringSupportedAction*/extends AbstractBaseAction {

  Editor myEditor;

  @Override
  public void actionPerformed(AnActionEvent e) {
    myEditor = e.getData(CommonDataKeys.EDITOR);
    PsiElement psiElement = e.getData(PSI_ELEMENT);
    PsiMethod psiMethod = (PsiMethod) psiElement;

    //TODO: Need to improve jaxrs support
    String servicePath;
    if (isJaxrsRestMethod(psiMethod)) {
      servicePath = PsiMethodHelper.create(psiMethod).buildServiceUriPath();
    } else {
      servicePath = PsiMethodHelper.create(psiMethod).buildServiceUriPathWithParams();
    }

    CopyPasteManager.getInstance().setContents(new StringSelection(servicePath));
    showPopupBalloon("copied servicePath:\n " + servicePath);
  }

  private boolean isJaxrsRestMethod(PsiMethod psiMethod) {
    PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();

    for (PsiAnnotation annotation : annotations) {
      boolean match = Arrays
        .stream(JaxrsHttpMethodAnnotation.values())
        .map(JaxrsHttpMethodAnnotation::getQualifiedName)
        .anyMatch(name -> name.equals(annotation.getQualifiedName()));
      if (match) {
        return true;
      }
    }

    return false;
  }

  /**
   * spring rest method is selected before triggering
   *
   * @param e
   */
  @Override
  public void update(AnActionEvent e) {
    PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);

    boolean visible = false;

    if (psiElement instanceof PsiMethod) {
      PsiMethod psiMethod = (PsiMethod) psiElement;
      // Rest method or annotated with RestController
      visible =
        (isRestController(Objects.requireNonNull(psiMethod.getContainingClass())) ||
          isRestfulMethod(psiMethod));
    }

    setActionPresentationVisible(e, visible);
  }

  //include "RestController" "Controller"
  private boolean isRestController(PsiClass containingClass) {
    PsiModifierList modifierList = containingClass.getModifierList();

    /*return modifierList.findAnnotation(SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName()) != null ||
                modifierList.findAnnotation(SpringControllerAnnotation.CONTROLLER.getQualifiedName()) != null ;*/

    assert modifierList != null;
    return (
      modifierList.findAnnotation(
          SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName()
        ) !=
        null ||
      modifierList.findAnnotation(
          SpringControllerAnnotation.CONTROLLER.getQualifiedName()
        ) !=
        null ||
      modifierList.findAnnotation(
          SpringControllerAnnotation.FEIGN_CLIENT.getQualifiedName()
        ) !=
        null ||
      modifierList.findAnnotation(JaxrsRequestAnnotation.PATH.getQualifiedName()) != null
    );
  }

  private boolean isRestfulMethod(PsiMethod psiMethod) {
    PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();

    for (PsiAnnotation annotation : annotations) {
      boolean match = Arrays
        .stream(SpringRequestMethodAnnotation.values())
        .map(SpringRequestMethodAnnotation::getQualifiedName)
        .anyMatch(name -> name.equals(annotation.getQualifiedName()));
      if (match) {
        return match;
      }
    }

    for (PsiAnnotation annotation : annotations) {
      boolean match = Arrays
        .stream(JaxrsHttpMethodAnnotation.values())
        .map(JaxrsHttpMethodAnnotation::getQualifiedName)
        .anyMatch(name -> name.equals(annotation.getQualifiedName()));
      if (match) {
        return true;
      }
    }

    return false;
  }

  private void showPopupBalloon(final String result) {
    ApplicationManager
      .getApplication()
      .invokeLater(
        new Runnable() {
          @Override
          public void run() {
            JBPopupFactory factory = JBPopupFactory.getInstance();
            factory
              .createHtmlTextBalloonBuilder(
                result,
                null,
                new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)),
                null
              )
              .setFadeoutTime(5000)
              .createBalloon()
              .show(factory.guessBestPopupLocation(myEditor), Balloon.Position.above);
          }
        }
      );
  }
}
