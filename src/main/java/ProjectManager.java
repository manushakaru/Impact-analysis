//import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiDocumentManagerImpl;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import model.ImpactSet;
import model.MethodEntity;
import model.ReferenceEntity;
import org.jf.dexlib2.iface.reference.MethodReference;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {

    public List<MethodEntity> getClassEntityList(Project project) {
        List<MethodEntity> methodList = new ArrayList<>();
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null)
            return methodList;
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        getElement(4,psiFile,project);
        if (psiFile instanceof PsiJavaFile) {
            PsiClass[] psiClasses = ((PsiJavaFile) psiFile).getClasses();
            analyzeClasses(methodList, psiClasses, project);

        }
        return methodList;
    }

    private void analyzeClasses(List<MethodEntity> methodList, PsiClass[] psiClasses, Project project) {
        for (PsiClass psiClas : psiClasses) {
            for (PsiMethod method : psiClas.getMethods()) {

                MethodEntity methodEntity=new MethodEntity(method.getName());
                methodEntity.setImpactSet(execute(method,Utils.depth,project));

                for (ReferenceEntity referenceEntity:executeCallee(method,Utils.depth).getReferences()) {
                    if(!methodEntity.getImpactSet().contains(referenceEntity)){
                        methodEntity.getImpactSet().addReference(referenceEntity);
                    }
                }

                methodList.add(methodEntity);
                //PsiFile[] psiFile=FilenameIndex.getFilesByName(project,"",GlobalSearchScope.projectScope(project));

            }
        }
    }

    public ImpactSet execute(PsiMethod method,int depth,Project project){
        if(depth==0){return null;}

        ImpactSet impactSet =new ImpactSet();

        try{
            for (PsiReference psiReference :
                    MethodReferencesSearch.search(method, GlobalSearchScope.projectScope(project),false).findAll()) {
                impactSet.addReference(new ReferenceEntity(psiReference));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
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
                        impactSet.addReference(new ReferenceEntity(psiMethodCallExpression.resolveMethod()));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
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

    public PsiElement getElement(int line,PsiFile psiFile,Project project){
        final Document document = PsiDocumentManagerImpl.getInstance(project).getDocument(psiFile);
        final int offset = document.getLineStartOffset(line - 1);
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiFile.findElementAt(offset), PsiMethod.class);
        System.out.println("Text : "+psiFile.findElementAt(offset)+psiMethod.getName());
        return psiFile.findElementAt(offset);
    }

}
