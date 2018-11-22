package MainPackage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class DrawableCLass {

	public UMLClass UMLclass;
	public double width;
	public double height;
	public double boxBottomLine;
	public double boxBottomLine2;
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
		drawTitleBox(cx, border1);
		drawAttributeBox(cx, border1);
		drawMethodBox(cx, border1);
	}
	
	private void drawRectangle(GraphicsContext cx,Rectangle rect){
        cx.setFill(Color.BLACK);
        cx.setStroke(Color.RED);
        cx.setLineWidth(2);
        cx.fillRect(rect.getX(),      
                     rect.getY(), 
                     rect.getWidth(), 
                     rect.getHeight());
        cx.setStroke(Color.BLUE);



	}
    private void drawTitleBox(GraphicsContext cx, Rectangle rect) {
        cx.setFill(Color.BLACK);
        cx.setStroke(Color.RED);
        cx.setLineWidth(2);
        cx.fillRect(rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight()/4);
        cx.setFill(Color.WHITE);
        cx.fillRect(rect.getX()+5,
                rect.getY()+5,
                rect.getWidth()-10,
                (rect.getHeight()/4)-10);
        cx.setStroke(Color.BLUE);
        boxBottomLine = rect.getHeight()/4;

    }
    private void drawAttributeBox(GraphicsContext cx, Rectangle rect) {
        cx.setFill(Color.BLACK);
        cx.setStroke(Color.RED);
        cx.setLineWidth(2);
        cx.fillRect(rect.getX(),
                rect.getY()+boxBottomLine,
                rect.getWidth(),
                (rect.getHeight()-boxBottomLine)/2);
        cx.setFill(Color.WHITE);

        cx.fillRect(rect.getX()+5,
                rect.getY()+boxBottomLine+5,
                rect.getWidth()-10,
                (rect.getHeight()-boxBottomLine-10)/2);
        boxBottomLine2 = (rect.getHeight()-boxBottomLine-10)/2;

        cx.setStroke(Color.BLUE);

    }

    private void drawMethodBox(GraphicsContext cx, Rectangle rect) {
        cx.setFill(Color.BLACK);
        cx.setStroke(Color.RED);
        cx.setLineWidth(2);
        cx.fillRect(rect.getX(),
                rect.getY()+boxBottomLine2+boxBottomLine,
                rect.getWidth(),
                (rect.getHeight()-boxBottomLine2)/2);
        cx.setFill(Color.WHITE);

        cx.fillRect(rect.getX()+5,
                rect.getY()+boxBottomLine2+boxBottomLine+5,
                rect.getWidth()-10,
                (rect.getHeight()-boxBottomLine-10)/2);

        cx.setStroke(Color.BLUE);

    }




}
