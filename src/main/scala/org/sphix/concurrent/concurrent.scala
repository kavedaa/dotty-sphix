package org.sphix.concurrent

import javafx.application.Platform

/**
  *   Convenience for `Platform.runLater`.
  *   Will execute the given block of code on the JavaFX application thread (the UI thread).
  */
def fxt[U](block: => U) = Platform.runLater(new Runnable { def run() = block } )

/**
  *   Convenience for `Platform.runLater`.
  *   Will execute the given block of code on the JavaFX application thread (the UI thread).
  */
def runLater[U](block: => U) = Platform.runLater(new Runnable { def run() = block } )