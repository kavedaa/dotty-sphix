package org.sphix.ui.viewer

import javafx.scene.control.ListView

import no.vedaadata.text.Render

import org.sphix.control.ListUtils
import org.sphix.collection.*

class TextListViewer[A](using render: Render[A]) extends Viewer[Iterable[A]]:
  def apply(x: Iterable[A]) = 
    new ListView[A] with ListUtils[A]:
      setCell(new StringCell(using x => render(x)) {})
      setItems(x.toObservableList)

class ListViewer[A](inner: Viewer[A]) extends Viewer[Iterable[A]]:
  def apply(x: Iterable[A]) = 
    new ListView[A] with ListUtils[A]:
      setCell(new GraphicCell(x => Some(inner(x))) {})
      setItems(x.toObservableList)