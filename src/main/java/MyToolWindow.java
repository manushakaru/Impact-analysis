import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import model.MethodEntity;
import model.ReferenceEntity;
import org.jetbrains.annotations.NotNull;

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
    private javax.swing.JList<ReferenceEntity> jList2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSpinner jSpinnerDepth;
    private javax.swing.JButton jButtonRun;
    private SpinnerNumberModel spinnerNumberModel;
    private javax.swing.JRadioButton jRBCurrent;
    private javax.swing.JRadioButton jRBGit;

    private DefaultListModel<ReferenceEntity> listModel;
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
        spinnerNumberModel=new SpinnerNumberModel(1,1,20,1);
        jSpinnerDepth = new javax.swing.JSpinner(spinnerNumberModel);
        jRBGit = new javax.swing.JRadioButton();
        jRBCurrent = new javax.swing.JRadioButton();

        String[] strings = new String[0];
        //ReferenceEntity[] references = new ReferenceEntity[0];

        jList1 = new javax.swing.JList<>(strings);
        ChangesScrollPane.setViewportView(jList1);

        jList2 = new javax.swing.JList<>();
        listModel=new DefaultListModel<>();
        jList2.setModel(listModel);
        jList2.setCellRenderer(new ListItemPanel());
        ImpactScrollPane.setViewportView(jList2);

        ChangesLabel.setText("Changes");
        ImpactLabel.setText("Impact");
        jRBGit.setText("Git");
        jRBCurrent.setText("Current");
        jButtonRun.setText("Run");
        jRBCurrent.setSelected(true);
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
                                .addGap(11, 11, 11)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ChangesLabel)
                                        .addComponent(ChangesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(ImpactLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                                                .addComponent(jRBCurrent)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jRBGit)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jSpinnerDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonRun))
                                        .addComponent(ImpactScrollPane))
                                .addGap(11, 11, 11))
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ImpactLabel)
                                        .addComponent(jButtonRun)
                                        .addComponent(jSpinnerDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ChangesLabel)
                                        .addComponent(jRBGit)
                                        .addComponent(jRBCurrent))
                                .addGap(4, 4, 4)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ChangesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                                        .addComponent(ImpactScrollPane))
                                .addGap(11, 11, 11))
        );

        jSpinnerDepth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utils.depth=(Integer)jSpinnerDepth.getValue();
            }
        });

        jRBCurrent.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(jRBCurrent.isSelected()){
                    jRBGit.setSelected(false);
                    Utils.isCurrent=true;
                }else{
                    jRBGit.setSelected(true);
                    Utils.isCurrent=false;
                }
            }
        });

        jRBGit.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(jRBGit.isSelected()){
                    jRBCurrent.setSelected(false);
                }else{
                    jRBCurrent.setSelected(true);
                }
            }
        });

    }

    public void setChanges(String[] strings){
        jList1 = new javax.swing.JList<>(strings);
        ChangesScrollPane.setViewportView(jList1);

        /*jList1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) {
                    //jList2 = new javax.swing.JList<ReferenceEntity>();//methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferences()
                    //DefaultListModel<ReferenceEntity>
                    listModel.clear();

                    for (ReferenceEntity reference:methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferences()) {
                        listModel.addElement(reference);
                    }

                    ImpactScrollPane.setViewportView(jList2);
                    jList2.addMouseListener(new ListListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JList list = (JList)e.getSource();
                            if (e.getClickCount() == 2) {
                                methodList.get(jList1.getSelectedIndex()).getImpactSet().navigate(jList2.getSelectedIndex());
                            }
                        }
                    });

                    ImpactLabel.setText("Impact("+methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferencesString().size()+")");
                }
            }
        });*/
        jList1.addMouseListener(new ListListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    methodList.get(jList1.getSelectedIndex()).navigate();
                }else if(e.getClickCount() == 1) {
                    listModel.clear();

                    for (ReferenceEntity reference:methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferences()) {
                        listModel.addElement(reference);

                    }

                    ImpactScrollPane.setViewportView(jList2);
                    jList2.addMouseListener(new ListListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            JList list = (JList)e.getSource();
                            if (e.getClickCount() == 2) {
                                methodList.get(jList1.getSelectedIndex()).getImpactSet().navigate(jList2.getSelectedIndex());
                            }
                        }
                    });

                    ImpactLabel.setText("Impact("+methodList.get(jList1.getSelectedIndex()).getImpactSet().getReferencesString().size()+")");
                }
            }
        });
    }

    public void actionRun() {
        /*ProgressManager.getInstance().run(new Task.Backgroundable(project,"Searching") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {

            }
        });*/

        methodList = projectManager.getClassEntityList(project);

        ArrayList<String> strings=new ArrayList<String>();
        int i=0;
        for (MethodEntity method : methodList) {
            strings.add(method.toString());
            i++;
        }

        setChanges(Utils.GetStringArray(strings));
        ImpactLabel.setText("Impact");
    }

    public JPanel getContent() {
        return mainPanel;
    }
}