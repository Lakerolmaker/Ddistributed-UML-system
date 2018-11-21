package MainPackage;

import java.io.File;

import javax.imageio.ImageIO;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.image.WritableImage;

public class canvas extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene s = new Scene(root);

        final Canvas canvas = new Canvas(700,700);
        GraphicsContext cx = canvas.getGraphicsContext2D();
      
       DrawableCLass newclass = new DrawableCLass();
       newclass.draw(cx);
        
      
        saveToImage(canvas);
        
        root.getChildren().add(canvas);
        primaryStage.setScene(s);
        primaryStage.show();
         
    }
    
    
    
    public void saveToImage(Canvas canvas) {
    	
    	int width = (int) canvas.getHeight();
    	int height = (int) canvas.getWidth();
    	
        WritableImage wim = new WritableImage(width , height);

        canvas.snapshot(null, wim);
        
    	File file = new File("CanvasImage.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception s) {
        }
    	
    	
    }


}

