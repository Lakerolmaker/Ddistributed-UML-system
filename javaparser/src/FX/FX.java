package FX;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

import Main.main;
 
public class FX extends Application {
    public static void main(String[] args) {
        launch(args);
        
        
        
    }
    
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Window");
        Button btn = new Button();
        btn.setText("btn");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("abc");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}