import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.ui.DialogWrapper;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AnalyseReport extends DialogWrapper {

    private JPanel mainPanel = new JPanel(new GridBagLayout());
    //private JScrollPane ChangesScrollPane;
    //private GridBag gridBag;
    private JTextField textField=new JTextField();

    protected AnalyseReport(boolean canBeParent) {
        super(canBeParent);
    }

    @Override
    protected void init() {
        super.init();
        setTitle("Analyse Report");
        setSize(600,400);
    }

    @Override
    protected JComponent createCenterPanel() {
        mainPanel.setPreferredSize(new Dimension(600,400));
        /*gridBag = new GridBag().
                setDefaultInsets(new Insets(0,0, AbstractLayout.DEFAULT_VGAP,AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
        .setDefaultFill(GridBagConstraints.HORIZONTAL);*/
        mainPanel.add(textField);
        return mainPanel;
    }
}
