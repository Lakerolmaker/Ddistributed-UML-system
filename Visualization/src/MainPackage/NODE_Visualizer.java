package MainPackage;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
import TCP.ZIP;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NODE_Visualizer extends Application {

	public static String project_name = "UMLFromJava";
	public static ArrayList<UMLClass> umlClasses = new ArrayList<UMLClass>();
	public static DrawableCLass[][] classes;
	StandardValues standard = new StandardValues();
	public static TCP tcp = new TCP();
	ZIP zip = new ZIP();

	public static String[] systemArgs;

	public static void main(String[] args) throws Exception {
		systemArgs = args;

		tcp.server.initializeServer();
		tcp.server.start(new RunnableArg<String>() {

			@Override
			public void run() {

				String raw_data = this.getArg();
				Gson javaParser = new Gson();
				TCP_data data = javaParser.fromJson(raw_data, TCP_data.class);

				if (data.metaData.equals("Parsed data")) {

					System.out.println("Recived parsed data");

					UMLPackage project = javaParser.fromJson(data.data, UMLPackage.class);

					project_name = project.name;

					umlClasses = getClasses(project);

					System.out.println("Beginning visualizing");
					Lanchprogram();

				}
			}

		});

		tcp.server.post.addPostParamter("master_node", "true");
		tcp.server.addToNetwork("visualizer");

	}

	public static void Lanchprogram() {
		launch(systemArgs);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		creatElements();

		double Canvas_height = getCanvasHeight();
		double Canvas_width = getCanvasWidth();

		if (Canvas_height > Canvas_width) {
			Canvas_width = Canvas_height;
		} else {
			Canvas_height = Canvas_width;
		}

		Canvas canvas = new Canvas();
		canvas.setHeight(Canvas_height);
		canvas.setWidth(Canvas_width);

		GraphicsContext cx = canvas.getGraphicsContext2D();

		drawElemements(cx);

		File uml_picture = saveToImage(canvas);

		System.out.println("Visualizing comeplete");

		tcp.client.connectTNetwork("client");
		tcp.client.sendFile(uml_picture);

		System.out.println("Picture sent to clients");

		uml_picture.delete();
		System.out.println("Cleanup comeplete");
	}

	public static ArrayList<UMLClass> getClasses(UMLPackage inputPackage) {

		ArrayList<UMLClass> newPackage = new ArrayList<UMLClass>();

		if (!inputPackage.classes.isEmpty()) {
			newPackage.addAll(inputPackage.classes);
		}

		if (!inputPackage.Packages.isEmpty()) {
			for (int i = 0; i < inputPackage.Packages.size(); i++) {
				newPackage.addAll(getClasses(inputPackage.Packages.get(i)));
			}
		}

		return newPackage;
	}

	public double getCanvasWidth() {
		double maxWidth = 0;
		for (int y = 0; y < classes.length; y++) {
			for (int x = 0; x < classes[y].length; x++) {
				try {
					double newWidth = classes[y][x].getrightX();
					if (newWidth > maxWidth) {
						maxWidth = newWidth;
					}
				} catch (Exception e) {
				}

			}
		}
		return maxWidth + standard.canvasPadding_Y;
	}

	public double getCanvasHeight() {
		double maxHeight = 0;
		for (int y = 0; y < classes.length; y++) {
			for (int x = 0; x < classes[y].length; x++) {
				try {
					double newHeight = classes[y][x].getbottomY();
					if (newHeight > maxHeight) {
						maxHeight = newHeight;
					}
				} catch (Exception e) {
				}

			}
		}
		return maxHeight + standard.canvasPadding_Y;
	}

	public void drawElemements(GraphicsContext cx) {
		for (int y = 0; y < classes.length; y++) {
			for (int x = 0; x < classes[y].length; x++) {
				try {
					classes[y][x].draw(cx);
					drawArrows(cx, x, y);
				} catch (Exception e) {
				}
			}
		}
	}

	private void drawArrows(GraphicsContext cx, int pointA_X, int pointA_Y) {

		for (String id : classes[pointA_Y][pointA_X].UMLclass.composistion) {

			for (int y = 0; y < classes.length; y++) {
				for (int x = 0; x < classes[y].length; x++) {
					DrawableCLass newclass = classes[y][x];
					String name = newclass.getName();
					if (name.equals(id)) {
						System.out.println("Arrow from : " + classes[pointA_Y][pointA_X].getName() + " -> " + name);
						drawArrow(cx, pointA_X, pointA_Y, x, y);
					}
				}
			}

		}

	}

	private double X;
	private double Y;
	private double targetX;
	private double targetY;

	private void drawArrow(GraphicsContext cx, int pointA_X, int pointA_Y, int pointB_X, int pointB_Y) {

		DrawableCLass classA = classes[pointA_Y][pointA_X];
		DrawableCLass classB = classes[pointB_Y][pointB_X];

		cx.setStroke(Color.PURPLE);
		cx.setLineWidth(standard.arrowWidth);

		// check if it above or on the same column
		if (pointB_Y <= pointA_Y) {

			X = classA.getX() + (classA.getWidth() / 2);
			Y = classA.getY();

			cx.beginPath();
			cx.moveTo(X, Y);

			// : draw first line
			Y -= (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();

		} else {

			X = classA.getX() + (classA.getWidth() / 2);
			Y = classA.getY() + classA.getHeight();

			cx.beginPath();
			cx.moveTo(X, Y);

			// : draw first line
			Y += (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();

		}

		// : sets the target X and Y
		// : ( below or at the same level , connects to the above of the target)
		if (pointA_Y <= pointB_Y) {
			targetX = classB.getX() + (classB.getWidth() / 2);
			targetY = classB.getY();
		} else {
			targetX = classB.getX() + (classB.getWidth() / 2);
			targetY = classB.getbottomY();
		}

		// : EDGE CASE target is right below or under
		if ((pointA_X == pointB_X) && ((pointB_Y == (pointA_Y + 1)) || (pointB_Y == (pointA_Y - 1)))) {
			finalConnection(cx);
		} else {

			// : To the edge of the original element
			if (pointB_X >= pointA_X) {
				X = classA.getrightX() + (standard.padding / 2);
				cx.lineTo(X, Y);
				cx.stroke();
			} else {
				X = classA.getX() - (standard.padding / 2);
				cx.lineTo(X, Y);
				cx.stroke();
			}

			travelHorizontaly(cx, pointA_X, pointB_X, classB);
			travelVerticaly(cx, pointA_Y, pointB_Y, classB);

			finalConnection(cx);
		}

		cx.closePath();

	}

	public void finalConnection(GraphicsContext cx) {
		// : Final connection
		cx.lineTo(targetX, Y);
		cx.stroke();
		cx.lineTo(targetX, targetY);
		cx.stroke();
	}

	public void travelVerticaly(GraphicsContext cx, int pointA_Y, int pointB_Y, DrawableCLass elem) {

		if (pointB_Y < pointA_Y) {
			Y = elem.getbottomY() + (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();
		} else {
			Y = elem.getY() - (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();
		}

	}

	public void travelHorizontaly(GraphicsContext cx, int pointA_X, int pointB_X, DrawableCLass elem) {

		if (pointB_X > pointA_X) {
			X = elem.getX() - (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();
		} else {
			X = elem.getrightX() + (standard.padding / 2);
			cx.lineTo(X, Y);
			cx.stroke();
		}

	}

	public void creatElements() {

		int sqr = (int) Math.sqrt(umlClasses.size());

		classes = new DrawableCLass[sqr + 1][sqr + 1];

		int X = 0;
		int Y = 0;

		for (int i = 0; i < umlClasses.size(); i++) {

			classes[Y][X] = createClass(Y, X, umlClasses.get(i));

			// : checks for new row.
			if (X == sqr) {
				Y++;
				X = 0;
			} else {
				// : increses the collum
				X++;
			}

		}

	}

	public DrawableCLass createClass(int y, int x, UMLClass umlClass) {

		double ClassX = getpreviousX(x);
		double ClassY = getpreviousY(y);

		DrawableCLass newClass = new DrawableCLass(ClassX, ClassY, umlClass);

		return newClass;

	}

	public double getpreviousY(int lineY) {

		double maxY = 0;

		for (int x = 0; x < classes[lineY].length; x++) {
			try {
				double curY = classes[lineY - 1][x].getY() + classes[lineY - 1][x].getHeight();
				if (curY > maxY) {
					maxY = curY;
				}
			} catch (Exception e) {

			}
		}

		return maxY + standard.padding;
	}

	public double getpreviousX(int lineX) {

		double maxX = 0;

		for (int y = 0; y < classes[lineX].length; y++) {
			try {
				double curX = classes[y][lineX - 1].getX() + classes[y][lineX - 1].getWidth();
				if (curX > maxX) {
					maxX = curX;
				}
			} catch (Exception e) {

			}
		}

		return maxX + standard.padding;

	}

	public File saveToImage(Canvas canvas) {

		int width = (int) canvas.getHeight();
		int height = (int) canvas.getWidth();

		WritableImage wim = new WritableImage(width, height);

		canvas.snapshot(null, wim);

		String imagename = project_name + ".png";

		File file = new File(imagename);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
		} catch (Exception s) {
		}
		return file;
	}

}
