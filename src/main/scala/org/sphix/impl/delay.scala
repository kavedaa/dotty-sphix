package org.sphix.impl

import scala.concurrent.duration.Duration

import java.util.concurrent.*

import javafx.beans.InvalidationListener
import javafx.beans.value.*

import javafx.application.Platform.runLater

import org.sphix.*

class DelayedVal[A](source: ObservableValue[A], duration: Duration) 
  extends LazyVal[A]:

  def compute = source.getValue

  val THREAD_FACTORY = new ThreadFactory:
    def newThread(runnable: Runnable) =
      val thread = new Thread(runnable)
      thread.setPriority(Thread.MIN_PRIORITY)
      thread.setDaemon(true)
      thread

  val executor = new ScheduledThreadPoolExecutor(1, THREAD_FACTORY)
  var future: Option[Future[_]] = None

  val invalidator: Runnable = 
    () =>
      runLater { () =>
        invalidate(source)
      }

  source observe {
    future.foreach(_ cancel false)
    future = Some(executor.schedule(invalidator, duration.toNanos, TimeUnit.NANOSECONDS))
  }

