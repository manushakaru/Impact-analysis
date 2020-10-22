import model.ReferenceEntity;

import javax.swing.*;
import java.awt.*;

public class ListItemPanel extends javax.swing.JPanel implements ListCellRenderer<ReferenceEntity> {

    public ListItemPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        ClassLabel = new javax.swing.JLabel();
        MethodLabel = new javax.swing.JLabel();
        DepthLabel = new javax.swing.JLabel();

        iconUp=new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/up.png")).getImage());
        iconDown=new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/down.png")).getImage());
        iconClass=new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/class.png")).getImage());
        iconMethod=new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("drawables/method.png")).getImage());

        setBackground(backgroundNonSelectionColor);

        ClassLabel.setText("Class");

        MethodLabel.setText("Method");

        DepthLabel.setText("Depth");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(ClassLabel)
                                .addGap(10, 10, 10)
                                .addComponent(MethodLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                                .addComponent(DepthLabel)
                                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ClassLabel)
                                        .addComponent(MethodLabel)
                                        .addComponent(DepthLabel))
                                .addGap(2, 2, 2))
        );
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ReferenceEntity> list, ReferenceEntity value, int index, boolean isSelected, boolean cellHasFocus) {
        ClassLabel.setText(value.getPsiClass().getName());
        MethodLabel.setText(value.getPsiMethod().getName());
        DepthLabel.setText(String.valueOf(value.getDepth()));

        ClassLabel.setIcon(iconClass);
        MethodLabel.setIcon(iconMethod);
        if(value.isCaller()) {
            DepthLabel.setIcon(iconUp);
        }else {
            DepthLabel.setIcon(iconDown);
        }

        if (cellHasFocus) {
            setBackground(backgroundSelectionColor);
            ClassLabel.setForeground(textSelectionColor);
            MethodLabel.setForeground(textSelectionColor);
            DepthLabel.setForeground(textSelectionColor);
        } else {
            setBackground(backgroundNonSelectionColor);
            ClassLabel.setForeground(textNonSelectionColor);
            MethodLabel.setForeground(textNonSelectionColor);
            DepthLabel.setForeground(textNonSelectionColor);
        }

        return this;
    }

    private javax.swing.JLabel ClassLabel;
    private javax.swing.JLabel DepthLabel;
    private javax.swing.JLabel MethodLabel;
    private Color textSelectionColor = Color.WHITE;
    private Color backgroundSelectionColor = new java.awt.Color(38,118,191);
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;
    private Icon iconUp;
    private Icon iconDown;
    private Icon iconClass;
    private Icon iconMethod;

}
