package model;

import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;


public class ClassEntityTest extends JavaCodeInsightTestCase {

    private MethodEntity methodEntity;
    private String javaFile = "public class Foo {\n" +
            "}";
    private String psiMethodString = "public void greeting() {\n" +
            "        System.out.println(\"Hello\");\n" +
            "    }";
    private PsiMethod psiMethod;
    private ClassEntity classEntity;
    private String testClassName = "TestClass";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PsiElementFactory psiElementFactory = myJavaFacade.getElementFactory();
        PsiClass psiJavaClass = psiElementFactory.createClassFromText(javaFile, null);
        psiMethod =  psiElementFactory.createMethodFromText(psiMethodString, psiJavaClass);
        methodEntity = new MethodEntity(psiMethod);
        classEntity = new ClassEntity(testClassName);
    }

    public void testTestGetName() {
        assertEquals(testClassName, classEntity.getName());
    }

    public void testTestSetName() {
        String newName = "NewName";
        classEntity.setName(newName);
        assertEquals(newName, classEntity.getName());

    }

    public void testAddMethod() {
        assertEquals(0, classEntity.getMethodList().size());
        classEntity.addMethod(methodEntity);
        assertEquals(1, classEntity.getMethodList().size());
    }

    public void testGetMethodList() {
         List<MethodEntity> mMethodList = new ArrayList<MethodEntity>();
         assertEquals(mMethodList, classEntity.getMethodList());
         mMethodList.add(methodEntity);
         classEntity.addMethod(methodEntity);
         assertEquals(mMethodList, classEntity.getMethodList());

    }
}