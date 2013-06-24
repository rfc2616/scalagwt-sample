/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package com.google.gwt.sample.showcase.client.content.lists

import com.google.gwt.core.client.GWT
import com.google.gwt.core.client.RunAsyncCallback
import com.google.gwt.i18n.client.Constants
import com.google.gwt.sample.showcase.client.ContentWidget
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseData
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseSource
import com.google.gwt.sample.showcase.client.ShowcaseAnnotations.ShowcaseStyle
import com.google.gwt.user.client.Command
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.rpc.AsyncCallback
import com.google.gwt.user.client.ui.MenuBar
import com.google.gwt.user.client.ui.MenuItem
import com.google.gwt.user.client.ui.Widget

/**
 * Example file.
 */
object CwMenuBar {
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  trait CwConstants extends Constants with ContentWidget.CwConstants {
    def cwMenuBarHelpCategory: String

    def cwMenuBarFileOptions: Array[String]

    def cwMenuBarGWTOptions: Array[String]

    def cwMenuBarPrompts: Array[String]

    def cwMenuBarName: String

    def cwMenuBarHelpOptions: Array[String]

    def cwMenuBarEditCategory: String

    def cwMenuBarFileCategory: String

    def cwMenuBarFileRecents: Array[String]

    def cwMenuBarDescription: String

    def cwMenuBarEditOptions: Array[String]
  }
}

@ShowcaseStyle(Array(
    ".gwt-MenuBar", ".gwt-MenuBarPopup", "html>body .gwt-MenuBarPopup",
    "* html .gwt-MenuBarPopup"))
class CwMenuBar(@ShowcaseData private val constants: CwMenuBar.CwConstants)
      extends ContentWidget(constants) {

  def getDescription: String = constants.cwMenuBarDescription
  def getName: String = constants.cwMenuBarName

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  def onInitialize: Widget = {
    // Create a command that will execute on menu item selection
    val menuCommand = new Command {
      private final val phrases = constants.cwMenuBarPrompts
      private var curPhrase = 0

      def execute: Unit = {
        Window.alert(phrases(curPhrase))
          curPhrase = (curPhrase + 1) % phrases.length
        }
      }

    // Create a menu bar
    val menu = new MenuBar
    menu.setAutoOpen(true)
    menu.setWidth("500px")
    menu.setAnimationEnabled(true)

    // Create a sub menu of recent documents
    val recentDocsMenu = new MenuBar(true)
    val recentDocs = constants.cwMenuBarFileRecents
    recentDocs foreach { recentDocsMenu.addItem(_, menuCommand) }

    // Create the file menu
    val fileMenu = new MenuBar(true)
    fileMenu.setAnimationEnabled(true)
    menu.addItem(new MenuItem(constants.cwMenuBarFileCategory, fileMenu))
    val fileOptions = constants.cwMenuBarFileOptions
    fileOptions.view.zipWithIndex foreach { pair =>
      val (fileOption, i) = pair;
      if (i == 3) {
        fileMenu.addSeparator
        fileMenu.addItem(fileOption, recentDocsMenu)
        fileMenu.addSeparator
      } else {
        fileMenu.addItem(fileOption, menuCommand)
      }
    }

    // Create the edit menu
    val editMenu = new MenuBar(true)
    menu.addItem(new MenuItem(constants.cwMenuBarEditCategory, editMenu))
    val editOptions = constants.cwMenuBarEditOptions
    editOptions foreach { editMenu.addItem(_, menuCommand) }

    // Create the GWT menu
    val gwtMenu = new MenuBar(true)
    menu.addItem(new MenuItem("GWT", true, gwtMenu))
    val gwtOptions = constants.cwMenuBarGWTOptions
    gwtOptions foreach { gwtMenu.addItem(_, menuCommand) }

    // Create the help menu
    val helpMenu = new MenuBar(true)
    menu.addSeparator
    menu.addItem(new MenuItem(constants.cwMenuBarHelpCategory, helpMenu))
    val helpOptions = constants.cwMenuBarHelpOptions
    helpOptions foreach { helpMenu.addItem(_, menuCommand) }

    // Return the menu
    menu.ensureDebugId("cwMenuBar")
    menu
  }

  protected[client] def asyncOnInitialize(callback: AsyncCallback[Widget]): Unit = {
    GWT.runAsync(new RunAsyncCallback {
      def onFailure(caught: Throwable): Unit = {
        callback.onFailure(caught)
      }

      def onSuccess: Unit = {
        callback.onSuccess(onInitialize)
      }
   })
  }
}

