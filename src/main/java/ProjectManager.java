//import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import model.ImpactSet;
import model.MethodEntity;
import model.ReferenceEntity;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
    static Project project1;

    public static List<MethodEntity> getClassEntityList(Project project) {
        List<MethodEntity> methodList = new ArrayList<>();
        project1=project;

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null)
            return methodList;
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile instanceof PsiJavaFile) {
            PsiClass[] psiClasses = ((PsiJavaFile) psiFile).getClasses();
            analyzeClasses(methodList, psiClasses, 1);
        }
        return methodList;
    }

    private static void analyzeClasses(List<MethodEntity> methodList, PsiClass[] psiClasses, int counter) {
        for (PsiClass psiClas : psiClasses) {
            //ClassEntity entity = new ClassEntity(psiClas.getName());
            for (PsiMethod method : psiClas.getMethods()) {
                /*MethodEntity methodEntity=new MethodEntity(method.getName());
                try{
                    for (PsiReference psiReference :
                            MethodReferencesSearch.search(method, GlobalSearchScope.projectScope(project1),false).findAll()) {
                        methodEntity.addPsiReference(psiReference);
                        PsiMethod psiMethod=PsiTreeUtil.getParentOfType(psiReference.getElement(), PsiMethod.class);
                        methodEntity.addPsiReferenceString(psiMethod.getContainingClass().getName()+"->"+psiMethod.getName());
                        //if(methodEntity.getPsiReferencesString().contains("Main->main"))

                    }

                }catch (Exception e){}*/
                MethodEntity methodEntity=new MethodEntity(method.getName());
                methodEntity.setImpactSet(execute(method,Utils.depth));
                methodList.add(methodEntity);
                //methodList.add(methodEntity);
            }
            /*if (psiClas.getAllInnerClasses().length != 0 && counter > 0)
                analyzeClasses(classList, psiClas.getAllInnerClasses(), --counter);*/
        }
    }

    public static ImpactSet execute(PsiMethod method,int depth){
        if(depth==0){return null;}

        ImpactSet impactSet =new ImpactSet();

        try{
            for (PsiReference psiReference :
                    MethodReferencesSearch.search(method, GlobalSearchScope.projectScope(project1),false).findAll()) {
                impactSet.addReference(new ReferenceEntity(psiReference));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        ImpactSet indirectImpactSet =new ImpactSet();

        for (ReferenceEntity referenceEntity:impactSet.getReferences()) {
            indirectImpactSet=execute(referenceEntity.getPsiMethod(),depth-1);
        }

        if(indirectImpactSet==null){return impactSet;}

        for (ReferenceEntity referenceEntity:indirectImpactSet.getReferences()) {
            if(!impactSet.contains(referenceEntity)){
                impactSet.addReference(referenceEntity);
            }
        }

        return impactSet;
    }

}
