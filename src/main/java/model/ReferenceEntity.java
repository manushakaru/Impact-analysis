package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

public class ReferenceEntity {

    private PsiReference psiReference;
    private PsiMethod psiMethod;
    private PsiClass psiClass;
    private String displayString;
    private int depth;
    private boolean isCaller = false;

    public ReferenceEntity(PsiReference psiReference,int depth) {
        this.psiReference = psiReference;
        psiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
        psiClass=psiMethod.getContainingClass();
        this.depth=depth;
        this.isCaller=true;
        displayString=psiClass.getContainingFile().getContainingDirectory().toString()+"->"+psiClass.getName()+"->"+psiMethod.getSignature(PsiSubstitutor.EMPTY).toString();
        System.out.println(displayString);
    }

    public ReferenceEntity(PsiMethod psiMethod,int depth) {
        this.psiReference = psiMethod.getReference();
        this.psiMethod = psiMethod;
        psiClass=psiMethod.getContainingClass();
        this.depth=depth;
        displayString=psiClass.getContainingFile().getContainingDirectory().toString()+"->"+psiClass.getName()+"->"+psiMethod.getSignature(PsiSubstitutor.EMPTY).toString();
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

    public int getDepth() {
        return depth;
    }

    public boolean isCaller() {
        return isCaller;
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
