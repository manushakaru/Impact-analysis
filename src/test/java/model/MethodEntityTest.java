package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MethodEntityTest {
    private MethodEntity methEnt =  new MethodEntity("test");
    @Test
    public void testToString() {
        assertEquals("test", methEnt.toString());
    }

    @Test
    public void setImpactSet() {
        ImpactSet impSet = new ImpactSet();
        methEnt.setImpactSet(impSet);
        assertEquals(impSet,methEnt.getImpactSet());
    }

    @Test
    public void getImpactSet() {
        assertNull(methEnt.getImpactSet());
    }
}