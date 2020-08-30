import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.intellij.psi.util.MethodSignatureUtil.areParametersErasureEqual;
import static com.intellij.psi.util.MethodSignatureUtil.areSignaturesEqual;

public class GitVcs {

    public void getCurrentBranch(Project project) {
        AbstractVcs[] VCS = ProjectLevelVcsManager.getInstance(project).getAllActiveVcss();
        if (VCS.length == 0) {

        }

        //AbstractVcs currentVersionControl = VCS[0];
        //VCS[0].getChangeProvider().getChanges();
    }

    @NotNull
    public static List<VirtualFile> getAffectedFiles(String changeListName, Project project) {
        List<VirtualFile> files = new ArrayList<VirtualFile>();
        final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        final List<VirtualFile> modifiedFiles = ChangeListManager.getInstance(project).getAffectedFiles();
        //modifiedFiles.get(0).
        if (changeListName == null) {
            return changeListManager.getAffectedFiles();
        }
        //final LocalChangeList changeList = changeListManager.findChangeList(changeListName);
        final LocalChangeList changeList = changeListManager.getChangeLists().get(0);
        if (changeList != null) {
            System.out.println("ChangeListSize : "+changeList.getChanges().size());
            for (Change change : changeList.getChanges()) {
                final ContentRevision afterRevision = change.getAfterRevision();
                final ContentRevision beforeRevision = change.getBeforeRevision();
                ArrayList<PsiMethod> beforeMethods = new ArrayList<>();
                ArrayList<PsiMethod> afterMethods = new ArrayList<>();

                try {
                    //System.out.println("After : " + afterRevision.getContent());
                    //System.out.println("Before : " + beforeRevision.getContent());

                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);

                    PsiFile before = factory.createFileFromText(JavaLanguage.INSTANCE, afterRevision.getContent());
                    PsiFile after = factory.createFileFromText(JavaLanguage.INSTANCE, beforeRevision.getContent());

                    if (before instanceof PsiJavaFile && after instanceof PsiJavaFile) {
                        PsiClass[] psiClassesBefore = ((PsiJavaFile) before).getClasses();
                        for (PsiClass psiClass : psiClassesBefore) {
                            for (PsiMethod method : psiClass.getMethods()) {
                                beforeMethods.add(method);
                            }
                        }

                        PsiClass[] psiClassesAfter = ((PsiJavaFile) after).getClasses();
                        for (PsiClass psiClass : psiClassesAfter) {
                            for (PsiMethod method : psiClass.getMethods()) {
                                afterMethods.add(method);
                            }
                        }
                        //System.out.println("Checking equal");
                        for (PsiMethod psiMethodBefore:beforeMethods) {
                            for (PsiMethod psiMethodAfter:afterMethods) {
                                if(psiMethodAfter.getName().equals(psiMethodBefore.getName()) && areSignaturesEqual(psiMethodAfter,psiMethodBefore)){
                                    //System.out.println(psiMethodAfter.getName()+" equal "+psiMethodBefore.getName());
                                    String af=psiMethodAfter.getText().trim();
                                    String bef=psiMethodBefore.getText().trim();
                                    System.out.println(psiMethodBefore.getName()+" & "+psiMethodAfter.getName()+" "+compare(psiMethodBefore,psiMethodAfter));
                                    //System.out.println("c*"+ psiMethodAfter.toString()+"c*");
                                    //System.out.println("c*"+ psiMethodBefore+"c*");
                                    /*for (PsiElement psi:psiMethodAfter.getBody().getChildren()
                                         ) {
                                        System.out.println(psi.getChildren().length);
                                    }
                                    System.out.println("->");
                                    for (PsiElement psi:psiMethodBefore.getBody().getChildren()
                                    ) {
                                        System.out.println(psi.getChildren());
                                    }*/

                                }else{
                                    System.out.println(psiMethodBefore.getName()+" & "+psiMethodAfter.getName()+" "+0);
                                }
                            }
                        }
                    }
                    //-->Important-->PsiFile psiFile = PsiManager.getInstance(project).findFile(change.getVirtualFile());
                }catch (Exception e){

                }
                //PsiFile[] psiFiles= FilenameIndex.getFilesByName(project,afterRevision.getFile().getName(), GlobalSearchScope.projectScope(project));

                /*for (PsiFile psiFile:psiFiles) {
                    System.out.println("Path-"+psiFile.getVirtualFile().getPath());
                    if (psiFile instanceof PsiJavaFile) {
                        PsiClass[] psiClasses = ((PsiJavaFile) psiFile).getClasses();
                        for (PsiClass psiClass : psiClasses) {
                            for (PsiMethod method : psiClass.getMethods()) {
                                //System.out.println(method.getName());
                                System.out.println(method);

                            }
                        }
                    }
                }*/
            }
        }
        return files;
    }

    public static int compare(PsiElement before, PsiElement after) {
        if (!before.toString().equals(after.toString())) {
            //System.out.println(before+"="+after);
            return 0;
        } else {
            if (before.getChildren().length == 0 || after.getChildren().length == 0) {
                return 1;
            } else if (after.getChildren().length != before.getChildren().length) {
                //System.out.println(before.getChildren().length+"="+after.getChildren().length);
                return 0;
            } else {
                int comp = 1;
                for (int i = 0; i < before.getChildren().length; i++) {
                    if(compare(before.getChildren()[i],after.getChildren()[i])==0){
                        comp=0;
                        break;
                    }
                }
                return comp;
            }
        }
    }
}
//areSignaturesEqual(psiMethodAfter,psiMethodBefore