/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heigvd.gen.client.TCPClient.views.popup;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author mathieu
 */
public class PopupWindow extends Stage {

   public PopupWindow(Stage primaryStage, String text) {
      super();
      
      final Stage dialog = new Stage();
      initModality(Modality.APPLICATION_MODAL);
      initOwner(primaryStage);
      
      VBox dialogVbox = new VBox(20);
      dialogVbox.getChildren().add(new Text(text));
      Scene dialogScene = new Scene(dialogVbox, 300, 200);
      
      dialog.setScene(dialogScene);
      dialog.show();
   }
}
