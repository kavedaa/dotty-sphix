package org.sphix.control

import scala.compiletime.*
import scala.deriving.*

import javafx.scene.control.*

import no.vedaadata.text.LabelTransformer

import org.sphix.*
import org.sphix.collection.*


class DerivedTable[S <: Product](labels: List[String], cellFactories: List[TableCellFactory[S, Any]], labelTransformer: LabelTransformer) extends TableView[S]:
  val columns = labels.zip(cellFactories).zipWithIndex.map: 
    case ((label, cellFactory), index) =>
      new TableColumn[S, Any]:
        setText(labelTransformer(label))
        setCellValueFactory(_.getValue.productElement(index).toVal)
        setCellFactory(cellFactory)
  getColumns.addAll(columns.toObservableList)

inline def derivedTable[S <: Product](using m: Mirror.ProductOf[S])(using labelTransformer: LabelTransformer): TableView[S] =
  val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
  type CellFactories = Tuple.Map[m.MirroredElemTypes, [X] =>> TableCellFactory[S, X]]
  val cellFactories = summonAll[CellFactories].toList.asInstanceOf[List[TableCellFactory[S, Any]]]
  new DerivedTable[S](labels, cellFactories, labelTransformer)