import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import model.MethodEntity;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.psi.util.MethodSignatureUtil.areSignaturesEqual;

public class GitVcs {

    public static List<MethodEntity> getAffectedFiles(Project project) {

        List<MethodEntity> methodList = new ArrayList<>();
        final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        final List<VirtualFile> modifiedFiles = ChangeListManager.getInstance(project).getAffectedFiles();
        final LocalChangeList changeList = changeListManager.getChangeLists().get(0);

        if (changeList != null) {

            System.out.println("ChangeListSize : " + changeList.getChanges().size());
            for (Change change : changeList.getChanges()) {
                final ContentRevision afterRevision = change.getAfterRevision();
                final ContentRevision beforeRevision = change.getBeforeRevision();
                ArrayList<PsiMethod> beforeMethods = new ArrayList<>();
                ArrayList<PsiMethod> afterMethods = new ArrayList<>();

                try {
                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);

                    PsiFile before = factory.createFileFromText(JavaLanguage.INSTANCE, beforeRevision.getContent());
                    PsiFile after = PsiManager.getInstance(project).findFile(afterRevision.getFile().getVirtualFile());

                    if (before instanceof PsiJavaFile && after instanceof PsiJavaFile) {

                        PsiJavaFile jBefore = (PsiJavaFile) before;
                        PsiJavaFile jAfter = (PsiJavaFile) after;

                        PsiClass[] psiClassesBefore = jBefore.getClasses();
                        PsiClass[] psiClassesAfter = jAfter.getClasses();

                        for (PsiClass psiClass : psiClassesAfter) {
                            for (PsiMethod method : psiClass.getMethods()) {
                                afterMethods.add(method);
                            }
                        }

                        for (PsiMethod psiMethodAfter : afterMethods) {

                            beforeMethods.clear();
                            for (PsiClass psiClass : psiClassesBefore) {
                                if (psiClass.getName().equals(psiMethodAfter.getContainingClass().getName())) {
                                    for (PsiMethod method : psiClass.getMethods()) {
                                        if (psiMethodAfter.getName().equals(method.getName())) {
                                            beforeMethods.add(method);
                                        }
                                    }
                                }
                            }

                            if (beforeMethods.size() == 0) {
                                //method has changed
                                methodList.add(new MethodEntity(psiMethodAfter));
                                continue;
                            }

                            boolean hasSimilar = false;
                            for (PsiMethod psiMethodBefore : beforeMethods) {
                                if (psiMethodAfter.getName().equals(psiMethodBefore.getName()) && areSignaturesEqual(psiMethodAfter, psiMethodBefore)) {
                                    for (int i = 0; i < psiMethodAfter.getModifierList().getChildren().length; i++) {
                                        System.out.println(psiMethodAfter.getName() + " " + psiMethodAfter.getModifierList().getChildren()[i] + " CHILD " + compare(psiMethodAfter.getModifierList().getChildren()[i], psiMethodBefore.getModifierList().getChildren()[i]));
                                    }

                                    String af = psiMethodAfter.getText().trim();
                                    String bef = psiMethodBefore.getText().trim();
                                    if (compare(psiMethodBefore.getBody(), psiMethodAfter.getBody()) == 1) {
                                        hasSimilar = true;
                                        break;
                                    }
                                }
                            }

                            if (!hasSimilar) {
                                methodList.add(new MethodEntity(psiMethodAfter));
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return methodList;
    }

    //return 0 if not equal
    public static int compare(PsiElement before, PsiElement after) {
        if (!before.toString().trim().equals(after.toString().trim())) {
            System.out.println(before + "=" + after);
            return 0;
        } else {
            if (before.getChildren().length == 0 || after.getChildren().length == 0) {
                return 1;
            } else if (after.getChildren().length != before.getChildren().length) {
                return 0;
            } else {
                int comp = 1;
                for (int i = 0; i < before.getChildren().length; i++) {
                    if (compare(before.getChildren()[i], after.getChildren()[i]) == 0) {
                        comp = 0;
                        break;
                    }
                }
                return comp;
            }
        }
    }
}
