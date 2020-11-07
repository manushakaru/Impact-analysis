package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;

import java.util.ArrayList;

public class MethodEntity {
    private String name;
    private ImpactSet impactSet;
    private PsiMethod psiMethod;

    public MethodEntity(PsiMethod psiMethod) {
        this.name = psiMethod.getName();
        this.psiMethod=psiMethod;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void setImpactSet(ImpactSet impactSet) {
        this.impactSet = impactSet;
    }

    public ImpactSet getImpactSet() {
        return impactSet;
    }

    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    public void navigate() {
        try{
            PsiElement navigationElement = psiMethod.getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);
            }
        }catch (Exception e){

        }
    }
}
