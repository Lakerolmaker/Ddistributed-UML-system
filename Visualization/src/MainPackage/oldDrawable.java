package MainPackage;

import FileClasses.Method;
import FileClasses.UMLClass;
import FileClasses.Variable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class oldDrawable {

	public UMLClass UMLclass;
	public double width;
	public double height;
	public double boxBottomLine;
	public double boxBottomLine2;
	public Rectangle border1;
	public Rectangle border2;
	public Rectangle border3;

	// : The width and height of the longest text
	private double maxWidth;
	private double maxHeight;

	private StandardValues standard = new StandardValues();

	public DrawableCLass(double x, double y, UMLClass umlclass) {
		super();
		this.UMLclass = umlclass;
		
		//: Get's the max width;
		maxWidth = getMaxWidht();
		maxHeight = getMaxHeight();
		border1 = new Rectangle(x, y, maxWidth, maxHeight);
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
//		drawJesus(cx, border1);
//		drawJesus1(cx, border1);
//	
	}

	private void drawRectangle(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.BLACK);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), maxWidth, maxHeight);

	}

	private void drawJesus(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.BLACK);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), maxWidth, maxHeight );
		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);

		int height = 0;
		for (int i = 0; i < UMLclass.Variables.size(); i++) {

			double attributeX = rect.getX();
			double attributeY = rect.getY() + boxBottomLine + 30 + height;

			String attibuteValue = getAttributeText(UMLclass.Variables.get(i));

			cx.strokeText(attibuteValue, attributeX, attributeY);

			height += 20;
		}
	}
	private void drawJesus1(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.BLACK);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), maxWidth, maxHeight );
		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);

		int height = 0;
		for (int i = 0; i < UMLclass.Methods.size(); i++) {

			double methodX = rect.getX();
			double methodY = rect.getY() + boxBottomLine + boxBottomLine2 + 30 + height;

			String methodValue = getMethodText(UMLclass.Methods.get(i));

			cx.strokeText(methodValue, methodX, methodY);

			
			height += 20;
		}

	}
	
	
	private void drawTitleBox(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.PURPLE);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		
		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);
		boxBottomLine = rect.getHeight() / 4;
// writing in the half wont account for justifying the text position
		double titleX = rect.getX() + ((int) rect.getWidth() / 2);
		double titleY = rect.getY() + ((int) rect.getHeight() / 8);
		cx.strokeText(gettitleText(), titleX, titleY);

	}

	private String gettitleText() {
		return UMLclass.name;
	}

	private void drawAttributeBox(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.PINK);
		
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY() + boxBottomLine, rect.getWidth(), getMaxHeightAttribute());
		
		boxBottomLine2 = (rect.getHeight() - boxBottomLine - 10) / 2;
		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);

		int height = 0;
		for (int i = 0; i < UMLclass.Variables.size(); i++) {

			double attributeX = rect.getX() + 50;
			double attributeY = rect.getY() + boxBottomLine + 30 + height;

			String attibuteValue = getAttributeText(UMLclass.Variables.get(i));

			cx.strokeText(attibuteValue, attributeX, attributeY);

			height += 20;
		}

	}

	private String getAttributeText(Variable var) {
		return "+" + var.name + ":" + var.type;
	}

	private void drawMethodBox(GraphicsContext cx, Rectangle rect) {
		cx.setFill(Color.BLACK);
		cx.setStroke(Color.RED);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY() + boxBottomLine2 + boxBottomLine, maxWidth,
				getMaxHeightMethod());
		cx.setFill(Color.WHITE);

		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);

		int height = 0;
		for (int i = 0; i < UMLclass.Methods.size(); i++) {

			double methodX = rect.getX() + 50;
			double methodY = rect.getY() + boxBottomLine + boxBottomLine2 + 30 + height;

			String methodValue = getMethodText(UMLclass.Methods.get(i));

			cx.strokeText(methodValue, methodX, methodY);

			
			height += 20;
		}

	}

	private String getMethodText(Method var) {
		return "+" + var.name + "():" + var.returnType;
	}

	//: compares all values in the class , to find the largest
	private double getMaxWidht() {
		double currentMax = StandardValues.width;

		double titleWidth = getTextWidth(this.gettitleText());
		if (titleWidth > currentMax) {
			currentMax = titleWidth;
		}

		for (Variable var : UMLclass.Variables) {
			double varWidth = getTextWidth(this.getAttributeText(var));
			if (varWidth > currentMax) {
				currentMax = varWidth;
			}
		}

		for (Method var : UMLclass.Methods) {
			double methodWidth = getTextWidth(this.getMethodText(var));
			if (methodWidth > currentMax) {
				currentMax = methodWidth;
			}
		}
	
		return currentMax;
	}

	private double getTextWidth(String msg) {
		final Text text = new Text(msg);
		text.setFont(StandardValues.font);
		return text.getLayoutBounds().getWidth();
	}


	
	private double getMaxHeightTitle() {
		double currentMax = StandardValues.height;
		double titleHeight = getTextHeight(this.gettitleText());

		
		return boxBottomLine;
	
	}
	
	private double getMaxHeightAttribute() {
		double currentMax = StandardValues.height;
		double i = 0;
		double titleHeight = getTextHeight(this.gettitleText());
		
		for (Variable var : UMLclass.Variables) {
			i++;}
		currentMax = i*titleHeight;
		return  currentMax;
	
	}
	
	private double getMaxHeightMethod() {
		double currentMax = StandardValues.height;
		double i = 0;
		double titleHeight = getTextHeight(this.gettitleText());
		for (Variable var : UMLclass.Variables) {
			i++;}
		currentMax = i*titleHeight;
		return  currentMax;
	}
	
	
	
	private double getMaxHeight() {
		double currentMax = StandardValues.height;
		double i = 0;
		double j = 0;
		double titleHeight = getTextHeight(this.gettitleText());
		if ( titleHeight > currentMax) {
			currentMax = titleHeight;
		}
		
		for (Variable var : UMLclass.Variables) {
			i++;
//			double varHeight = getTextHeight(this.getAttributeText(var));
//			if (i*getTextHeight(this.getAttributeText(var)) > currentMax) {
//				currentMax = i*getTextHeight(this.getAttributeText(var));
			
			}
		
		
		
		for (Method var : UMLclass.Methods) {
			j++;
//			double methodHeight = getTextHeight(this.getMethodText(var));
//			if (j*getTextHeight(this.getMethodText(var)) > currentMax) {
//				currentMax = j*getTextHeight(this.getMethodText(var));
//				System.out.println(methodHeight);
			}
		
		if (i<j) {currentMax = j * titleHeight;}
		else {currentMax = i * titleHeight;}
		System.out.println(currentMax);
		
		return currentMax;
		
		
	}

	
	private double getTextHeight(String msg) {
		final Text text = new Text(msg);
		text.setFont(StandardValues.font);
		return text.getLayoutBounds().getHeight();
	}
	
	
}
