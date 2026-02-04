package org.sphix.ui.viewer

import javafx.scene.control.Hyperlink

import no.vedaadata.text.Render

class HyperlinkViewer[A](action: A => Unit)(using render: Render[A]) extends Viewer[A]:
  def apply(x: A) =
    val link = new Hyperlink(render(x).orNull)
    link.setOnAction(_ => action(x))
    link