package model;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.ArrayList;

public class ImpactSet {
    private ArrayList<ReferenceEntity> references;
    private ArrayList<String> referencesString;

    public ImpactSet() {
        references=new ArrayList<ReferenceEntity>();
        referencesString=new ArrayList<String>();
    }

    public void addReference(ReferenceEntity reference) {
        references.add(reference);
        referencesString.add(reference.getDisplayString());
    }

    public ReferenceEntity getReference(int i) {
        return references.get(i);
    }

    public String getReferenceString(int i) {
        return referencesString.get(i);
    }

    public ArrayList<String> getReferencesString() {
        return referencesString;
    }

    public ArrayList<ReferenceEntity> getReferences() { return references; }

    public boolean contains(ReferenceEntity referenceEntity){
        return referencesString.contains(referenceEntity.getDisplayString());
    }

    public void navigate(int i) {
        references.get(i).navigate();
    }
}
