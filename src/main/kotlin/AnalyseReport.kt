import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.JBPopupMenu
import java.awt.Dimension
import javax.swing.*


class AnalyseReport : DialogWrapper(true) {

    val txtField = JTextField()
    val panel = JPanel()
    val ChangesScrollPane = JScrollPane()
    val jList1 = JList<String>()
    val ImpactScrollPane = JScrollPane();
    val jList2 = JList<String>()
    val ChangesLabel = JLabel()
    val ImpactLabel = JLabel()

    init {
        init()
        title = "Analyse Result"
    }

    override fun createCenterPanel(): JComponent? {
        panel.preferredSize= Dimension(600,400);

        jList1.setModel(object : AbstractListModel<String?>() {
            var strings =
                arrayOf("Class1 : method1()", "Class2 : method1()", "Class3 : method1()")

            override fun getSize(): Int {
                return strings.size
            }

            override fun getElementAt(i: Int): String {
                return strings[i]
            }
        })
        ChangesScrollPane.setViewportView(jList1)

        jList2.setModel(object : AbstractListModel<String?>() {
            var strings =
                arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

            override fun getSize(): Int {
                return strings.size
            }

            override fun getElementAt(i: Int): String {
                return strings[i]
            }
        })
        //ImpactScrollPane.setViewportView(jList2)

        ChangesLabel.text = "Changes"

        ImpactLabel.text = "Impact"

        val panelLayout = GroupLayout(panel)
        panel.setLayout(panelLayout)
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ChangesLabel)
                                .addComponent(
                                    ChangesScrollPane,
                                    GroupLayout.PREFERRED_SIZE,
                                    187,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addGap(15, 15, 15)
                        .addGroup(
                            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ImpactLabel)
                                .addComponent(
                                    ImpactScrollPane,
                                    GroupLayout.PREFERRED_SIZE,
                                    384,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addContainerGap(11, Short.MAX_VALUE.toInt())
                )
        )
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                        .addGroup(
                            panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(ChangesLabel)
                                .addComponent(ImpactLabel)
                        )
                        .addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED,
                            9,
                            Short.MAX_VALUE.toInt()
                        )
                        .addGroup(
                            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    ImpactScrollPane,
                                    GroupLayout.Alignment.TRAILING,
                                    GroupLayout.PREFERRED_SIZE,
                                    338,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    ChangesScrollPane,
                                    GroupLayout.Alignment.TRAILING,
                                    GroupLayout.PREFERRED_SIZE,
                                    338,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addContainerGap()
                )
        )

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(
                    panel,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE.toInt()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(
                    panel,
                    GroupLayout.DEFAULT_SIZE,
                    GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE.toInt()
                )
        )

        //selection
        jList1.selectionMode = ListSelectionModel.SINGLE_SELECTION

        jList1.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                println(e.firstIndex)
                if(e.firstIndex==0){
                    jList2.setModel(object : AbstractListModel<String?>() {
                        var strings =
                            arrayOf("Class2 : method3() : 41", "Class6 : method2() : 114", "Class4 : method1() : 54")

                        override fun getSize(): Int {
                            return strings.size
                        }

                        override fun getElementAt(i: Int): String {
                            return strings[i]
                        }
                    })
                }else if(e.firstIndex==1){
                    jList2.setModel(object : AbstractListModel<String?>() {
                        var strings =
                            arrayOf("Class1 : method4() : 21", "Class5 : method1() : 85")

                        override fun getSize(): Int {
                            return strings.size
                        }

                        override fun getElementAt(i: Int): String {
                            return strings[i]
                        }
                    })
                }else{
                    jList2.setModel(object : AbstractListModel<String?>() {
                        var strings =
                            arrayOf("Class5 : method2() : 78", "Class1 : method1() : 55", "Class2 : method1() : 12")

                        override fun getSize(): Int {
                            return strings.size
                        }

                        override fun getElementAt(i: Int): String {
                            return strings[i]
                        }
                    })
                }
                ImpactScrollPane.setViewportView(jList2)
            }
        }

        jList2.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                println(e.firstIndex)
                val popup = JBPopupMenu()
                popup.add(JMenuItem("Go to Line"))
                popup.show(jList2,10,10);
            }
        }
        
        return panel
    }
}