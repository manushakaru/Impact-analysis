import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.search.MethodUsagesSearcher;
import org.jetbrains.annotations.NotNull;

public class Analyse extends com.intellij.openapi.actionSystem.AnAction {

    AnalyseReport analyseReport;
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        /*analyseReport=new AnalyseReport(true);
        String depth=Messages.showInputDialog("Insert Depth","Analyse",Messages.getInformationIcon());
        analyseReport.showAndGet();*/
        GitVcs.getAffectedFiles("",e.getProject());

    }
}
