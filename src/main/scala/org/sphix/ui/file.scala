package org.sphix.ui

import scala.jdk.CollectionConverters.*

import java.io.File
import java.nio.file.Path

import javafx.stage.*

import org.sphix.*

trait FileChoosing:

  val initialPath = Var[Option[Path]](None)

  protected def getInitialPath: Option[Path] = 
    initialPath()

  protected def setInitialPath(path: Path): Unit =
    initialPath() = Some(path)

  def withFileForOpen[A](description: String, extensions: String*)(f: File => A): Option[A] =
    withFileForOpen(description -> extensions)(f)

  def withFileForOpen[A](fileTypes: (String, Seq[String])*)(f: File => A): Option[A] =
    Option(getFileChooser(fileTypes).showOpenDialog(null)) map { file =>
      setInitialPath(file.getParentFile.toPath)
      f(file)
    }

  def withMultipleFilesForOpen[A](description: String, extensions: String*)(f: Seq[File] => A): Option[A] =
    withMultipleFilesForOpen(description -> extensions)(f)

  def withMultipleFilesForOpen[A](fileTypes: (String, Seq[String])*)(f: Seq[File] => A): Option[A] =
    Option(getFileChooser(fileTypes).showOpenMultipleDialog(null)) map { files =>
      val fileList = files.asScala.toSeq
      fileList.headOption foreach { file => setInitialPath(file.getParentFile.toPath) }
      f(fileList)
    }

  def withFileForSave[A](description: String, extension: String, initialFilename: Option[String] = None)(f: File => A): Option[A] =
    Option(getFileChooser(Seq(description -> Seq(extension)), initialFilename).showSaveDialog(null)) map { file =>
      setInitialPath(file.getParentFile.toPath)
      f(ensureExtension(file, extension))
    }

  def withPath[A](op: Path => A): Option[A] =
    val directoryChooser = new DirectoryChooser
    getInitialPath foreach { path =>
      val pathFile = path.toFile
      if pathFile.exists() then directoryChooser.setInitialDirectory(pathFile)
    }
    Option(directoryChooser.showDialog(null)) map { file =>
      setInitialPath(file.toPath)
      op(file.toPath)
    }

  private def getFileChooser(fileTypes: Seq[(String, Seq[String])], initialFilename: Option[String] = None) = 

    val fileChooser = new FileChooser

    val filters = fileTypes map { case (description, extensions) =>
      val extensionsWithFallback = if extensions.isEmpty then List("*") else extensions
      val extensionFormats = extensionsWithFallback.map(ext => s"*.$ext")
      new FileChooser.ExtensionFilter(description, extensionFormats*)
    }
    
    fileChooser.getExtensionFilters.addAll(filters*)

    getInitialPath foreach { path =>
      val pathFile = path.toFile
      if pathFile.exists() then fileChooser.setInitialDirectory(pathFile)
    }

    initialFilename foreach fileChooser.setInitialFileName

    fileChooser

  private def ensureExtension(file: File, extension: String): File =
    if (!(file.getName endsWith ("." + extension))) new File(file.getAbsolutePath + extension) else file

end FileChoosing

object FileChoosing extends FileChoosing