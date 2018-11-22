package UML_Visualization;

/*
 * 
 *  A class for calculating the width in pixels of text.
 *  
 * 
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MeasureText extends Application {
	
	public static void main(String[] args) {
		launch(args); 
	}
  
	@Override 
	public void start(Stage stage) throws Exception {
		final String msg = "xxxxxxxxxxxxxxx"; //it's a dummy message for demo
		final Text text = new Text(msg);
		Font font = Font.font("Arial", 20);
		text.setFont(font);
		//new Scene(new Group(text));
		
		//text.applyCss(); 

		final double width = text.getLayoutBounds().getWidth(); //the width in pixels of the text is here
		
		//here the original text displays on the canvas
		Canvas canvas = new Canvas(200, 50);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(font);
        gc.fillText(msg, 0, 40);
    
		System.out.print(width);
		/*stage.setScene(new Scene(new Label(Double.toString(width))));
	    stage.show();*/
		stage.setScene(new Scene(new VBox(canvas)));
		stage.show();
			
	}
}


