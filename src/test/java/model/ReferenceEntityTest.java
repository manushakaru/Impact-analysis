package model;

import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.LightJavaCodeInsightTestCase;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceEntityTest extends BasePlatformTestCase {

    private String javaFile = "public class Foo extends Bar {\n" +
            "    public Foo(int a, int b) {\n" +
            "        super(a, b);\n" +
            "    }\n" +
            "    \n" +
            "    public void greeting() {\n" +
            "        System.out.println(\"Hello\");\n" +
            "    }\n" +
            "}";
    private String psiMethodString = "public String getName() {\n" +
            "        return name;\n" +
            "    }";
    private ReferenceEntity referenceEntity;
    private int depth = 1;
    private PsiMethod psiMethod;
    private  PsiReference psiReference;
    private PsiClass psiJavaClass;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        PsiElementFactory psiElementFactory = myJavaFacade.getElementFactory();
//        psiJavaClass = psiElementFactory.createClassFromText(javaFile, null);
//        psiMethod =  psiElementFactory.createMethodFromText(psiMethodString, psiJavaClass);
//        psiReference = psiMethod.getReference();
//        assert psiReference != null;
//        referenceEntity = new ReferenceEntity(psiReference,depth);
        Project project = getProject();
        myFixture.copyDirectoryToProject("src/main",getTestDataPath());
        PsiManager psiManager = getPsiManager();
        String projectName = project.getName();
        PsiFile [] psiFiles = FilenameIndex.getFilesByName(project, "A.java", GlobalSearchScope.projectScope(project));
        int length = psiFiles.length;
        if(length > 0) {
            psiJavaClass = ((PsiJavaFile) psiFiles[0]).getClasses()[0];
            psiMethod = (PsiMethod) psiJavaClass.findMethodsByName("getName")[0];
           // PsiMethod signature = myFixture.findElementByText(psiMethodString, PsiMethod.class);
            // psiMethod = psiJavaClass.findMethodBySignature(signature, false);
            psiReference = psiMethod.getReference();
        }
        System.out.println();


    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/testData/GitVcsTest";
    }

    public void testGetPsiMethod() {
        //assertEquals(psiMethod, referenceEntity.getPsiMethod());
    }

    @Test
    public void testGetPsiClass() {
        //assertEquals(psiJavaClass, referenceEntity.getPsiClass());
    }

    @Test
    public void testGetDisplayString() {
    }

    @Test
    public void testGetDepth() {
        //assertEquals(depth,referenceEntity.getDepth());
    }

    @Test
    public void testIsCaller() {
        //assertEquals(true, referenceEntity.isCaller());
    }

    @Test
    public void testNavigate() {
    }
}