package MainPackage;

import FileClasses.Method;
import FileClasses.UMLClass;
import FileClasses.Variable;
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
	
	public DrawableCLass(double x , double y, UMLClass umlclass) {
		super();
		this.UMLclass = umlclass;
		border1 = new Rectangle(x , y, standard.width, standard.height);
	}
	
	public double getrightX() {
		return border1.getX() + border1.getWidth();
	}
	
	public double getbottomY() {
		return border1.getY() + border1.getHeight();
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
		
        double titleX = rect.getX() + ((int) rect.getWidth()/2);
        double titleY = rect.getY() + ((int) rect.getHeight()/8);
        
		cx.strokeText( UMLclass.name , titleX,  titleY);

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
		
		int height = 0;
		for(int i = 0; i < UMLclass.Variables.size(); i++) {
			
			double attributeX = rect.getX() + 50;
			double attributeY = rect.getY() + boxBottomLine + 30 + height;
			
			Variable var = UMLclass.Variables.get(i);
			String attibuteValue = "+" + var.name + ":" + var.type;
			
			cx.strokeText(attibuteValue, attributeX, attributeY );
			
        	height += 20;
		}
		
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
		
		int height = 0;
		for(int i = 0; i < UMLclass.Methods.size(); i++) {
			
			double methodX = rect.getX() + 50;
			double methodY = rect.getY() + boxBottomLine + boxBottomLine2 + 30 + height;
			
			Method var = UMLclass.Methods.get(i);
			String methodValue = "+" + var.name + "():" + var.returnType;
			
        	cx.strokeText(methodValue, methodX , methodY );
        	
        	height += 20;
        	
        }

    }

}
