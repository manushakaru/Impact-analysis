import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import java.awt.*;

public class AnalyseReport extends DialogWrapper {

    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private final JTextField textField = new JTextField();

    protected AnalyseReport(boolean canBeParent) {
        super(canBeParent);
    }

    @Override
    protected void init() {
        super.init();
        setTitle("Analyse Report");
        setSize(600, 400);
    }

    @Override
    protected JComponent createCenterPanel() {
        mainPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.add(textField);
        return mainPanel;
    }
}
