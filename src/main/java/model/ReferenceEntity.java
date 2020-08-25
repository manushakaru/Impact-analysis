package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;

public class ReferenceEntity {

    private PsiReference psiReference;
    private PsiMethod psiMethod;
    private PsiClass psiClass;
    private String displayString;

    public ReferenceEntity(PsiReference psiReference,int depth) {
        this.psiReference = psiReference;
        psiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
        psiClass=psiMethod.getContainingClass();
        displayString=psiClass.getName()+"->"+psiMethod.getName()+" ("+depth+") ^";
    }

    public ReferenceEntity(PsiMethod psiMethod,int depth) {
        this.psiReference = psiMethod.getReference();
        this.psiMethod = psiMethod;
        psiClass=psiMethod.getContainingClass();
        displayString=psiClass.getName()+"->"+psiMethod.getName()+" ("+depth+")";
    }

    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void navigate() {
        try{
            PsiElement navigationElement = psiReference.getElement().getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);
            }
        }catch (Exception e){
            PsiElement navigationElement = psiMethod.getNavigationElement();
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
                ((Navigatable) navigationElement).navigate(true);
            }
        }
    }
}
