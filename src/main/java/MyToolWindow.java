import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import model.MethodEntity;
import model.ReferenceEntity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MyToolWindow {

    private javax.swing.JLabel changesLabel;
    private javax.swing.JScrollPane changesScrollPane;
    private javax.swing.JLabel impactLabel;
    private javax.swing.JScrollPane impactScrollPane;
    private javax.swing.JList<String> changesList;
    private javax.swing.JList<ReferenceEntity> impactList;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSpinner depthSpinner;
    private javax.swing.JButton runBtn;
    private SpinnerNumberModel spinnerNumberModel;
    private javax.swing.JRadioButton currentRB;
    private javax.swing.JRadioButton gitRB;
    private DefaultListModel<ReferenceEntity> listModel;
    private List<MethodEntity> methodList;
    private final ProjectManager projectManager;
    private final Project project;

    public MyToolWindow(ToolWindow toolWindow, Project project) {
        initComponents();
        projectManager = new ProjectManager();
        this.project = project;
    }

    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        changesScrollPane = new javax.swing.JScrollPane();

        impactScrollPane = new javax.swing.JScrollPane();
        impactList = new javax.swing.JList<>();
        changesLabel = new javax.swing.JLabel();
        impactLabel = new javax.swing.JLabel();
        runBtn = new javax.swing.JButton();
        spinnerNumberModel = new SpinnerNumberModel(1, 1, 20, 1);
        depthSpinner = new javax.swing.JSpinner(spinnerNumberModel);
        gitRB = new javax.swing.JRadioButton();
        currentRB = new javax.swing.JRadioButton();

        String[] strings = new String[0];

        changesList = new javax.swing.JList<>(strings);
        changesScrollPane.setViewportView(changesList);

        impactList = new javax.swing.JList<>();
        listModel = new DefaultListModel<>();
        impactList.setModel(listModel);
        impactList.setCellRenderer(new ListItemPanel());
        impactScrollPane.setViewportView(impactList);

        changesLabel.setText("Methods");
        impactLabel.setText("Impact");
        gitRB.setText("Git");
        currentRB.setText("Current");
        runBtn.setText("Run");
        currentRB.setSelected(true);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(changesLabel)
                                        .addComponent(changesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(impactLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                                                .addComponent(currentRB)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(gitRB)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(depthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(runBtn))
                                        .addComponent(impactScrollPane))
                                .addGap(11, 11, 11))
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(impactLabel)
                                        .addComponent(runBtn)
                                        .addComponent(depthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(changesLabel)
                                        .addComponent(gitRB)
                                        .addComponent(currentRB))
                                .addGap(4, 4, 4)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(changesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                                        .addComponent(impactScrollPane))
                                .addGap(11, 11, 11))
        );

        runBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runBtn.setEnabled(false);
                actionRun();
                runBtn.setEnabled(true);
            }
        });

        depthSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Utils.depth = (Integer) depthSpinner.getValue();
            }
        });

        currentRB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (currentRB.isSelected()) {
                    gitRB.setSelected(false);
                    Utils.isCurrent = true;
                } else {
                    gitRB.setSelected(true);
                    Utils.isCurrent = false;
                }
            }
        });

        gitRB.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentRB.setSelected(!gitRB.isSelected());
            }
        });

    }

    public void setChanges(String[] strings) {
        changesList = new javax.swing.JList<>(strings);
        changesScrollPane.setViewportView(changesList);

        changesList.addMouseListener(new ListListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    methodList.get(changesList.getSelectedIndex()).navigate();
                } else if (e.getClickCount() == 1) {
                    createImpactList();
                }
            }
        });
    }

    public void actionRun() {

        methodList = projectManager.getClassEntityList(project);
        ArrayList<String> strings = new ArrayList<String>();
        int i = 0;

        for (MethodEntity method : methodList) {
            strings.add(method.toString());
            i++;
        }

        setChanges(Utils.GetStringArray(strings));
        changesList.setSelectedIndex(0);
        createImpactList();
    }

    public void createImpactList() {
        listModel.clear();

        for (ReferenceEntity reference : methodList.get(changesList.getSelectedIndex()).getImpactSet().getReferences()) {
            listModel.addElement(reference);
        }

        impactScrollPane.setViewportView(impactList);
        impactList.addMouseListener(new ListListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    methodList.get(changesList.getSelectedIndex()).getImpactSet().navigate(impactList.getSelectedIndex());
                }
            }
        });

        impactLabel.setText("Impact(" + methodList.get(changesList.getSelectedIndex()).getImpactSet().getReferencesString().size() + ")");
    }

    public JPanel getContent() {
        return mainPanel;
    }
}