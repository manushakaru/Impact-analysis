package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

/**
 * MethodEntity class represents a PsiMethod  and impact set related
 * to the PsiMethod
 *
 * @version 1.0
 *
 */
public class MethodEntity {
    /**
     * PsiMethod name
     *
     * @see PsiMethod#getName()
     */
    private final String name;
    /**
     * Impacted method set
     *
     * @see ImpactSet
     */
    private ImpactSet impactSet;
    /**
     * PsiMethod
     *
     * @see PsiMethod
     */
    private final PsiMethod psiMethod;

    /**
     * MethodEntity constructo, initialize the name and psiMethod
     *
     * @param psiMethod Represents a Java method or constructor.
     */
    public MethodEntity(PsiMethod psiMethod) {
        this.name = psiMethod.getName();
        this.psiMethod = psiMethod;
    }

    /**
     * Returns name
     *
     * @return name
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the impact set
     *
     * @return impactSet
     */
    public ImpactSet getImpactSet() {
        return impactSet;
    }

    /**
     * Set impact set
     *
     * @param impactSet represents ImpactSet object
     */
    public void setImpactSet(ImpactSet impactSet) {
        this.impactSet = impactSet;
    }

    /**
     * Returns psiMethod
     *
     * @return psiMethod
     */
    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    /**
     * Navigate to the position of the method in the code.
     * Get navigationElement from psiMethod and check if the
     * navigationElement is not null and can navigate.
     * If all the conditions are met, then navigate to the
     * position in the code
     */
    public void navigate() {
        try {
            PsiElement navigationElement = psiMethod.getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);
            }
        } catch (Exception e) {
        }
    }
}
