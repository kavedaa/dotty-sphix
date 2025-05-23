package org.sphix

import javafx.beans.binding.ObjectBinding
import javafx.beans.Observable

// for backwards compat.

abstract class Func[A](dependencies0: Observable*) extends ObjectBinding[A]:
  def dependencies: Seq[Observable] = dependencies0
  bind(dependencies*)
  def compute: A
  def computeValue(): A = compute
