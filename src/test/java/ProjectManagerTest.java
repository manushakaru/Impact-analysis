import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import model.MethodEntity;
import org.junit.Test;
import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProjectManagerTest extends BasePlatformTestCase {

    private Project project;
    private ProjectManager projectManager;

    public void setUp() throws Exception {
        super.setUp();
        projectManager = new ProjectManager();
        project = getProject();
        myFixture.copyDirectoryToProject("src/main",getTestDataPath());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/TestProject";
    }


    public void testGetClassEntityList() {
        List<MethodEntity> methodList = new ArrayList<>();
        List<MethodEntity> returnedMethodList = projectManager.getMethodEntityList(project);
        assertEquals(methodList, returnedMethodList);
    }
    
    public void testAnalyzeClasses() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = projectManager.getClass();
        Class[] args = new Class[2];
        args[0] = List.class;
        // args[1] = PsiClass[].class;
        args[1] = Project.class;
        Method method = c.getDeclaredMethod("analyzeClasses", args);
        method.setAccessible(true);
        List<MethodEntity> testMethodList = new ArrayList<>();
        // PsiClass[] testPsiClasses = null;
        List<MethodEntity> methodList = new ArrayList<>();
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "A.java", GlobalSearchScope.projectScope(project));
        if(psiFiles.length == 1) {
            PsiClass psiJavaClass = ((PsiJavaFile) psiFiles[0]).getClasses()[0];
            // testPsiClasses = ((PsiJavaFile) psiFiles[0]).getClasses();
            PsiMethod [] psiMethods = psiJavaClass.getMethods();
            for(PsiMethod psiMethod: psiMethods){
                methodList.add(new MethodEntity(psiMethod));
                testMethodList.add(new MethodEntity(psiMethod));
            }
        }
        method.invoke(projectManager, testMethodList, project);
        assertEquals(testMethodList, methodList);


    }

    public void testExecute() {
    }

    @Test
    public void testExecuteCallee() {
    }

    public void testGetElement() {
    }

}