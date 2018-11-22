package MainPackage;

import java.io.File;
import java.util.ArrayList;

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

	ArrayList<String> umlClasses = new ArrayList<String>();
	DrawableCLass[][]  classes;
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        addelements(110);
        
        creatElements();
        
        double Canvas_height = getCanvasHeight();
        Canvas canvas = new Canvas();
        canvas.setHeight(Canvas_height);
        canvas.setWidth(Canvas_height);
       
        GraphicsContext cx = canvas.getGraphicsContext2D();
       
        drawElemements(cx);
       
        saveToImage(canvas);
        
       System.exit(0);
         
    }
    
    public void addelements(int amount) {
    	for (int i = 0; i < amount; i++) {
    		umlClasses.add("sugma");
		}
    }
    

    public double getCanvasWidth() {
    	double maxWidth = 0;
    	for(int y = 0; y < classes.length; y++) {
    		for(int x = 0; x < classes[y].length; x++) {
    			try {
    				double newWidth = classes[y][x].getrightX();
    				if( newWidth > maxWidth) {
    					maxWidth = newWidth;
    				}
				} catch (Exception e) {}
    			
    		}
    	}
    	return maxWidth + StandardValues.canvasPadding_Y;
    }
    
    public double getCanvasHeight() {
    	double maxHeight = 0;
    	for(int y = 0; y < classes.length; y++) {
    		for(int x = 0; x < classes[y].length; x++) {
    			try {
    				double newHeight = classes[y][x].getbottomY();
    				if( newHeight > maxHeight) {
    					maxHeight = newHeight;
    				}
				} catch (Exception e) {}
    			
    		}
    	}
    	return maxHeight + StandardValues.canvasPadding_Y;
    }
    
    public void drawElemements(GraphicsContext cx) {
    	for(int x = 0; x < classes.length; x++) {
    		for(int y = 0; y < classes[x].length; y++) {
    			try {
    				classes[x][y].draw(cx);
				} catch (Exception e) {}
    		}
    	}
    }
    
    public void creatElements() {
    	
    	
    	int sqr = (int) Math.sqrt(umlClasses.size());
    	
    	classes = new DrawableCLass[sqr + 1][sqr + 1];
    	
    	int X = 0;
    	int Y = 0;
    	
    	for(int i = 0; i < umlClasses.size(); i++) {
	 		
    		classes[Y][X] = createClass(Y , X);
    		
    		//: checks for new row.
    		if((i != 0) && ((i + 1) % sqr == 0)) {
    			Y ++;
    			X = 0;
    		}else {
    			//: increses the collum
        		X ++;
    		}
    		
		}
    	
    }
    
    public DrawableCLass createClass(int y , int x) {
    	
    	double ClassX = getpreviousX(y, x);
    	double ClassY = getpreviousY(y, x);
    	
    	ClassX += StandardValues.padding;
    	ClassY += StandardValues.padding;

    	DrawableCLass newClass = new DrawableCLass(ClassX, ClassY);
    	
    	return newClass;
    	
    }
       
    public double getpreviousY(int y , int x) {
    	
    	double width = 0;
    	
    	try {
			
    		width = classes[y - 1][x].getbottomY();
    		
		} catch (Exception e) {}
    	
    	return width;
    }
    
    public double getpreviousX(int y , int x) {
    	
    	double height = 0;
    	
    	try {
			
    		height = classes[y][x - 1].getrightX();
    		
		} catch (Exception e) {}
    	
    	return height;
    	
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

