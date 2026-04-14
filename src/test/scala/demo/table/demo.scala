package demo.table

import scala.concurrent.ExecutionContext.Implicits.global

import javafx.stage.*
import javafx.application.Application
import javafx.scene.control.*
import javafx.scene.layout.*

import org.sphix.*
import org.sphix.ui.*
import org.sphix.ui.dialog.EditorDialog
import org.sphix.excel.*
import org.sphix.concurrent.FutureModal

@main def main() = Application.launch(classOf[TableDemoApp])


class TableDemoBase[A](val table: TableView[A])(using Window):

  val pane = new BorderPane

  val excelButton = new Button("Excel...")

  val toolbar = new ToolBar(excelButton)

  table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)
  table.getSelectionModel.setCellSelectionEnabled(true)

  pane.setTop(toolbar)
  pane.setCenter(table)

  def exportWithOptions(options: TableExcel.Options): Unit =
    FileChoosing.withFileForSave("Excel", "xlsx"): file =>
      FutureModal("Exporting..."):
        TableExcel.writeFile(file, table, options)
      .onComplete: res =>
        Responding.respondWith(x => s"Exported to $x")(res)

  excelButton.setOnAction: _ =>
    TableExcel.optionsDialog(table).showAndWait().ifPresent: options =>
      exportWithOptions(options)

class TableDemoApp extends SimpleApp2("Table demos"):

  def root(stage: Stage) =

    given Window = stage

    val defaultsDemoTab = new Tab("Default cells"):
      val defaultsDemo = new demo.table.DefaultsDemo
      setContent(defaultsDemo.pane)
      setClosable(false)

    val booleanDemoTab = new Tab("Boolean"):
      val booleanDemo = new demo.table.primitive.boolean.BooleanDemo
      setContent(booleanDemo.pane)
      setClosable(false)

    val bigDecimalDemoTab = new Tab("BigDecimal"):
      val bigDecimalDemo = new demo.table.primitive.bigdecimal.BigDecimalDemo
      setContent(bigDecimalDemo.pane)
      setClosable(false)

    val graphicDemoTab = new Tab("Graphics cells"):
      val graphicDemo = new demo.table.graphic.GraphicDemo
      setContent(graphicDemo.pane)
      setClosable(false)

    val editingDemoTab = new Tab("Editing"):
      setContent(new demo.table.editing.EditingDemo)
      setClosable(false)

    val controlsDemoTab = new Tab("Controls"):
      setContent(controls.ControlsDemo)
      setClosable(false)

    val mixinDemoTab = new Tab("Mixin"):
      setContent(mixin.MixinDemo)
      setClosable(false)

    val multiDemoTab = new Tab("Multi"):
      val multiDemo = new demo.table.multi.MultiDemo
      setContent(multiDemo.pane)
      setClosable(false)

    val headerDemoTab = new Tab("Header"):
      setContent(HeaderDemo)
      setClosable(false)

    new TabPane(defaultsDemoTab, booleanDemoTab, bigDecimalDemoTab, graphicDemoTab, editingDemoTab, controlsDemoTab, mixinDemoTab, multiDemoTab, headerDemoTab)