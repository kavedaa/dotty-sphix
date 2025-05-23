package org.sphix.ui.editor

import javafx.scene.control.*
import javafx.scene.input._

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq

// trait ResolverTexts:
//   def item: String
//   def candidate: String

// class ResolverEditorFactory[A, B](items: List[A], itemCandidates: Map[A, List[B]])(renderA: A => String, renderB: B => String)(using texts: ResolverTexts)
//   extends EditorFactory[Map[A, B]]:
//     def createEditor = new ResolverEditor(items, itemCandidates)(renderA, renderB)

// class ResolverEditor[A, B](items: List[A], itemCandidates: Map[A, List[B]])(renderA: A => String, renderB: B => String)(using texts: ResolverTexts) 
//   extends Editor[Map[A, B]]:

//   type C = Container.Primitive[Map[A, B]]

//   val resolverItems = 
//     items
//       .map: item =>
//         ResolverItem(item, itemCandidates.get(item).getOrElse(Nil))
//       .to(ObservableSeq)      

//   val table = new ResolverTable[A, B](renderA, renderB)

//   table.setItems(resolverItems)

//   val status = Val(Status.Valid)  // TODO

//   def container(label: Option[String]) = Container.Primitive(this, label, table)

//   def set(x: Map[A, B]) = x.foreach: (item, candidate) =>
//     resolverItems.find(_.item == item).foreach: resolverItem =>
//       resolverItem.selectItemModel.selected() = Some(candidate)

//   def clear() = resolverItems.foreach: item =>
//     item.selectItemModel.selected() = None

//   val value = resolverItems: xs =>
//     val keyValues = xs.flatMap(_.toKeyValue)
//     Value.Valid(keyValues.toMap)


// case class SelectItemModel[A](selected0: Option[A], items: ObservableSeq[A]):
//   val selected = Var(selected0)

// case class ResolverItem[A, B](item: A, candidates: List[B]):
//   val selectItemModel = SelectItemModel[B](None, candidates.to(ObservableSeq))
//   def toKeyValue = selectItemModel.selected().map(item -> _)


// class ResolverTable[A, B](renderA: A => String, renderB: B => String)(using texts: ResolverTexts)
//   extends TableView[ResolverItem[A, B]]
//   with TableUtils[ResolverItem[A, B]]:

//   val item = new Column(texts.item, x => renderA(x.item).toVal)

//   val candidate = new Column(texts.candidate, _.selectItemModel.toVal):
//     setCell(ResolverTableCell[ResolverItem[A, B], B](renderB))

//   getColumns.addAll(item, candidate)

//   setEditable(true)

// object ResolverTableCell:
//   def apply[S, A](render0: A => String) = new ResolverTableCell[S, A]:
//     def render = render0
  

// trait ResolverTableCell[S, A] extends TableCell[S, SelectItemModel[A]]:
//   cell =>

//   def render: A => String

//   this.getStyleClass().add("combo-box-table-cell");

//   private lazy val comboBox = new ComboBox[A] with ComboBoxUtils[A]:

//     setCell(TextCell(render))
//     setButtonCell(TextCell(render))

//     setMaxWidth(java.lang.Double.MAX_VALUE);

//     getSelectionModel.selectedItemProperty.onValue: v =>
//       Option(v).foreach: item =>
//         cell.getItem.selected() = Some(item)
//         commitEdit(getItem)

//     setOnKeyPressed((t: KeyEvent) =>
//       if new KeyCodeCombination(KeyCode.ESCAPE).`match`(t) then
//         cell.cancelEdit()
//     )

//   override def startEdit() =
//     if isEditable && getTableView.isEditable && getTableColumn.isEditable then   
//       comboBox setItems cell.getItem.items
//       getItem.selected().foreach(comboBox.getSelectionModel.select) //	important that this comes before super.startEdit()
//       super.startEdit()
//       setText(null)
//       setGraphic(comboBox)
//       //      comboBox show ()	//	this is wanted for good UX but triggers weird bug 
//       comboBox.requestFocus()

//   override def cancelEdit() = 
//     super.cancelEdit()
//     setText(getItem.selected().map(render).getOrElse(""))
//     setGraphic(null)

//   override def updateItem(item: SelectItemModel[A], empty: Boolean) = 
//     super.updateItem(item, empty)
//     if (!empty) then
//       if (isEditing) then
//         getItem.selected().foreach(comboBox.getSelectionModel.select)
//         setText(null)
//         setGraphic(comboBox)
//       else 
//         setText(getItem.selected().map(render).getOrElse(""))
//         setGraphic(null)
//     else 
//       setText(null)
//       setGraphic(null)
