package com.heigvd.gen.gen_projet.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller_Login implements Initializable {

    @FXML
    private Button bttn_login;
    @FXML
    private Button bttn_reg;
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        System.out.println("You clicked me!");

        Stage stage;
        Parent root;
        if(event.getSource()==bttn_login) {

            try {
                stage = (Stage) bttn_login.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("resources.fxml.MainMenu.fxml"));

                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
