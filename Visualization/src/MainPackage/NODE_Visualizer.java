package MainPackage;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
import TCP.ZIP;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.image.WritableImage;

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

					System.out.println("Begning visualizing");
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
		for (int x = 0; x < classes.length; x++) {
			for (int y = 0; y < classes[x].length; y++) {
				try {
					classes[x][y].draw(cx);
				} catch (Exception e) {
				}
			}
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

		double ClassX = getpreviousX(y, x);
		double ClassY = getpreviousY(y, x);

		DrawableCLass newClass = new DrawableCLass(ClassX, ClassY, umlClass);

		return newClass;

	}

	public double getpreviousY(int y, int x) {

		double width = 0;

		try {

			width = classes[y - 1][x].getbottomY();

		} catch (Exception e) {
			
		}

		return width + standard.padding;
	}

	public double getpreviousX(int y, int x) {

		double height_above = 0;
		double height_left = 0;
 
		try {
			height_above = classes[y - 1][x].getX();
		} catch (Exception e) {
			//: Adds a standard padding to the classes that are a the edge
			height_above = standard.padding;
		}

		try {
			height_left = classes[y][x - 1].getrightX();
		} catch (Exception e) {
			//: Adds a standard padding to the classes that are a the edge
			height_left = standard.padding;
		}

		//: returns the largest x value
		if (height_above > height_left) {
			return height_above;
		} else {
			if(y == 0) {
				return height_left + standard.padding;
			}else {
				return height_left;
			}
			
		}

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
