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

/**
 * Implementation of executing caller and calle method search
 *
 * @version 1.0
 */
public class ProjectManager {

    /**
     * Get all Method Entities
     * Takes project object and execute caller and callee search with different modes such as
     * current file impact analysis mode and git change analysis mode
     *
     * @param project an object representing an IntelliJ project
     * @return list of method entities
     */
    public List<MethodEntity> getMethodEntityList(Project project) {

        // If the current file change analysis mode is selected
        if (Utils.isCurrent) {

            List<MethodEntity> methodList = new ArrayList<>();
            // Get currently selected text editor
            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            if (editor == null)
                return methodList;
            // Get PsiFile of the current editor document
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

            // Check the psiFile instance is a java file
            if (psiFile instanceof PsiJavaFile) {
                // Get all classes in the file
                PsiClass[] psiClasses = ((PsiJavaFile) psiFile).getClasses();
                analyzeClasses(methodList, psiClasses, project);
            }

            return methodList;
        }
        // If the git change analysis mode is selected
        else {
            // Get git changes list
            List<MethodEntity> methodList = GitVcs.getAffectedFiles(project);
            analyzeClasses(methodList, project);
            return methodList;
        }
    }

    /**
     * Create ImpactSet by executing checks for caller and callee methods
     * Generate all caller and callee method for each method in the methodList and for each class in the
     * classes list and set impactSet of the related MethodEntity object
     *
     * @see MethodEntity
     * @see ImpactSet
     *
     * @param methodList list of MethodEntities
     * @param psiClasses PsiElement which represents a Java class or interface
     * @param project an object representing an IntelliJ project
     */
    private void analyzeClasses(List<MethodEntity> methodList, PsiClass[] psiClasses, Project project) {

        // Loop through all the classes
        for (PsiClass psiClas : psiClasses) {
            // Loop through all methods inside a class
            for (PsiMethod method : psiClas.getMethods()) {

                // Create MethodEntity object
                MethodEntity methodEntity = new MethodEntity(method);
                // Generate impact set
                methodEntity.setImpactSet(execute(method, Utils.depth, project));

                // Loop through reference entities of Callees
                for (ReferenceEntity referenceEntity : executeCallee(method, Utils.depth).getReferenceEntities()) {
                    // If referenceEntity is not containing in impact set, then add referenceEntity into
                    // impact set
                    if (!methodEntity.getImpactSet().contains(referenceEntity)) {
                        methodEntity.getImpactSet().addReference(referenceEntity);
                    }
                }
                methodList.add(methodEntity);
            }
        }
    }

    /**
     * Create ImpactSet by executing checks for caller and callee methods
     * Generate all caller and callee method for each method in the methodList and set impactSet
     * of the related MethodEntity object
     *
     * @see MethodEntity
     * @see ImpactSet
     *
     * @param methodList list of MethodEntities
     * @param project an object representing an IntelliJ project
     */
    private void analyzeClasses(List<MethodEntity> methodList, Project project) {
        // Loop through methods
        for (MethodEntity methodEntity : methodList) {
            // Generate impact set
            methodEntity.setImpactSet(execute(methodEntity.getPsiMethod(), Utils.depth, project));

            // Loop through reference entities of Callees
            for (ReferenceEntity referenceEntity : executeCallee(methodEntity.getPsiMethod(), Utils.depth).getReferenceEntities()) {
                // If referenceEntity is not containing in impact set, then add referenceEntity into impact set
                if (!methodEntity.getImpactSet().contains(referenceEntity)) {
                    methodEntity.getImpactSet().addReference(referenceEntity);
                }
            }
        }
    }

    /**
     * Get all callers of a method according to the given depth
     * This method takes PsiMethod, depth of the search and IntelliJ project as parameters and
     * do the global scope reference method search recursively and returns an ImpactSet.
     *
     * @param method a Java method or constructor
     * @param depth searching depth
     * @param project an object representing an IntelliJ project
     * @return Impact set which contains caller methods of the given method
     */
    public ImpactSet execute(PsiMethod method, int depth, Project project) {
        // Return null when depth is 0
        if (depth == 0) {
            return null;
        }

        ImpactSet impactSet = new ImpactSet();

        try {
            // Search for a method using MethodReferenceSearch and get all usages
            for (PsiReference psiReference :
                    MethodReferencesSearch.search(method, GlobalSearchScope.projectScope(project), false).findAll()) {
                // Create ReferenceEntity object and add into ImpactSet
                impactSet.addReference(new ReferenceEntity(psiReference, Utils.depth - depth + 1));
            }
        } catch (Exception e) {
            System.out.println("ProjectManager:75:" + e.getMessage());
        }

        ImpactSet indirectImpactSet = new ImpactSet();

        // Get indirect impact methods by recursive call
        for (ReferenceEntity referenceEntity : impactSet.getReferenceEntities()) {
            indirectImpactSet = execute(referenceEntity.getPsiMethod(), depth - 1, project);
        }

        // If the indirectImpactSet is null then return impactSet object
        if (indirectImpactSet == null) {
            return impactSet;
        }

        // Loop through referenceEntities in indirectImpactSet
        for (ReferenceEntity referenceEntity : indirectImpactSet.getReferenceEntities()) {
            if (!impactSet.contains(referenceEntity)) {
                impactSet.addReference(referenceEntity);
            }
        }

        return impactSet;
    }

    /**
     * Get all callees of a method according to the given depth
     * This method takes PsiMethod and depth of the search  as parameters and
     * do the search for PsiMethodCallExpressions recursively and returns an ImpactSet.
     *
     * @param method a Java method or constructor
     * @param depth searching depth
     * @return Impact set which contains callee methods of the given method
     *
     */
    public ImpactSet executeCallee(PsiMethod method, int depth) {
        // Return null when depth is 0
        if (depth == 0) {
            return null;
        }

        ImpactSet impactSet = new ImpactSet();

        try {
            // Get all PsiStatements inside a method
            PsiStatement[] psiStatements = method.getBody().getStatements();
            // Loop through PsiStatements
            for (int i = 0; i < psiStatements.length; i++) {
                // Loop through child PsiElements of a PsiStatement
                for (PsiElement child : psiStatements[i].getChildren()) {
                    // Check if the PsiElement is an instance of a PsiMethodCallExpression
                    if (child instanceof PsiMethodCallExpression) {
                        // Cast to PsiMethodCallExpression
                        PsiMethodCallExpression psiMethodCallExpression = (PsiMethodCallExpression) child;
                        // Create ReferenceEntity and add into impactSet
                        impactSet.addReference(new ReferenceEntity(psiMethodCallExpression.resolveMethod(), Utils.depth - depth + 1));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ProjectManager:116:" + e.getMessage() + " > " + method.getName());
            e.printStackTrace();
        }

        ImpactSet indirectImpactSet = new ImpactSet();

        // Get indirect callee impact methods by recursive call
        for (ReferenceEntity referenceEntity : impactSet.getReferenceEntities()) {
            indirectImpactSet = executeCallee(referenceEntity.getPsiMethod(), depth - 1);
        }

        // If the indirectImpactSet is null then return impactSet object
        if (indirectImpactSet == null) {
            return impactSet;
        }

        // Loop through referenceEntities in indirectImpactSet
        for (ReferenceEntity referenceEntity : indirectImpactSet.getReferenceEntities()) {
            if (!impactSet.contains(referenceEntity)) {
                impactSet.addReference(referenceEntity);
            }
        }

        return impactSet;
    }
}
