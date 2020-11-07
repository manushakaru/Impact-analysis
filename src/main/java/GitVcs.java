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
import com.intellij.psi.search.FilenameIndex;
import model.MethodEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.intellij.psi.util.MethodSignatureUtil.areParametersErasureEqual;
import static com.intellij.psi.util.MethodSignatureUtil.areSignaturesEqual;

public class GitVcs {

    public static List<MethodEntity> getAffectedFiles(Project project) {
        List<MethodEntity> methodList = new ArrayList<>();
        final ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        final List<VirtualFile> modifiedFiles = ChangeListManager.getInstance(project).getAffectedFiles(); // ??
        //modifiedFiles.get(0);
        //now it only gets first changeList--> change it to get all
        final LocalChangeList changeList = changeListManager.getChangeLists().get(0);

        if (changeList != null) {

            System.out.println("ChangeListSize : "+changeList.getChanges().size());
            for (Change change : changeList.getChanges()) {
                final ContentRevision afterRevision = change.getAfterRevision();
                final ContentRevision beforeRevision = change.getBeforeRevision();
                ArrayList<PsiMethod> beforeMethods = new ArrayList<>();
                ArrayList<PsiMethod> afterMethods = new ArrayList<>();

                try {
                    final PsiFileFactory factory = PsiFileFactory.getInstance(project);

                    PsiFile before = factory.createFileFromText(JavaLanguage.INSTANCE, beforeRevision.getContent());
                    //PsiFile after = factory.createFileFromText(JavaLanguage.INSTANCE, beforeRevision.getContent());
                    PsiFile after = PsiManager.getInstance(project).findFile(afterRevision.getFile().getVirtualFile());

                    if (before instanceof PsiJavaFile && after instanceof PsiJavaFile) {

                        PsiJavaFile jBefore=(PsiJavaFile) before;
                        PsiJavaFile jAfter=(PsiJavaFile) after;

                        PsiClass[] psiClassesBefore = jBefore.getClasses();
                        PsiClass[] psiClassesAfter = jAfter.getClasses();

                        for (PsiClass psiClass : psiClassesAfter) {
                            for (PsiMethod method : psiClass.getMethods()) {
                                afterMethods.add(method);
                            }
                        }
                        //System.out.println("Checking equal");
                        for (PsiMethod psiMethodAfter:afterMethods) {

                            beforeMethods.clear();
                            for (PsiClass psiClass : psiClassesBefore) {
                                if(psiClass.getName().equals(psiMethodAfter.getContainingClass().getName())) {
                                    for (PsiMethod method : psiClass.getMethods()) {
                                        if(psiMethodAfter.getName().equals(method.getName())) {
                                            beforeMethods.add(method);
                                        }
                                    }
                                }
                            }

                            if(beforeMethods.size()==0){
                                //method has changed
                                methodList.add(new MethodEntity(psiMethodAfter));
                                continue;
                            }

                            boolean hasSimilar=false;
                            for (PsiMethod psiMethodBefore:beforeMethods) {
                                if(psiMethodAfter.getName().equals(psiMethodBefore.getName()) && areSignaturesEqual(psiMethodAfter,psiMethodBefore)){
                                    //System.out.println(psiMethodAfter.getName()+" equal "+psiMethodBefore.getName());
                                    // System.out.println(psiMethodAfter.getModifierList().getText()); --> Modifier list equality is not checking

                                    for (int i = 0; i < psiMethodAfter.getModifierList().getChildren().length; i++) {
                                        System.out.println(psiMethodAfter.getName()+" "+psiMethodAfter.getModifierList().getChildren()[i]+" CHILD "+compare(psiMethodAfter.getModifierList().getChildren()[i],psiMethodBefore.getModifierList().getChildren()[i]));
                                    }

                                    String af=psiMethodAfter.getText().trim();
                                    String bef=psiMethodBefore.getText().trim();
                                    if(compare(psiMethodBefore.getBody(),psiMethodAfter.getBody())==1) {
                                        hasSimilar=true;
                                        break;
                                    }
                                }else{
                                    //System.out.println(psiMethodBefore.getName()+" & "+psiMethodAfter.getName()+" "+0);
                                }
                            }

                            if(!hasSimilar){
                                methodList.add(new MethodEntity(psiMethodAfter));
                            }
                        }
                    }
                    //-->Important-->PsiFile psiFile = PsiManager.getInstance(project).findFile(change.getVirtualFile());
                }catch (Exception e){
                    System.out.println(e.getMessage());
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
        return methodList;
    }
    //return 0 if not equal
    public static int compare(PsiElement before, PsiElement after) {
        if (!before.toString().trim().equals(after.toString().trim())) {
            System.out.println(before+"="+after);
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
//areSignaturesEqual(psiMethodAfter,psiMethodBefore)