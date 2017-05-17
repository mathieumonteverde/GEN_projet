package com.heigvd.gen.gen_projet.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_MainMenu implements Initializable {

   @FXML
   private void handleLoginAction(ActionEvent event) {
      System.out.println("You clicked me!");

   }

   @Override
   public void initialize(URL url, ResourceBundle rb) {
      // TODO
   }
}
