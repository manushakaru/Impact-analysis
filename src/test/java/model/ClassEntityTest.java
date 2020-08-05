package model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClassEntityTest {
    private ClassEntity clsEnt = new ClassEntity("test");
    @Test
    public void getName() {
        assertEquals("test", clsEnt.getName());
    }

    @Test
    public void setName() {
        clsEnt.setName("test_");
        assertEquals("test_", clsEnt.getName());
    }

    @Test
    public void addMethod() {
        List<MethodEntity> testMethodList = new ArrayList<>();
        MethodEntity methodEnt = new MethodEntity("testMethod");
        testMethodList.add(methodEnt);
        clsEnt.addMethod(methodEnt);
        assertEquals(testMethodList.get(0),clsEnt.getMethodList().get(0));
    }

    @Test
    public void getMethodList() {
        assertEquals(0, clsEnt.getMethodList().size());
    }
}