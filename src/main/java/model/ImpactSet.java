package model;

import java.util.ArrayList;

/**
 * The ImpactSet class manages list of impacted method references
 * and related display strings which are going to display under
 * impacted set of the plugin
 *
 * @version 1.0
 *
 */
public class ImpactSet {

    /**
     * List of reference entities
     */
    private final ArrayList<ReferenceEntity> referenceEntities;

    /**
     * List of display strings which display in the plugin
     * interface
     */
    private final ArrayList<String> displayStrings;

    /**
     * ImpactSet constructor
     */
    public ImpactSet() {
        referenceEntities = new ArrayList<>();
        displayStrings = new ArrayList<>();
    }

    /**
     * Add reference entity to reference entities list
     * and get display string and add into displayStrings array
     *
     * @param referenceEntity referenceEntity object
     */
    public void addReference(ReferenceEntity referenceEntity) {
        referenceEntities.add(referenceEntity);
        displayStrings.add(referenceEntity.getDisplayString());
    }

    /**
     * Get reference entity by array index
     *
     * @param index index of the item
     * @return ReferenceEntity
     */
    public ReferenceEntity getReferenceEntity(int index) {
        return referenceEntities.get(index);
    }

    /**
     * Get display string by array index
     *
     * @param index index of the item
     * @return String
     */
    public String getDisplayString(int index) {
        return displayStrings.get(index);
    }

    /**
     * Get all display strings
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getDisplayStrings() {
        return displayStrings;
    }

    /**
     * Get all reference entities
     *
     * @return ArrayList<ReferenceEntity>
     */
    public ArrayList<ReferenceEntity> getReferenceEntities() {
        return referenceEntities;
    }

    /**
     * Check whether display string contains the reference entity's
     * display string
     *
     * @param referenceEntity referenceEntity object
     * @return {@code true} if display string contains the reference
     * entity's display string
     */
    public boolean contains(ReferenceEntity referenceEntity) {
        //to check signature
        return displayStrings.contains(referenceEntity.getDisplayString());
    }

    /**
     * Navigate to the relative code line using array index.
     * Cursor will positioned to the code line of the reference method
     *
     * @param index index of the item
     */
    public void navigate(int index) {
        referenceEntities.get(index).navigate();
    }
}
