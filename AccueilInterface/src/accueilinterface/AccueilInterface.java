/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accueilinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;

/**
 *
 * @author Chaymae
 */
public class AccueilInterface extends Application {

    @Override
    public void start(Stage primaryStage) {

        StackPane root = new StackPane();
        /*
        Button close = new Button(" Close Game ! ");
        close.setShape(new Circle(10));
        

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });

        root.getChildren().add(close);*/
        primaryStage.setTitle(" Motorcycles RACING !!!! ");
       // Group root = new Group();

        Scene scene = new Scene(root, 900, 700, Color.ANTIQUEWHITE);
        scene.setFill(Color.ANTIQUEWHITE);

        Button quit = new Button("Quit Game ! ");

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
            }
        });

        root.getChildren().add(quit);

        Text title = new Text();
        title.setX(10.0f);
        title.setY(140.0f);
        title.setCache(true);
        title.setText(" Always choose the FASTEST route ! ");
        title.setFill(Color.RED);
        title.setFont(Font.font(null, FontWeight.BOLD, 36));
        title.setEffect(new GaussianBlur());
        root.setAlignment(title,Pos.TOP_CENTER);
        root.setAlignment(quit, Pos.BOTTOM_CENTER);
        
        root.getChildren().add(title);
        
        
        Button lancer = new Button("Ready to play ? Join a Waiting room ! ");
        root.getChildren().add(lancer);
        root.setAlignment(lancer, Pos.CENTER_RIGHT);

        
        Button login = new Button("LOGIN ");
        root.getChildren().add(login);
        root.setAlignment(login, Pos.CENTER);
        
        
        Button signIN = new Button("SIGN IN ");
        root.getChildren().add(signIN);
        root.setAlignment(signIN, Pos.CENTER_LEFT);
        
        
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
