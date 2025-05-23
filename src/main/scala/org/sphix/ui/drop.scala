package org.sphix.ui

import scala.jdk.CollectionConverters.*

import java.io.File

import javafx.scene.Node
import javafx.scene.input.*
import javafx.application.Platform

trait FileDropping:

  def onDrop[U](node: Node, restrictFilesByExtensions: String | List[String] = Nil)(f: List[File] => U) =

    val extensions = restrictFilesByExtensions match
      case s: String => List(s)
      case x: List[?] => x    

    node.setOnDragOver { (event: DragEvent) =>
      if dragFiles(event, extensions).nonEmpty then
        event.acceptTransferModes(TransferMode.COPY)
      event.consume()
    }

    node.setOnDragDropped { (event: DragEvent) =>
      val files = dragFiles(event, extensions)
      event.setDropCompleted(true)
      event.consume()
      Platform.runLater: () =>    //  this seems to be necessary in order to hide the drag icon before the code continues
        f(files)                  //  such as when e.g. displaying a dialog after dropping
    }

  private def dragFiles(event: DragEvent, extensions: List[String]): List[File] = 
    val files = Option(event.getDragboard.getFiles).map(_.asScala.toList).getOrElse(Nil)
    if (extensions.isEmpty) then files
    else files.filter(file => extensions.exists(ext => file.getName.endsWith(ext)))

object FileDropping extends FileDropping