import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class Analyse : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        Messages.showInputDialog("Insert Depth", "Analyse", Messages.getInformationIcon())
        AnalyseReport().showAndGet()
    }
}