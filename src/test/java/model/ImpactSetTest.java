package model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.Query;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ImpactSetTest extends BasePlatformTestCase {

    private ReferenceEntity referenceEntity;
    private int depth = 1;
    private PsiMethod psiMethod;
    private PsiMethod testPsiMethod;
    private PsiClass testPsiClass;
    private PsiReference psiReference;
    private PsiClass psiJavaClass;
    private PsiReference testPsiReference;
    private ArrayList<ReferenceEntity> psiReferenceArrayList;
    private ArrayList<String> referencesStringList;
    private String testDisplayString;
    private ImpactSet impactSet;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Project project = getProject();
        psiReferenceArrayList = new ArrayList<ReferenceEntity>();
        referencesStringList = new ArrayList<String>();
        impactSet = new ImpactSet();
        myFixture.copyDirectoryToProject("src/main",getTestDataPath());
        PsiManager psiManager = getPsiManager();
        String projectName = project.getName();
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "A.java", GlobalSearchScope.projectScope(project));
        int length = psiFiles.length;
        if(length > 0) {
            psiJavaClass = ((PsiJavaFile) psiFiles[0]).getClasses()[0];
            psiMethod = (PsiMethod) psiJavaClass.findMethodsByName("getName")[0];
            Query<PsiReference> queryPsiReference = MethodReferencesSearch.search(psiMethod,GlobalSearchScope.projectScope(project),false);
            for (PsiReference psiReference : queryPsiReference){
                ReferenceEntity referenceEntity = new ReferenceEntity(psiReference,depth);
                psiReferenceArrayList.add(referenceEntity);
                referencesStringList.add(referenceEntity.getDisplayString());
            }
        }
    }

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/testData/GitVcsTest";
    }

    @Test
    public void testAddReference() {
        assertEquals(0,impactSet.getReferences().size());
        assertEquals(0,impactSet.getReferencesString().size());
        impactSet.addReference(psiReferenceArrayList.get(0));
        assertEquals(1,impactSet.getReferences().size());
        assertEquals(1,impactSet.getReferencesString().size());
    }

    @Test
    public void testGetReference() {
        impactSet.addReference(psiReferenceArrayList.get(0));
        assertEquals(psiReferenceArrayList.get(0), impactSet.getReference(0));
    }

    @Test
    public void testGetReferenceString() {
        impactSet.addReference(psiReferenceArrayList.get(0));
        assertEquals(referencesStringList.get(0), impactSet.getReferenceString(0));
    }

    @Test
    public void testGetReferencesStringList() {
        for(ReferenceEntity item : psiReferenceArrayList) {
            impactSet.addReference(item);
        }
        assertEquals(referencesStringList,impactSet.getReferencesString());
    }

    @Test
    public void testGetReferencesList() {
        for(ReferenceEntity item : psiReferenceArrayList) {
            impactSet.addReference(item);
        }
        assertEquals(psiReferenceArrayList,impactSet.getReferences());
    }

    @Test
    public void testContains() {
        for(ReferenceEntity item : psiReferenceArrayList) {
            impactSet.addReference(item);
        }
        assertTrue(impactSet.contains(psiReferenceArrayList.get(0)));
    }

    @Test
    public void testNavigate() {
    }
}