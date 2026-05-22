package org.sphix.ui.crud

import scala.language.implicitConversions
import scala.jdk.CollectionConverters._

import javafx.scene.Node
import javafx.scene.layout._
import javafx.scene.control._
import javafx.scene.input._
import javafx.scene.image._

import org.controlsfx.control.action._

import org.sphix.*
import org.sphix.collection._
import org.sphix.control.Spring

object Crud:

  type Op = () => Unit
  
object CrudPane:

  trait Operations:
    def pane: Node
    def table: Node
    def actions: List[Action] = Nil
    def contextActions: List[Action] = Nil
    def toolbarBaseItems: List[Node] = actions.map(ActionUtils.createButton)
    def toolbarAuxItems: List[Node] = Nil
    def init(): Unit = {}

  trait Add(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val addAction = 
      new Action(texts.Add + "...", _ => f()):
        icons.Add.foreach(x => setGraphic(new ImageView(x)))

    override def actions = super.actions :+ addAction

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.INSERT)
        if combination.`match`(keyEvent) then f())
  end Add

  trait Edit(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val editAction = 
      new Action(texts.Edit + "...", _ => f()):
        icons.Edit.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ editAction 

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN)
        if combination.`match`(keyEvent) then f())
      table.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent: MouseEvent) =>
        if (mouseEvent.isAltDown) && (mouseEvent.getClickCount == 2) then f())
  end Edit

  trait Delete(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val deleteAction = 
      new Action(texts.Delete, _ => f()):
        icons.Delete.foreach(x => setGraphic(new ImageView(x)))

    override def contextActions = super.contextActions :+ deleteAction

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.DELETE)
        if combination.`match`(keyEvent) then f())
  end Delete

  trait Open(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val openAction = 
      new Action(texts.Open, _ => f()):
        icons.Open.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ openAction 

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.ENTER)
        if combination.`match`(keyEvent) then f())
      table.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent: MouseEvent) =>
        if mouseEvent.getClickCount == 2 then f())
  end Open

  trait Save(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val saveAction = 
      new Action(texts.Save, _ => f()):
        icons.Save.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ saveAction 

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)
        if combination.`match`(keyEvent) then f())
  end Save

  trait Refresh(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val refreshAction = 
      new Action(texts.Refresh, _ => f()):
        icons.Refresh.foreach(x => setGraphic(new ImageView(x)))

    override def toolbarAuxItems = super.toolbarAuxItems :+ ActionUtils.createButton(refreshAction)

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.F5)
        if combination.`match`(keyEvent) then f())
  end Refresh

  trait Clear(f: Crud.Op)(using texts: CrudTexts, icons: CrudIcons) extends Operations:

    val clearAction = 
      new Action(texts.Clear, _ => f()):
        icons.Clear.foreach(x => setGraphic(new ImageView(x)))

    override def toolbarAuxItems = super.toolbarAuxItems :+ ActionUtils.createButton(clearAction)

  end Clear


abstract class TableCrudPane[A](using CrudTexts, CrudIcons)
  extends BorderPane
  with CrudPane.Operations:

  def pane = this
  def table: TableView[A]
  def content: Node

  lazy val selectedItems: ObservableSeq[A] = table.getSelectionModel.getSelectedItems

  /**
    * This allows to have a single selection property that can be used for both single and multiple selection tables. 
    * If the table is in single selection mode, the selected item, if any, will be returned as an `Option`. 
    * If the table is in multiple selection mode, `None` will be returned, since there is no single selected item.
    */
  lazy val selectedItem: Val[Option[A]] = selectedItems.asVal.map: xs =>
    if xs.size == 1 then xs.headOption else None

  override def init() =
    super.init()

    val toolbar = ActionUtils.createToolBar(contextActions.asJava, ActionUtils.ActionTextBehavior.SHOW)

    toolbar.getItems.addAll(0, (toolbarBaseItems :+ new Separator).asJava)
    toolbar.getItems.addAll((new Spring :: toolbarAuxItems).asJava)
   
    val contextMenu = ActionUtils.createContextMenu(contextActions.asJava)

    table.setContextMenu(contextMenu)

    contextActions.foreach: action =>
      action.disabledProperty <== table.getSelectionModel.selectedIndexProperty.map: index =>
        index == -1

    setTop(toolbar)
    setCenter(content)



//  legacy

sealed trait CrudOperation
object CrudOperation {
  object Add extends CrudOperation
  object Edit extends CrudOperation
  object Delete extends CrudOperation
}


abstract class CrudPane(
  operations: Seq[CrudOperation],
  add: () => Unit,
  edit: () => Unit,
  delete: () => Unit)
  (implicit texts: CrudTexts,
  icons: CrudIcons)
  extends BorderPane {

  def table: TableView[?]
  def content: Node

  val addAction = new Action(texts.Add + "...", _ => add()) { icons.Add.foreach(x => setGraphic(new ImageView(x))) }
  val editAction = new Action(texts.Edit + "...", _ => edit()) { icons.Edit.foreach(x => setGraphic(new ImageView(x))) }
  val deleteAction = new Action(texts.Delete, _ => delete()) { icons.Delete.foreach(x => setGraphic(new ImageView(x))) }

  def toolbarPreItems: List[Node] = if (operations.contains(CrudOperation.Add)) List(ActionUtils.createButton(addAction)) else Nil

  def contextActions = List(
    Option.when(operations contains CrudOperation.Edit)(editAction),
    Option.when(operations contains CrudOperation.Delete)(deleteAction)
  ).flatten

  def toolbarPostItems: List[Node] = Nil

  def init() = {

    val toolbar = ActionUtils.createToolBar(contextActions.asJava, ActionUtils.ActionTextBehavior.SHOW)

    toolbar.getItems.addAll(0, (toolbarPreItems :+ new Separator).asJava)
    toolbar.getItems.addAll((new Spring :: toolbarPostItems).asJava)

    addEventHandler(KeyEvent.KEY_PRESSED, { (keyEvent: KeyEvent) =>    
      val addCombination = new KeyCodeCombination(KeyCode.INSERT)
      if (addCombination `match` keyEvent) add()
    })
    
    table.addEventHandler(KeyEvent.KEY_PRESSED, { (keyEvent: KeyEvent) =>    
      val editCombination = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN)
      val deleteCombination = new KeyCodeCombination(KeyCode.DELETE)
      if (editCombination `match` keyEvent) edit()
      if (deleteCombination `match` keyEvent) delete()
    })

    table.addEventHandler(MouseEvent.MOUSE_CLICKED, { (mouseEvent: MouseEvent) =>
      if (mouseEvent.getClickCount == 2) edit()
    })

    val contextActionsSeq = contextActions.to(ObservableSeq)

    val contextMenu = ActionUtils.createContextMenu(contextActionsSeq.asJava)

    table.setContextMenu(contextMenu)

    contextActions foreach { action =>
      action.disabledProperty <== table.getSelectionModel.selectedIndexProperty map { index =>
        index == -1
      }
    }

    setTop(toolbar)
    setCenter(content)
  }

}