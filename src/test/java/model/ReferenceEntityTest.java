package model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

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
    private PsiMethod testPsiMethod;
    private PsiClass testPsiClass;
    private PsiReference psiReference;
    private PsiClass psiJavaClass;
    private PsiReference testPsiReference;
    private String testDisplayString;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = getProject();
        myFixture.copyDirectoryToProject("src/main",getTestDataPath());
        PsiManager psiManager = getPsiManager();
        String projectName = project.getName();
        PsiFile [] psiFiles = FilenameIndex.getFilesByName(project, "A.java", GlobalSearchScope.projectScope(project));
        int length = psiFiles.length;
        if(length > 0) {
            psiJavaClass = ((PsiJavaFile) psiFiles[0]).getClasses()[0];
            psiMethod = (PsiMethod) psiJavaClass.findMethodsByName("getName")[0];
            for (PsiReference psiReference : MethodReferencesSearch.search(psiMethod,GlobalSearchScope.projectScope(project),false)){
                if(psiReference != null){
                    testPsiReference = psiReference;
                    testPsiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
                    testPsiClass = testPsiMethod.getContainingClass();
                    referenceEntity = new ReferenceEntity(testPsiReference, depth);
                    testDisplayString = testPsiClass
                            .getContainingFile()
                            .getContainingDirectory()
                            .toString()+"->"+psiJavaClass.getName()+"->"+testPsiMethod
                            .getSignature(PsiSubstitutor.EMPTY)
                            .toString();
                    break;
                }
            }
        }
    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/testData/GitVcsTest";
    }

    public void testGetPsiMethod() {
        assertEquals(testPsiMethod, referenceEntity.getPsiMethod());
    }


    public void testGetPsiClass() {
        assertEquals(testPsiClass, referenceEntity.getPsiClass());
    }


    public void testGetDisplayString() {
        assertEquals(testDisplayString, referenceEntity.getDisplayString());
    }


    public void testGetDepth() {
        assertEquals(depth,referenceEntity.getDepth());
    }


    public void testIsCaller() {
        assertTrue(referenceEntity.isCaller());
    }


    public void testNavigate() {
    }
}