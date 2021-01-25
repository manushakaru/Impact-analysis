import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import model.ImpactSet;
import model.MethodEntity;
import model.ReferenceEntity;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {

    public List<MethodEntity> getClassEntityList(Project project) {
        if(Utils.isCurrent) {
            List<MethodEntity> methodList = new ArrayList<>();
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor == null)
                return methodList;
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

            if (psiFile instanceof PsiJavaFile) {
                PsiClass[] psiClasses = ((PsiJavaFile) psiFile).getClasses();
                analyzeClasses(methodList, psiClasses, project);
            }

            return methodList;
        }else{
            List<MethodEntity> methodList = GitVcs.getAffectedFiles(project);
            analyzeClasses(methodList, project);
            return methodList;
        }
    }

    private void analyzeClasses(List<MethodEntity> methodList, PsiClass[] psiClasses, Project project) {
        for (PsiClass psiClas : psiClasses) {
            for (PsiMethod method : psiClas.getMethods()) {

                MethodEntity methodEntity=new MethodEntity(method);
                methodEntity.setImpactSet(execute(method,Utils.depth,project));

                for (ReferenceEntity referenceEntity:executeCallee(method,Utils.depth).getReferences()) {
                    if(!methodEntity.getImpactSet().contains(referenceEntity)){
                        methodEntity.getImpactSet().addReference(referenceEntity);
                    }
                }
                methodList.add(methodEntity);
            }
        }
    }

    private void analyzeClasses(List<MethodEntity> methodList, Project project) {
            for (MethodEntity methodEntity : methodList) {
                methodEntity.setImpactSet(execute(methodEntity.getPsiMethod(),Utils.depth,project));

                for (ReferenceEntity referenceEntity:executeCallee(methodEntity.getPsiMethod(),Utils.depth).getReferences()) {
                    if(!methodEntity.getImpactSet().contains(referenceEntity)){
                        methodEntity.getImpactSet().addReference(referenceEntity);
                    }
                }
            }
    }

    public ImpactSet execute(PsiMethod method,int depth,Project project){
        if(depth==0){return null;}

        ImpactSet impactSet =new ImpactSet();

        try{
            for (PsiReference psiReference :
                    MethodReferencesSearch.search(method, GlobalSearchScope.projectScope(project),false).findAll()) {
                impactSet.addReference(new ReferenceEntity(psiReference,Utils.depth-depth+1));
            }
        }catch (Exception e){
            System.out.println("ProjectManager:75:"+e.getMessage());
        }

        ImpactSet indirectImpactSet =new ImpactSet();

        for (ReferenceEntity referenceEntity:impactSet.getReferences()) {
            indirectImpactSet=execute(referenceEntity.getPsiMethod(),depth-1,project);
        }

        if(indirectImpactSet==null){return impactSet;}

        for (ReferenceEntity referenceEntity:indirectImpactSet.getReferences()) {
            if(!impactSet.contains(referenceEntity)){
                impactSet.addReference(referenceEntity);
            }
        }

        return impactSet;
    }

    public ImpactSet executeCallee(PsiMethod method,int depth){
        if(depth==0){return null;}

        ImpactSet impactSet =new ImpactSet();

        try{
            PsiStatement[] psiStatements=method.getBody().getStatements();
            for (int i = 0; i < psiStatements.length; i++) {
                for(PsiElement child:psiStatements[i].getChildren()){
                    if(child instanceof PsiMethodCallExpression){
                        PsiMethodCallExpression psiMethodCallExpression= (PsiMethodCallExpression)child;
                        impactSet.addReference(new ReferenceEntity(psiMethodCallExpression.resolveMethod(),Utils.depth-depth+1));
                    }
                }
            }
        }catch (Exception e){
            System.out.println("ProjectManager:116:"+e.getMessage()+" > "+method.getName());
            e.printStackTrace();
        }

        ImpactSet indirectImpactSet =new ImpactSet();

        for (ReferenceEntity referenceEntity:impactSet.getReferences()) {
            indirectImpactSet=executeCallee(referenceEntity.getPsiMethod(),depth-1);
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
