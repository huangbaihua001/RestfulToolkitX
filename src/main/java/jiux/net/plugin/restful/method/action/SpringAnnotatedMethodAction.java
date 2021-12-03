package jiux.net.plugin.restful.method.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.*;
import jiux.net.plugin.restful.action.AbstractBaseAction;
import jiux.net.plugin.restful.annotations.SpringControllerAnnotation;
import jiux.net.plugin.restful.annotations.SpringRequestMethodAnnotation;
import org.jetbrains.kotlin.asJava.LightClassUtilsKt;
import org.jetbrains.kotlin.asJava.classes.KtLightClass;
import org.jetbrains.kotlin.psi.KtClassOrObject;
import org.jetbrains.kotlin.psi.KtNamedFunction;

import java.util.Arrays;
import java.util.List;

/**
 *  Restful method (restful method add method)
 */
public abstract class SpringAnnotatedMethodAction extends AbstractBaseAction {

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
            // rest method or annotated with RestController
            visible = (isRestController(psiMethod.getContainingClass()) || isRestfulMethod(psiMethod));
        }
        if (psiElement instanceof KtNamedFunction) {
            KtNamedFunction ktNamedFunction = (KtNamedFunction) psiElement;
            PsiElement parentPsi = psiElement.getParent().getParent();
            if (parentPsi instanceof KtClassOrObject) {
                KtLightClass ktLightClass = LightClassUtilsKt.toLightClass(((KtClassOrObject) parentPsi));
                List<PsiMethod> psiMethods = LightClassUtilsKt.toLightMethods(ktNamedFunction);
                assert ktLightClass != null;
                visible = (isRestController(ktLightClass) || isRestfulMethod(psiMethods.get(0)));
            }
        }

        setActionPresentationVisible(e, visible);
    }

    //include "RestController" "Controller"
    private boolean isRestController(PsiClass containingClass) {
        PsiModifierList modifierList = containingClass.getModifierList();
        assert modifierList != null;
        return modifierList.findAnnotation(SpringControllerAnnotation.REST_CONTROLLER.getQualifiedName()) != null ||
                modifierList.findAnnotation(SpringControllerAnnotation.CONTROLLER.getQualifiedName()) != null;
    }

    private boolean isRestfulMethod(PsiMethod psiMethod) {
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            boolean match = Arrays.stream(SpringRequestMethodAnnotation.values())
                    .map(SpringRequestMethodAnnotation::getQualifiedName)
                    .anyMatch(name -> name.equals(annotation.getQualifiedName()));
            if (match) {
                return true;
            }
        }
        return false;
    }
}
