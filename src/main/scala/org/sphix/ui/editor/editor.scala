package org.sphix.ui.editor

import org.sphix.*

trait Editor[A]: 
  type C <: Container
  def get: A
  def status: Val[Status]
  def value: Val[Value[A]]
  def set(x: A): Unit
  def clear(): Unit
  def container(label: Option[String]): Container

object Editor:

  def apply[A](using editorFactory: EditorFactory[A]) = editorFactory.createEditor

  extension [A] (self: Editor[A]) 
    def transform[B](f: A => B)(g: B => A) = 
      new Editor[B]:
        type C = self.C
        def get = f(self.get)
        def value = self.value.map(v => v.map(f))
        def set(x: B) = self.set(g(x))
        export self.status, self.clear, self.container

