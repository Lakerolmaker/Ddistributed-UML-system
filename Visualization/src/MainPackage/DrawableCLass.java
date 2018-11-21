package MainPackage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class DrawableCLass {

	public UMLClass UMLclass;
	public double width;
	public double height;
	public Rectangle border1;
	public Rectangle border2;
	public Rectangle border3;
	

	private StandardValues standard = new StandardValues();
	
	public DrawableCLass() {
		super();
		border1 = new Rectangle(10 , 10, standard.width, standard.height);
		
	}

	
	public void draw(GraphicsContext cx) {
		drawRectangle(cx, border1);
	}
	
	private void drawRectangle(GraphicsContext cx,Rectangle rect){
        cx.setFill(Color.BLANCHEDALMOND);
        cx.setStroke(Color.RED);
        cx.setLineWidth(2);
        cx.fillRect(rect.getX(),      
                     rect.getY(), 
                     rect.getWidth(), 
                     rect.getHeight());
        cx.setStroke(Color.BLUE);
    }
	
	
	
}
