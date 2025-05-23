package org.sphix.util

import javafx.scene.*

object NodeUtil:

  /**
    * Finds the first node in the tree of nodes (including this node),
    * that is "focus traversable".
    */
  def firstFocusTraversable(node: Node): Option[Node] =
    if node.isFocusTraversable then Some(node)
    else node match
      case p: Parent => 
        val it = p.getChildrenUnmodifiable.iterator
        while it.hasNext do
          val a = it.next()
          val b = firstFocusTraversable(a)
          if b.isDefined then return b
        None    
      case n =>
        None