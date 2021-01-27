import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.psi.*;
import model.MethodEntity;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.psi.util.MethodSignatureUtil.areSignaturesEqual;

/**
 * Handle git changes
 *
 * @version 1.0
 */
public class GitVcs {

    /**
     * First get all the git changes comparing after revision against before revision by
     * comparing each method and identifying the changes between methods.
     *
     * @param project An object representing an IntelliJ project.
     * @return list of method entities which are identified as changes
     */
    public static List<MethodEntity> getAffectedFiles(Project project) {

        // Initialize empty MethodEntity list
        List<MethodEntity> methodList = new ArrayList<>();
        // Initialize ChangeListManager using current project
        final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        // Get LocalChangeList from changeListManager
        final LocalChangeList localChangeList = changeListManager.getChangeLists().get(0);

        // Proceed if the localChangeList is not null
        if (localChangeList != null) {

            //System.out.println("ChangeListSize : "+localChangeList.getChanges().size());
            // Loop through changes in the localChangeList
            for (Change change : localChangeList.getChanges()) {
                // Get content of the after revision of a change
                final ContentRevision afterRevision = change.getAfterRevision();
                // Get content of the before revision of a change
                final ContentRevision beforeRevision = change.getBeforeRevision();

                ArrayList<PsiMethod> beforeMethods = new ArrayList<>();
                ArrayList<PsiMethod> afterMethods = new ArrayList<>();

                try {
                    // Get instance of PsiFileFactory which creates a file from the specified text
                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);

                    // Create PsiFile for before revision text
                    PsiFile before = factory.createFileFromText(JavaLanguage.INSTANCE, beforeRevision.getContent());
                    // Get PsiFile for after revision using PsiManager instance
                    PsiFile after = PsiManager.getInstance(project).findFile(afterRevision.getFile().getVirtualFile());

                    // Check for the instances are  PsiJavaFiles
                    if (before instanceof PsiJavaFile && after instanceof PsiJavaFile) {

                        // Cast to PsiJavaFile objects
                        PsiJavaFile jBefore = (PsiJavaFile) before;
                        PsiJavaFile jAfter = (PsiJavaFile) after;

                        // Get list of PsiClasses
                        PsiClass[] psiClassesBefore = jBefore.getClasses();
                        PsiClass[] psiClassesAfter = jAfter.getClasses();

                        // Loop through after revision psiClasses
                        for (PsiClass psiClass : psiClassesAfter) {
                            // Loop through psiMethods inside a psiClass
                            for (PsiMethod method : psiClass.getMethods()) {
                                // Add method to list
                                afterMethods.add(method);
                            }
                        }
                        //System.out.println("Checking equal");
                        for (PsiMethod psiMethodAfter : afterMethods) {

                            beforeMethods.clear();

                            // Loop through before revision psiClasses
                            for (PsiClass psiClassBefore : psiClassesBefore) {
                                // Check if the before revision class name is equal to after revision class name
                                if (psiClassBefore.getName().equals(psiMethodAfter.getContainingClass().getName())) {
                                    // Loop through psiMethods in before revision psiClass where method name is equals to
                                    // the after revision method name
                                    for (PsiMethod method : psiClassBefore.findMethodsByName(psiMethodAfter.getName(), false)) {
                                        // Add method to beforeMethods list
                                        beforeMethods.add(method);
                                    }
                                }
                            }

                            // If the beforeMethods list is empty, then after revision psiMethod is new method
                            if (beforeMethods.size() == 0) {
                                //method has changed
                                methodList.add(new MethodEntity(psiMethodAfter));
                                continue;
                            }

                            // Check for a similar method
                            boolean hasSimilar = false;
                            // Loop through before revision PsiMethods
                            for (PsiMethod psiMethodBefore : beforeMethods) {
                                // Check the signature of the two methods are equal or not
                                if (areSignaturesEqual(psiMethodAfter, psiMethodBefore)) {
                                    // Compare before revision and after revision methods
                                    if (compare(psiMethodBefore, psiMethodAfter)) {
                                        hasSimilar = true;
                                        break;
                                    }
                                }
                            }
                            // If the both before and after methods are not equal, then there is a change
                            // between two methods
                            if (!hasSimilar) {
                                methodList.add(new MethodEntity(psiMethodAfter));
                            }
                        }
                    }

                } catch (Exception e) {
//                    Messages.showMessageDialog(e.getMessage(), "Error", Messages.getErrorIcon());
                }
            }
        }
        return methodList;
    }


    /**
     * Compare two PsiElement objects and return true if the both PsiElements are equal
     *
     * @param before PsiElement
     * @param after PsiElement
     * @return true if the two PsiElements are equal
     */
    public static boolean compare(PsiElement before, PsiElement after) {
        // If the PsiElement does not contain child
        if (before.getChildren().length == 0 && after.getChildren().length == 0) {
            // Convert to string and check the equality of the elements
            if (before.toString().trim().equals(after.toString().trim())) {
                System.out.println(before + "=" + after);
                return true;
            } else {
                return false;
            }
        }
        // If the children count is not same return false
        else if (after.getChildren().length != before.getChildren().length) {
            return false;
        }
        // If the PsiElement has children
        else {
            boolean comp = true;
            // Loop through before revision children
            for (int i = 0; i < before.getChildren().length; i++) {
                // Break loop if the Both PsiElements are not same
                if (!compare(before.getChildren()[i], after.getChildren()[i])) {
                    comp = false;
                    break;
                }
            }
            return comp;
        }
    }

}