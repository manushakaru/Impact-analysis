import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Performs lazy initialization of a tool window
 *
 * @version 1.0
 */
public class MyToolWindowFactory implements ToolWindowFactory {

    /**
     * Create tool window content
     * Create new MyToolWindow instance and create tool window content using MyToolWindow instance
     *
     * @param project An object representing an IntelliJ project
     * @param toolWindow Tool windows expose UI for specific functionality
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow,project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}