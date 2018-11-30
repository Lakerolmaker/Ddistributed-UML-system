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

public class DrawableCLass {

	public UMLClass UMLclass;
	public double width;
	public double height;
	public double boxBottomLine;
	public double boxBottomLine2;
	public Rectangle totalBorder;
	public Rectangle attributeBorder;
	public Rectangle titleBorder;
	public Rectangle methodBorder;

	// : The width and height of the longest text
	private double maxWidth;
	private double maxHeight;

	private StandardValues standard = new StandardValues();

	public DrawableCLass(double x, double y, UMLClass umlclass) {
		super();
		this.UMLclass = umlclass;

		// : Get's the max width;
		maxWidth = getMaxWidht();
		maxHeight = getMaxHeightTitle()+getMaxHeightAttribute()+getMaxHeightMethod();
		totalBorder = new Rectangle(x, y, maxWidth, maxHeight);
		createTitleBox();
		createAttributeBox();
		createMethodBox();
		

	}
	
	public void createTitleBox() {
		titleBorder = new Rectangle(totalBorder.getX(), totalBorder.getY(), maxWidth, getMaxHeightTitle());
	}

	public void createAttributeBox() {

		double textheight = getTextHeight("Example Text");
		double attributeX = titleBorder.getX();
		double attributeY = titleBorder.getY() + titleBorder.getHeight();
		double linePadding = 10;

		double totall_height = 0;
		for (int i = 0; i < UMLclass.Variables.size(); i++) {
			totall_height += textheight + linePadding;
		}

		attributeBorder = new Rectangle(attributeX, attributeY, maxWidth, totall_height);

	}
	
	public void createMethodBox() {

		double textheight = getTextHeight("Example Text");
		double attributeX = attributeBorder.getX();
		double attributeY = attributeBorder.getY() + attributeBorder.getHeight();
		double linePadding = 10;

		double totall_height = 0;
		for (int i = 0; i < UMLclass.Methods.size(); i++) {
			totall_height += textheight + linePadding;
		}
		//bit hardcode, total height needs to account for first box
		methodBorder = new Rectangle(attributeX, attributeY, maxWidth, totall_height+titleBorder.getHeight());

	}

	public double getrightX() {
		return totalBorder.getX() + totalBorder.getWidth();
	}

	public double getbottomY() {
		return totalBorder.getY() + totalBorder.getHeight();
	}

	public void draw(GraphicsContext cx) {
		drawTitleBox(cx, titleBorder);
		drawAttributeBox(cx, attributeBorder);
		drawMethodBox(cx, methodBorder);

//	
	}



	private void drawTitleBox(GraphicsContext cx, Rectangle rect) {
		double paddingX = 50;
		double textheight = getTextHeight("Example Text");
		
		cx.setFill(Color.PURPLE);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);

		double titleX = rect.getX() + paddingX;
		double titleY = rect.getY() + textheight;
		
		cx.strokeText(gettitleText(), titleX, titleY);

	}

	private String gettitleText() {
		return UMLclass.name;
	}

	private void drawAttributeBox(GraphicsContext cx, Rectangle rect) {

		double textheight = getTextHeight("Example Text");
		double paddingX = 50;
		double paddingY = 50;
		double attributeX = rect.getX() + paddingX;
		double attributeY = rect.getY() + paddingY;
		double linePadding = 10;

		cx.setFill(Color.PINK);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		boxBottomLine2 = (rect.getHeight() - boxBottomLine - 10) / 2;

		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);
		for (int i = 0; i < UMLclass.Variables.size(); i++) {
			String attibuteValue = getAttributeText(UMLclass.Variables.get(i));
			cx.strokeText(attibuteValue, attributeX, attributeY);
			attributeY += (textheight + linePadding);
		}

	}

	private String getAttributeText(Variable var) {
		return "+" + var.name + ":" + var.type;
	}

	private void drawMethodBox(GraphicsContext cx, Rectangle rect) {
		double textheight = getTextHeight("Example Text");
		double paddingX = 50;
		double paddingY = 50;
		double attributeX = rect.getX() + paddingX;
		double attributeY = rect.getY() + paddingY;
		double linePadding = 10;
		
		cx.setFill(Color.BLACK);
		cx.setLineWidth(2);
		cx.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

		cx.setFont(StandardValues.font);
		cx.setStroke(Color.AQUA);
		for (int i = 0; i < UMLclass.Methods.size(); i++) {
			String attibuteValue = getMethodText(UMLclass.Methods.get(i));
			cx.strokeText(attibuteValue, attributeX, attributeY);
			attributeY += (textheight + linePadding);
		}

	}

	private String getMethodText(Method var) {
		return "+" + var.name + "():" + var.returnType;
	}

	// : compares all values in the class , to find the largest
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
		double titleHeight = getTextHeight(this.gettitleText()) * 2;

		return titleHeight;

	}

	private double getMaxHeightAttribute() {
		double currentMax = StandardValues.height;
		double i = 0;
		double titleHeight = getTextHeight(this.gettitleText());

		for (Variable var : UMLclass.Variables) {
			i++;
		}
		currentMax = i * titleHeight;
		return currentMax;

	}

	private double getMaxHeightMethod() {
		double currentMax = StandardValues.height;
		double i = 0;
		double titleHeight = getTextHeight(this.gettitleText());
		for (Method var : UMLclass.Methods) {
			i++;
		}
		currentMax = i * titleHeight;
		return currentMax;
	}

	

	private double getTextHeight(String msg) {
		final Text text = new Text(msg);
		text.setFont(StandardValues.font);
		return text.getLayoutBounds().getHeight();
	}

}
