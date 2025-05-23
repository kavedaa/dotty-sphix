package org.sphix.ui.crud

import javafx.scene.image._

trait CrudIcons:

  def Add: Option[Image]
  def Edit: Option[Image]
  def Delete: Option[Image]
  def Open: Option[Image]
  def Save: Option[Image]
  def Refresh: Option[Image]
  def Clear: Option[Image]
  def IsSuccess: Option[Image]
  def IsFailure: Option[Image]

object CrudIcons:

  class Default extends CrudIcons:
    def Add = None
    def Edit = None
    def Delete = None
    def Open = None
    def Save = None
    def Clear = None
    def Refresh = None
    def IsSuccess = None
    def IsFailure = None

  object Default extends Default