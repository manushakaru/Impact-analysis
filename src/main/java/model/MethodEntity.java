package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.ArrayList;

public class MethodEntity {
    private String name;
    private ImpactSet impactSet;

    public MethodEntity(String name) {
        this.name = name;
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
}
