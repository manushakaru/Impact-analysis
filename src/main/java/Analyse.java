import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class Analyse extends com.intellij.openapi.actionSystem.AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GitVcs.getAffectedFiles(e.getProject());
    }
}
