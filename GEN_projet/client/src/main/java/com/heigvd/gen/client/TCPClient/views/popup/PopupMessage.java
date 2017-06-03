/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient.views.popup;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author mathieu
 */
public class PopupMessage extends Dialog {

   public PopupMessage(String message, String buttonText) {

      // Set the title
      setTitle(message);

      // Set button
      ButtonType foo = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
      getDialogPane().getButtonTypes().addAll(foo, ButtonType.CANCEL);
      // Set text field
      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(20, 150, 10, 10));

      // Set prompt invite
      Label prompt = new Label(message);

      // Add elements
      grid.add(prompt, 0, 0);

      // Add to the dialog
      getDialogPane().setContent(grid);
   }
}
