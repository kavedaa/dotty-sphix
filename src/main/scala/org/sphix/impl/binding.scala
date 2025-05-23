package org.sphix.impl

import javafx.beans.*
import javafx.beans.binding.*

//  use this instead of ObjectBinding#bind()
//  which only works when assigned to a stable value and cannot be chained
//  (likely due to some weak reference being GCed early)
abstract class StrongBinding[A](xs: Iterable[Observable]) extends ObjectBinding[A]: 
  val listener: InvalidationListener = 
    o => invalidate()
  xs.foreach(_.addListener(listener))
