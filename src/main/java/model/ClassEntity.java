package model;

import java.util.ArrayList;
import java.util.List;

public class ClassEntity {
    private String mName;
    private List<MethodEntity> mMethodList = new ArrayList<MethodEntity>();

    public ClassEntity(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void addMethod(MethodEntity methodName) {
        mMethodList.add(methodName);
    }

    public List<MethodEntity> getMethodList() {
        return mMethodList;
    }
}
