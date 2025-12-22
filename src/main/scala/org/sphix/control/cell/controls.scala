package org.sphix.control.cell

import javafx.scene.control.{ Tooltip, ProgressBar, Hyperlink }
import javafx.scene.web.WebView

trait HyperlinkCell[T] extends Cell[T]:
  def action(x: T): Unit
  def isHyperlink(x: T): Boolean = true
//  val hyperlink = new Hyperlink
//  should have been possible to have this here but causes graphic to disappear when e.g. mixed in with image cell, might be bug in JavaFX
  override def onUpdate(x: T) =
    super.onUpdate(x)
    if isHyperlink(x) then
      val hyperlink = new Hyperlink
      hyperlink.setText(getText)
      hyperlink.setGraphic(getGraphic)
      hyperlink.setOnAction(_ => action(x))
      setText(null)
      setGraphic(hyperlink)

trait TooltipCell[T, A] extends Cell[T]:
  def toTooltipText: T => A
  def asTooltipTextOption: A => Option[String]
  def tooltipText(x: T) = asTooltipTextOption(toTooltipText(x))
  lazy val tooltip = new Tooltip
  override def onUpdate(x: T) =
    super.onUpdate(x)
    tooltipText(x) match
      case Some(text) =>
        tooltip.setText(text)
        setTooltip(tooltip)
      case None =>
        setTooltip(null)

trait ProgressBarCell[T] extends DataCell[T, Double]:
  def min: Double
  def max: Double
  lazy val progressBar = new ProgressBar
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    dataValue(x) match 
      case Some(v) =>
        val progress = v / max - min
        progressBar.setProgress(progress)
        setGraphic(progressBar)
      case None =>
        setGraphic(null)

trait WebViewCell[T, A] extends Cell[T]:
  def dataType = DataType.string
  def toUrl: T => A
  def asUrlOption: A => Option[String]
  lazy val webView = new WebView
  override def onUpdate(x: T) =
    super.onUpdate(x)
    asUrlOption(toUrl(x)).foreach { v => 
      webView.getEngine.load(v)
      setGraphic(webView)
    }

  