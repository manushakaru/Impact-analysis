import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import model.MethodEntity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class MyToolWindow {
    private javax.swing.JLabel ChangesLabel;
    private javax.swing.JScrollPane ChangesScrollPane;
    private javax.swing.JLabel ImpactLabel;
    private javax.swing.JScrollPane ImpactScrollPane;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSpinner jSpinnerDepth;
    private javax.swing.JButton jButtonRun;
    private SpinnerNumberModel spinnerNumberModel;

    private List<MethodEntity> methodList;
    private ProjectManager projectManager;
    private Project project;

    public MyToolWindow(ToolWindow toolWindow,Project project) {
        initComponents();
        projectManager=new ProjectManager();
        this.project=project;
    }

    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        ChangesScrollPane = new javax.swing.JScrollPane();

        ImpactScrollPane = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        ChangesLabel = new javax.swing.JLabel();
        ImpactLabel = new javax.swing.JLabel();
        jButtonRun = new javax.swing.JButton();
        spinnerNumberModel=new SpinnerNumberModel(1,1,5,1);
        jSpinnerDepth = new javax.swing.JSpinner(spinnerNumberModel);

        String[] strings = new String[0];
        jList1 = new javax.swing.JList<>(strings);

        ChangesScrollPane.setViewportView(jList1);

        jList2 = new javax.swing.JList<>(strings);
        ImpactScrollPane.setViewportView(jList2);

        ChangesLabel.setText("Changes");
        ImpactLabel.setText("Impact");
        jButtonRun.setText("Run");
        jButtonRun.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionRun();
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ChangesLabel)
                                        .addComponent(ChangesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                                .addGap(15, 15, 15)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(ImpactScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                                .addGap(11, 11, 11))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(ImpactLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jSpinnerDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonRun)
                                                .addContainerGap())))
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ImpactLabel)
                                        .addComponent(jButtonRun)
                                        .addComponent(jSpinnerDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ChangesLabel))
                                .addGap(4, 4, 4)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ImpactScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                                        .addComponent(ChangesScrollPane, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );

        jSpinnerDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utils.depth=(Integer)jSpinnerDepth.getValue();
            }
        });

    }

    public void setChanges(String[] strings){
        jList1 = new javax.swing.JList<>(strings);
        ChangesScrollPane.setViewportView(jList1);

        jList1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) {
                    jList2 = new javax.swing.JList<>(Utils.GetStringArray(methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferencesString()));
                    ImpactScrollPane.setViewportView(jList2);
                    jList2.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JList list = (JList)e.getSource();
                            if (e.getClickCount() == 2) {
                                methodList.get(jList1.getSelectedIndex()).getImpactSet().navigate(jList2.getSelectedIndex());
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                }
            }
        });
    }

    public void actionRun() {
        methodList = projectManager.getClassEntityList(project);

        ArrayList<String> strings=new ArrayList<String>();
        int i=0;
        for (MethodEntity method : methodList) {
            strings.add(method.toString());
            i++;
        }
        setChanges(Utils.GetStringArray(strings));
    }

    public JPanel getContent() {
        return mainPanel;
    }
}