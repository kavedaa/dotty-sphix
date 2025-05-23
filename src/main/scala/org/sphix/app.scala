package org.sphix

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.Parent

import com.sun.javafx.css.StyleManager

abstract class SimpleApp(title: Option[String]) extends Application:

  Thread.currentThread.setUncaughtExceptionHandler: (thread: Thread, throwable: Throwable) =>
    throwable.getStackTrace.foreach(println)

  def this(title: String) = this(Some(title))
  def this() = this(None)

  def root: Parent

  def stylesheet: Option[String] = None

  def start(stage: Stage) =
    val scene = new Scene(root)
    title foreach stage.setTitle
    stage.setScene(scene)
    stage.show()
    stylesheet foreach StyleManager.getInstance.addUserAgentStylesheet


abstract class SimpleApp2(title: Option[String]) extends Application:

  Thread.currentThread.setUncaughtExceptionHandler: (thread: Thread, throwable: Throwable) =>
    throwable.getStackTrace.foreach(println)

  def this(title: String) = this(Some(title))
  def this() = this(None)

  def root(stage: Stage): Parent

  def stylesheet: Option[String] = None

  def start(stage: Stage) =
    val scene = new Scene(root(stage))
    title foreach stage.setTitle
    stage.setScene(scene)
    stage.show()
    stylesheet foreach StyleManager.getInstance.addUserAgentStylesheet
