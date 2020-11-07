import com.intellij.openapi.editor.colors.EditorColorsManager;
import model.ReferenceEntity;

import javax.swing.*;
import java.awt.*;

public class ListItemPanel extends javax.swing.JPanel implements ListCellRenderer<ReferenceEntity> {

    private javax.swing.JLabel classLabel;
    private javax.swing.JLabel depthLabel;
    private javax.swing.JLabel methodLabel;
    private final Color textSelectionColor = Color.WHITE;
    private final Color backgroundSelectionColor = new java.awt.Color(38, 118, 191);
    private final Color textNonSelectionColor = EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground();
    private final Color backgroundNonSelectionColor = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
    private Icon iconUp;
    private Icon iconDown;
    private Icon iconClass;
    private Icon iconMethod;
    public ListItemPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        classLabel = new javax.swing.JLabel();
        methodLabel = new javax.swing.JLabel();
        depthLabel = new javax.swing.JLabel();

        iconUp = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/up.png")).getImage());
        iconDown = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/down.png")).getImage());
        iconClass = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/class.png")).getImage());
        iconMethod = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/method.png")).getImage());

        setBackground(backgroundNonSelectionColor);

        classLabel.setText("Class");

        methodLabel.setText("Method");

        depthLabel.setText("Depth");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(classLabel)
                                .addGap(10, 10, 10)
                                .addComponent(methodLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                                .addComponent(depthLabel)
                                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(classLabel)
                                        .addComponent(methodLabel)
                                        .addComponent(depthLabel))
                                .addGap(2, 2, 2))
        );
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ReferenceEntity> list, ReferenceEntity value, int index, boolean isSelected, boolean cellHasFocus) {
        classLabel.setText(value.getPsiClass().getName());
        methodLabel.setText(value.getPsiMethod().getName());
        depthLabel.setText(String.valueOf(value.getDepth()));

        classLabel.setIcon(iconClass);
        methodLabel.setIcon(iconMethod);
        if (value.isCaller()) {
            depthLabel.setIcon(iconUp);
        } else {
            depthLabel.setIcon(iconDown);
        }

        if (cellHasFocus) {
            setBackground(backgroundSelectionColor);
            classLabel.setForeground(textSelectionColor);
            methodLabel.setForeground(textSelectionColor);
            depthLabel.setForeground(textSelectionColor);
        } else {
            setBackground(backgroundNonSelectionColor);
            classLabel.setForeground(textNonSelectionColor);
            methodLabel.setForeground(textNonSelectionColor);
            depthLabel.setForeground(textNonSelectionColor);
        }

        return this;
    }

}
