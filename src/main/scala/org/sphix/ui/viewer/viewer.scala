package org.sphix.ui.viewer

import scala.deriving.Mirror

import javafx.scene.Node
import javafx.scene.control.*

import no.vedaadata.text.Render
import no.vedaadata.text.LabelTransformer

abstract class Viewer[A] extends (A => Node)

object Viewer:

  //  primitive viewers

  given labelTextViewer[A](using render: Render[A]): Viewer[A] = new Viewer[A]:
    def apply(x: A) = new Label(render(x).orNull)

  def readOnlyTextFieldViewer[A](using render: Render[A]): Viewer[A] = new Viewer[A]:
    def apply(x: A) = 
      new TextField:
        setEditable(false)
        setFocusTraversable(false)
        setText(render(x).orNull)

  //  extensions

  extension [A](viewer: Viewer[A])

    def map(f: Node => Node): Viewer[A] = new Viewer[A]:
      def apply(x: A) = f(viewer(x))

    def toDialog(title: String, x: A) = new ViewerDialog(x, Some(title))(using viewer)
    def toDialog(x: A) = new ViewerDialog(x)(using viewer)
    
  //  exports for discoverability

  export org.sphix.ui.viewer.gridProductViewer as gridProduct
  export org.sphix.ui.viewer.alignedGridProductViewer as alignedGridProduct
  export org.sphix.ui.viewer.tableProduct as tableProduct
  export org.sphix.ui.viewer.table as table
  export org.sphix.ui.viewer.HyperlinkViewer as Hyperlink
  export org.sphix.ui.viewer.TextListViewer as TextList
  export org.sphix.ui.viewer.ListViewer as List


