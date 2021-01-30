package model;

import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;



public class ReferenceEntityTest extends BasePlatformTestCase {

    private ReferenceEntity referenceEntity;
    private int depth = 1;
    private PsiMethod psiMethod;
    private PsiMethod testPsiMethod;
    private PsiClass psiJavaClass;
    private String testDisplayString;
    private PsiClass testPsiClass;

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
            Query<PsiReference> queryPsiReference = MethodReferencesSearch.search(psiMethod,GlobalSearchScope.projectScope(project),false);
            for (PsiReference psiReference : queryPsiReference){
                testPsiMethod = PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
                testPsiClass = testPsiMethod.getContainingClass();
                testDisplayString = testPsiClass
                        .getContainingFile()
                        .getContainingDirectory()
                        .toString()+"->"+testPsiClass.getName()+"->"+testPsiMethod
                        .getSignature(PsiSubstitutor.EMPTY)
                        .toString();
                referenceEntity = new ReferenceEntity(psiReference,depth);
                break;
            }
        }
    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/testData/TestProject";
    }

    public void testGetPsiMethod() {
        assertEquals(testPsiMethod, referenceEntity.getPsiMethod());
    }

    @Test
    public void testGetPsiClass() {
        assertEquals(testPsiClass, referenceEntity.getPsiClass());
    }

    @Test
    public void testGetDisplayString() {
        assertEquals(testDisplayString, referenceEntity.getDisplayString());
    }

    @Test
    public void testGetDepth() {
        assertEquals(depth,referenceEntity.getDepth());
    }

    @Test
    public void testIsCaller() {
        assertTrue(referenceEntity.isCaller());
    }

}