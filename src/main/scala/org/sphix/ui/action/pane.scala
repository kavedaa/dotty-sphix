package org.sphix.ui.action

import scala.language.implicitConversions
import scala.jdk.CollectionConverters.*

import javafx.scene.Node
import javafx.scene.layout.* 
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.scene.image.*

import org.controlsfx.control.action.*

import org.sphix.*
import org.sphix.collection.*
import org.sphix.control.Spring

trait ActionHandler[A]:
  def handle(): Unit

object ActionHandler:
  def apply[A](f: => Unit): ActionHandler[A] = 
    new ActionHandler[A]:
      def handle() = f

trait ActionType

object ActionType:

  opaque type Add = ActionType
  opaque type Edit = ActionType
  opaque type Delete = ActionType
  opaque type Open = ActionType
  opaque type Save = ActionType
  opaque type Refresh = ActionType
  opaque type Clear = ActionType

object ActionPane:

  trait Operations:
    def pane: Node
    def table: Node
    def actions: List[Action] = Nil
    def contextActions: List[Action] = Nil
    def toolbarBaseItems: List[Node] = actions.map(ActionUtils.createButton)
    def toolbarAuxItems: List[Node] = Nil
    def init(): Unit = {}

  trait Add(f: ActionHandler[ActionType.Add])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val addAction = 
      new Action(texts.Add + "...", _ => f.handle()):
        icons.Add.foreach(x => setGraphic(new ImageView(x)))

    override def actions = super.actions :+ addAction

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.INSERT)
        if combination.`match`(keyEvent) then f.handle())

  end Add

  trait Edit(f: ActionHandler[ActionType.Edit])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val editAction = 
      new Action(texts.Edit + "...", _ => f.handle()):
        icons.Edit.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ editAction 

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN)
        if combination.`match`(keyEvent) then f.handle())
      table.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent: MouseEvent) =>
        if (mouseEvent.isAltDown) && (mouseEvent.getClickCount == 2) then f.handle())

  end Edit

  trait Delete(f: ActionHandler[ActionType.Delete])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val deleteAction = 
      new Action(texts.Delete, _ => f.handle()):
        icons.Delete.foreach(x => setGraphic(new ImageView(x)))

    override def contextActions = super.contextActions :+ deleteAction

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.DELETE)
        if combination.`match`(keyEvent) then f.handle())

  end Delete

  trait Open(f: ActionHandler[ActionType.Open])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val openAction = 
      new Action(texts.Open, _ => f.handle()):
        icons.Open.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ openAction 

    override def init() =
      super.init()
      table.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.ENTER)
        if combination.`match`(keyEvent) then f.handle())
      table.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent: MouseEvent) =>
        if mouseEvent.getClickCount == 2 then f.handle())

  end Open

  trait Save(f: ActionHandler[ActionType.Save])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val saveAction = 
      new Action(texts.Save, _ => f.handle()):
        icons.Save.foreach(x => setGraphic(new ImageView(x)))       

    override def contextActions = super.contextActions :+ saveAction 

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)
        if combination.`match`(keyEvent) then f.handle())

  end Save

  trait Refresh(f: ActionHandler[ActionType.Refresh])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val refreshAction = 
      new Action(texts.Refresh, _ => f.handle()):
        icons.Refresh.foreach(x => setGraphic(new ImageView(x)))

    override def toolbarAuxItems = super.toolbarAuxItems :+ ActionUtils.createButton(refreshAction)

    override def init() =
      super.init()
      pane.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent: KeyEvent) =>    
        val combination = new KeyCodeCombination(KeyCode.F5)
        if combination.`match`(keyEvent) then f.handle())

  end Refresh

  trait Clear(f: ActionHandler[ActionType.Clear])(using texts: ActionTexts, icons: ActionIcons) extends Operations:

    val clearAction = 
      new Action(texts.Clear, _ => f.handle()):
        icons.Clear.foreach(x => setGraphic(new ImageView(x)))

    override def toolbarAuxItems = super.toolbarAuxItems :+ ActionUtils.createButton(clearAction)

  end Clear


abstract class TableActionPane[A](using ActionTexts, ActionIcons)
  extends BorderPane
  with ActionPane.Operations:

  def pane = this
  def table: TableView[A]
  def content: Node

  lazy val selectedItems: ObservableSeq[A] = table.getSelectionModel.getSelectedItems

  /**
    * This allows to have a single selection property that can be used for both single and multiple selection tables. 
    * If the table is in single selection mode, the selected item, if any, will be returned as an `Option`. 
    * If the table is in multiple selection mode, `None` will be returned, since there is no single selected item.
    */
  lazy val actionItem: Val[Option[A]] = selectedItems.asVal.map: xs =>
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
