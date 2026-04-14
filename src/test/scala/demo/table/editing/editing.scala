package demo.table.editing

import javafx.stage.Window
import javafx.scene.control.*

class EditingDemo(using Window) extends TabPane:

  val generalDemoTab = new Tab("General"):
    val generalDemo = new demo.table.editing.general.GeneralDemo
    setContent(generalDemo.pane)
    setClosable(false)

  val textFieldDemoTab = new Tab("Textfield"):
    val textFieldDemo = new demo.table.editing.textfield.TextFieldDemo
    setContent(textFieldDemo.pane)
    setClosable(false)

  val comboBoxDemoTab = new Tab("ComboBox"):
    val comboBoxDemo = new demo.table.editing.combobox.ComboBoxDemo
    setContent(comboBoxDemo.pane)
    setClosable(false)

  getTabs.addAll(generalDemoTab, textFieldDemoTab, comboBoxDemoTab)

end EditingDemo