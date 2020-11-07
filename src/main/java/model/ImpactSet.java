package model;

import java.util.ArrayList;

public class ImpactSet {
    private final ArrayList<ReferenceEntity> references;
    private final ArrayList<String> referencesString;

    public ImpactSet() {
        references = new ArrayList<>();
        referencesString = new ArrayList<>();
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

    public ArrayList<ReferenceEntity> getReferences() {
        return references;
    }

    public boolean contains(ReferenceEntity referenceEntity) {
        //to check signature
        return referencesString.contains(referenceEntity.getDisplayString());
    }

    public void navigate(int i) {
        references.get(i).navigate();
    }
}
