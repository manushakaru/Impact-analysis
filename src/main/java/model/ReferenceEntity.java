package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Represents PsiReference
 *
 * @see PsiReference
 * @see PsiMethod
 * @see PsiClass
 *
 * @version 1.0
 *
 */
public class ReferenceEntity {

    /**
     * @see PsiReference
     */
    private final PsiReference psiReference;

    /**
     * @see PsiMethod
     */
    private final PsiMethod psiMethod;

    /**
     * Represents a Java class or interface.
     * @see PsiClass
     */
    private final PsiClass psiClass;

    /**
     * Display string which display in the plugin interface
     */
    private final String displayString;

    /**
     * Define searching depth.
     * If the depth = 1, program will take care of immediate
     * caller and immediate callee of the selected method.
     */
    private final int depth;

    /**
     * Attribute to define reference entity is a caller or callee
     */
    private boolean isCaller = false;

    /**
     * ReferenceEntity constructor using PsiReference and depth.
     * Initialize psiReference, psiMethod, psiClass, depth, displayString and
     * set isCaller to true.
     *
     * @param psiReference a reference to a PSI element
     * @param depth depth of the search
     */
    public ReferenceEntity(PsiReference psiReference, int depth) {
        this.psiReference = psiReference;
        psiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
        psiClass = psiMethod.getContainingClass();
        this.depth = depth;
        this.isCaller = true;
        displayString = psiClass.getContainingFile().getContainingDirectory().toString() + "->" + psiClass.getName() + "->" + psiMethod.getSignature(PsiSubstitutor.EMPTY).toString();
        System.out.println(displayString);
    }

    /**
     * ReferenceEntity constructor using PsiReference and depth.
     * Initialize psiReference, psiMethod, psiClass, depth, displayString.
     *
     * @param psiMethod represents a Java method or constructor.
     * @param depth depth of the search
     */
    public ReferenceEntity(PsiMethod psiMethod, int depth) {
        this.psiReference = psiMethod.getReference();
        this.psiMethod = psiMethod;
        psiClass = psiMethod.getContainingClass();
        this.depth = depth;
        displayString = psiClass.getContainingFile().getContainingDirectory().toString() + "->" + psiClass.getName() + "->" + psiMethod.getSignature(PsiSubstitutor.EMPTY).toString();
    }

    /**
     * Returns PsiMethod
     *
     * @return psiMethod
     */
    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    /**
     * Returns PsiClass of the method.
     *
     * @return psiClass
     */
    public PsiClass getPsiClass() {
        return psiClass;
    }

    /**
     * Returns display string
     *
     * @return displayString
     */
    public String getDisplayString() {
        return displayString;
    }

    /**
     * Returns searching depth
     *
     * @return depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns boolean to represent the method is a caller or callee
     *
     * @return true if method is a caller
     */
    public boolean isCaller() {
        return isCaller;
    }

    /**
     * Navigate to the position of the method in the code.
     * Get navigationElement from psiReference and check if the
     * navigationElement is not null and can navigate.
     * If all the conditions are met, then navigate to the
     * position in the code.
     * If an error thrown,
     * Get navigationElement from psiMethod and check if the
     * navigationElement is not null and can navigate.
     * If all the conditions are met, then navigate to the
     * position in the code.
     */
    public void navigate() {
        try {
            PsiElement navigationElement = psiReference.getElement().getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);

            }
        } catch (Exception e) {
            PsiElement navigationElement = psiMethod.getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);
            }
        }
    }
}
