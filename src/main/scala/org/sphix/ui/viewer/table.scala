package org.sphix.ui.viewer

import scala.deriving.Mirror
import scala.compiletime.*

import javafx.scene.control.*

import no.vedaadata.text.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*

inline def tableProduct[P <: Product](using labelTransformer: LabelTransformer)(using m: Mirror.ProductOf[P]): Viewer[P] =
  val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]].map(labelTransformer)
  type RenderTypes = Tuple.Map[m.MirroredElemTypes, Render]
  val renders = summonAll[RenderTypes].toList.asInstanceOf[List[Render[Any]]]
  type AlignTypes = Tuple.Map[m.MirroredElemTypes, Align]
  val aligns = summonAll[AlignTypes].toList.asInstanceOf[List[Align[Any]]]
  new TableProductViewer[P](labels, renders, aligns)

case class Data(label: String, value: Option[String], align: Align[Any])

class TableProductViewer[P <: Product](labels: List[String], renders: List[Render[Any]], aligns: List[Align[Any]]) extends Viewer[P]:
  def apply(x: P) = 
    val elems = x.productIterator.toList
    val values = elems.lazyZip(renders).map:
      (elem, render) => render(elem)
    val datas = labels.lazyZip(values).lazyZip(aligns).map(Data.apply)
    new TableView[Data] with TableUtils[Data]:
      val labelColumn = new Column("Label", _.label.toVal):
        setDefaultCell()
      val valueColumn = new Column("Value", _.toVal):
        setCell:
          new StringCell(using _.value)
          with AlignmentCell(x => Some(x.align.alignment.toPos))
      getColumns.addAll(labelColumn, valueColumn)  
      setItems(datas.toObservableList)

inline def table[P <: Product](using m: Mirror.ProductOf[P])(using labelTransformer: LabelTransformer): Viewer[Iterable[P]] =
  val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
  type CellFactories = Tuple.Map[m.MirroredElemTypes, [X] =>> TableCellFactory[P, X]]
  val cellFactories = summonAll[CellFactories].toList.asInstanceOf[List[TableCellFactory[P, Any]]]
  new TableViewer[P](labels, cellFactories, labelTransformer)      

class TableViewer[P <: Product](labels: List[String], cellFactories: List[TableCellFactory[P, Any]], labelTransformer: LabelTransformer) 
  extends Viewer[Iterable[P]]:
    def apply(x: Iterable[P]) = new TableView[P]:
      val columns = labels.zip(cellFactories).zipWithIndex.map: 
        case ((label, cellFactory), index) =>
          new TableColumn[P, Any]:
            setText(labelTransformer(label))
            setCellValueFactory(_.getValue.productElement(index).toVal)
            setCellFactory(cellFactory)
      getColumns.addAll(columns.toObservableList)
      setItems(x.toObservableList)