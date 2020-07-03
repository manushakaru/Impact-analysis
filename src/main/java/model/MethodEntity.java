package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.ArrayList;

public class MethodEntity {
    private String name;
    /*private ArrayList<PsiReference> psiReferences;
    private ArrayList<String> psiReferencesString;*/
    private ImpactSet impactSet;

    public MethodEntity(String name) {
        this.name = name;
        //psiReferences=new ArrayList<PsiReference>();
        //psiReferencesString=new ArrayList<String>();
    }

    @Override
    public String toString() {
        return this.name;
    }

    /*public PsiReference getPsiReference(int i) {
        return psiReferences.get(i);
    }

    public void addPsiReference(PsiReference psiReference) {
        psiReferences.add(psiReference);
    }

    public void addPsiReferenceString(String psiReference) {
        psiReferencesString.add(psiReference);
    }

    public String getPsiReferenceString(int i) {
        return psiReferencesString.get(i);
    }

    public ArrayList<String> getPsiReferencesString() {
        return psiReferencesString;
    }*/

    public void setImpactSet(ImpactSet impactSet) {
        this.impactSet = impactSet;
    }

    public ImpactSet getImpactSet() {
        return impactSet;
    }

    /*public void navigate(int i) {
        PsiElement navigationElement = psiReferences.get(i).getElement().getNavigationElement();
        if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate())
        {
            ((Navigatable) navigationElement).navigate(true);
        }
    }*/
}
