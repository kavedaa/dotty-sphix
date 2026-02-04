package org.sphix.ui.viewer

import scala.deriving.Mirror
import scala.compiletime.*

import javafx.scene.control.Label
import javafx.scene.layout.GridPane 

import no.vedaadata.text.*

inline def gridProductViewer[P <: Product](using labelTransformer: LabelTransformer)(using m: Mirror.ProductOf[P]): Viewer[P] =
  val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]].map(labelTransformer)  
  type ElemTypes = Tuple.Map[m.MirroredElemTypes, Viewer]
  val elemViewers = summonAll[ElemTypes].toList.asInstanceOf[List[Viewer[Any]]]
  new AlignedGridProductViewer[P](labels, elemViewers, aligns = labels.map(_ => None))

inline def alignedGridProductViewer[P <: Product](using labelTransformer: LabelTransformer)(using m: Mirror.ProductOf[P]): Viewer[P] =
  val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]].map(labelTransformer)
  type ElemTypes = Tuple.Map[m.MirroredElemTypes, Viewer]
  val elemViewers = summonAll[ElemTypes].toList.asInstanceOf[List[Viewer[Any]]]
  type AlignTypes = Tuple.Map[m.MirroredElemTypes, Align]
  val aligns = summonAll[AlignTypes].toList.asInstanceOf[List[Align[Any]]]
  new AlignedGridProductViewer[P](labels, elemViewers, aligns.map(Some(_)))

class AlignedGridProductViewer[P <: Product](labels: List[String], elemViewers: List[Viewer[Any]], aligns: List[Option[Align[Any]]]) extends Viewer[P]:
  def apply(x: P) =
    val elems = x.productIterator.toList
    val grid = new GridPane:
      setHgap(10)
      setVgap(5)
    (labels.zip(elemViewers).zip(aligns)).zipWithIndex.foreach:
      case (((label, viewer), align), row) =>
        val labelNode = new Label(label)
        val viewNode = viewer(elems(row))
        grid.add(labelNode, 0, row)
        grid.add(viewNode, 1, row)
        align.foreach: x =>
          GridPane.setHalignment(viewNode, x.alignment.toHPos)
    grid