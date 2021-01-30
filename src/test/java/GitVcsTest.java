import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import org.junit.Test;

import static org.junit.Assert.*;

public class GitVcsTest extends JavaCodeInsightTestCase {

    private String javaFile = "public class Foo extends Bar {\n" +
            "    public Foo(int a, int b) {\n" +
            "        super(a, b);\n" +
            "    }\n" +
            "    \n" +
            "    public void greeting() {\n" +
            "        System.out.println(\"Hello\");\n" +
            "    }\n" +
            "}";

    @Test
    public void testCompare() {
         String mrthodBefore = "public void test(String str) {\n" +
                "        System.out.println(\"Hello world\"+str);\n" +
                "}";
         String methodAfter = "public void test(int str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         PsiElementFactory psiElementFactory = myJavaFacade.getElementFactory();
         PsiClass psiJavaClass = psiElementFactory.createClassFromText(javaFile, null);
         PsiMethod psiMethodBefore = psiElementFactory.createMethodFromText(mrthodBefore, null);
         PsiMethod psiMethodAfter = psiElementFactory.createMethodFromText(methodAfter, null);
         boolean result = GitVcs.compare(psiMethodBefore, psiMethodAfter);
         assertEquals(false, result);

    }

    @Test
    public void getCurrentBranch() {
    }

    @Test
    public void getAffectedFiles() {
    }

}