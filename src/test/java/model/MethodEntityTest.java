package model;

import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;


public class MethodEntityTest extends JavaCodeInsightTestCase {

    private MethodEntity methodEntity;
    private PsiFile psiFile;
    private String javaFile = "public class Foo extends Bar {\n" +
            "    public Foo(int a, int b) {\n" +
            "        super(a, b);\n" +
            "    }\n" +
            "    \n" +
            "    public void greeting() {\n" +
            "        System.out.println(\"Hello\");\n" +
            "    }\n" +
            "}";
    private String psiMethodString = "public void greeting() {\n" +
            "        System.out.println(\"Hello\");\n" +
            "    }";
    private PsiMethod psiMethod;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PsiElementFactory psiElementFactory = myJavaFacade.getElementFactory();
        PsiClass psiJavaClass = psiElementFactory.createClassFromText(javaFile, null);
        psiMethod =  psiElementFactory.createMethodFromText(psiMethodString, psiJavaClass);
        methodEntity = new MethodEntity(psiMethod);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    public void testToString() {
        assertEquals("greeting", methodEntity.toString());
    }

    public void testSetImpactSet() {

    }


    public void testGetImpactSet() {
    }


    public void testGetPsiMethod() {
        assertEquals(psiMethod, methodEntity.getPsiMethod());
    }


    public void testNavigate() {
    }
}