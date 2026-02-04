package org.sphix.ui.viewer

import javafx.geometry.*

import no.vedaadata.text.Alignment

extension (alignment: Alignment)

  def toHPos: HPos = alignment match
    case Alignment.Left => HPos.LEFT
    case Alignment.Center => HPos.CENTER
    case Alignment.Right => HPos.RIGHT

  def toPos: Pos = alignment match
    case Alignment.Left => Pos.CENTER_LEFT
    case Alignment.Center => Pos.CENTER
    case Alignment.Right => Pos.CENTER_RIGHT