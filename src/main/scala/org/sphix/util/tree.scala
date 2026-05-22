package org.sphix.util

import scala.jdk.CollectionConverters.*

import org.sphix.*
import javafx.scene.control.*

case class Tree[A](value: A, children: List[Tree[A]] = Nil)

case class TreeList[A](trees: List[Tree[A]])

object TreeList:

  def toTreeItem[A, B](treeList: TreeList[A], f: A => B, root: B): TreeItem[B] = 
    def toChildItem(tree: Tree[A]): TreeItem[B] =
      new TreeItem(f(tree.value)):
        getChildren.addAll(tree.children.map(toChildItem).asJava)
    new TreeItem(root):
      getChildren.addAll(treeList.trees.map(toChildItem).asJava)

object SelectableTree:

  sealed trait SelectableItem[+A](val label: String):
    val isSelectedAndLabel: Var[(Boolean, String)] = Var((false, label))

  object SelectableItem:
    case class WithValue[A](value: A, label0: String) extends SelectableItem[A](label0)
    case class WithoutValue(label0: String) extends SelectableItem[Nothing](label0)

  def fromTreeList[A](treeList: TreeList[A], f: A => String, rootLabel: String): TreeItem[SelectableItem[A]] = 
    def toSelectableItem(x: A) = SelectableItem.WithValue(x, f(x))
    TreeList.toTreeItem(treeList, toSelectableItem, SelectableItem.WithoutValue(rootLabel))
