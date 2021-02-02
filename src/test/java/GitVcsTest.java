import com.intellij.codeInsight.JavaCodeInsightTestCase;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import org.junit.Test;

import static org.junit.Assert.*;

public class GitVcsTest extends JavaCodeInsightTestCase {

    private String javaFile = "public class Foo{\n" +
            "}";

    @Test
    public void testCompare() {
         String methodBeforeCase1 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase1 = "public void test(int str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodBeforeCase2 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase2 = "private void test(int str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodBeforeCase3 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase3 = "private int test(String str) {\n" +
                 "    System.out.println(\"Hello world\"+str);\n" +
                 "    return 1;\n" +
                 "}";
         String methodBeforeCase4 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase4 = "public void test(String str1) {\n" +
                 "        System.out.println(\"Hello world\"+str1);\n" +
                 "}";
         String methodBeforeCase5 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase5 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world !!!\"+str);\n" +
                 "}";
         String methodBeforeCase6 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase6 = "public void test(String str) {\n" +
                 "    System.out.println(\"Hello world\"+str);\n" +
                 "        \n" +
                 "}";
         String methodBeforeCase7 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase7 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\");\n" +
                 "}";
         String methodBeforeCase8 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase8 = "public void test(String str) {\n" +
                 "        System.out.print(\"Hello world\"+str);\n" +
                 "}";
         String methodBeforeCase9 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase9 = "public static void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodBeforeCase10 = "public void test(String str) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         String methodAfterCase10 = "public void test(String str,int i) {\n" +
                 "        System.out.println(\"Hello world\"+str);\n" +
                 "}";
         PsiElementFactory psiElementFactory = myJavaFacade.getElementFactory();
         PsiClass psiJavaClass = psiElementFactory.createClassFromText(javaFile, null);
         PsiMethod psiMethodBeforeCase1 = psiElementFactory.createMethodFromText(methodBeforeCase1, null);
         PsiMethod psiMethodAfterCase1 = psiElementFactory.createMethodFromText(methodAfterCase1, null);
         PsiMethod psiMethodBeforeCase2 = psiElementFactory.createMethodFromText(methodBeforeCase2, null);
         PsiMethod psiMethodAfterCase2 = psiElementFactory.createMethodFromText(methodAfterCase2, null);
         PsiMethod psiMethodBeforeCase3 = psiElementFactory.createMethodFromText(methodBeforeCase3, null);
         PsiMethod psiMethodAfterCase3 = psiElementFactory.createMethodFromText(methodAfterCase3, null);
         PsiMethod psiMethodBeforeCase4 = psiElementFactory.createMethodFromText(methodBeforeCase4, null);
         PsiMethod psiMethodAfterCase4 = psiElementFactory.createMethodFromText(methodAfterCase4, null);
         PsiMethod psiMethodBeforeCase5 = psiElementFactory.createMethodFromText(methodBeforeCase5, null);
         PsiMethod psiMethodAfterCase5 = psiElementFactory.createMethodFromText(methodAfterCase5, null);
         PsiMethod psiMethodBeforeCase6 = psiElementFactory.createMethodFromText(methodBeforeCase6, null);
         PsiMethod psiMethodAfterCase6 = psiElementFactory.createMethodFromText(methodAfterCase6, null);
         PsiMethod psiMethodBeforeCase7 = psiElementFactory.createMethodFromText(methodBeforeCase7, null);
         PsiMethod psiMethodAfterCase7 = psiElementFactory.createMethodFromText(methodAfterCase7, null);
         PsiMethod psiMethodBeforeCase8 = psiElementFactory.createMethodFromText(methodBeforeCase8, null);
         PsiMethod psiMethodAfterCase8 = psiElementFactory.createMethodFromText(methodAfterCase8, null);
         PsiMethod psiMethodBeforeCase9 = psiElementFactory.createMethodFromText(methodBeforeCase9, null);
         PsiMethod psiMethodAfterCase9 = psiElementFactory.createMethodFromText(methodAfterCase9, null);
         PsiMethod psiMethodBeforeCase10 = psiElementFactory.createMethodFromText(methodBeforeCase10, null);
         PsiMethod psiMethodAfterCase10 = psiElementFactory.createMethodFromText(methodAfterCase10, null);

         assertEquals(false, GitVcs.compare(psiMethodBeforeCase1, psiMethodAfterCase1));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase2, psiMethodAfterCase2));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase3, psiMethodAfterCase3));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase4, psiMethodAfterCase4));
         assertEquals(true, GitVcs.compare(psiMethodBeforeCase5, psiMethodAfterCase5));
         assertEquals(true, GitVcs.compare(psiMethodBeforeCase6, psiMethodAfterCase6));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase7, psiMethodAfterCase7));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase8, psiMethodAfterCase8));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase9, psiMethodAfterCase9));
         assertEquals(false, GitVcs.compare(psiMethodBeforeCase10, psiMethodAfterCase10));


    }

    @Test
    public void getCurrentBranch() {
    }

    @Test
    public void getAffectedFiles() {
    }

}