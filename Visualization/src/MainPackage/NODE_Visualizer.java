package MainPackage;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import FileClasses.Progress;
import FileClasses.Relationship;
import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
import TCP.ZIP;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class NODE_Visualizer extends Application {

	public static String project_name = "UMLFromJava";
	public static ArrayList<UMLClass> umlClasses = new ArrayList<UMLClass>();
	public static ArrayList<Relationship> rellationships = new ArrayList<Relationship>();
	public static DrawableCLass[][] classes;

	public static TCP tcp = new TCP();
	public static TCP progress_tcp = new TCP();

	StandardValues standard = new StandardValues();
	public static Gson gson = new Gson();
	ZIP zip = new ZIP();

	public double offsetX = 0;
	public double offsetY = 0;

	double Canvas_height;
	double Canvas_width;


	public void cleanup() {
		Canvas_height = 0;
		Canvas_width = 0;
		offsetX = 0;
		offsetY = 0;
		project_name = "UMLFromJava";
		umlClasses = new ArrayList<UMLClass>();
		rellationships = new ArrayList<Relationship>();
		classes = null;

		X = 0;
		Y = 0;
		targetX = 0;
		targetY = 0;
	}

	public static void main(String[] args) throws Exception {

		NODE_Visualizer visualizer = new NODE_Visualizer();

		tcp.server.initializeServer();
		tcp.server.start(new RunnableArg<String>() {

			@Override
			public void run() {

				String raw_data = this.getArg();
				TCP_data data = gson.fromJson(raw_data, TCP_data.class);

				if (data.metaData.equals("Parsed data")) {

					System.out.println("Recived parsed data");

					UMLPackage project = gson.fromJson(data.data, UMLPackage.class);

					project_name = project.name;

					umlClasses = getClasses(project);
					rellationships = getRellationships(project);

					System.out.println("Beginning visualizing");
					visualizer.startVisualizer();

				}
			}

		});

		tcp.server.post.addPostParamter("master_node", "true");
		tcp.server.addToNetwork("visualizer");

		Platform.setImplicitExit(false);

		launch(args);

	}

	public static void sendProgress(int progress, String stage) {

		progress_tcp.client.onConnect(new RunnableArg<String>() {

			@Override
			public void run() {
				Progress prog = new Progress(progress, stage);
				String json = gson.toJson(prog);
				progress_tcp.client.send(json);
			}

		});

		try {
			progress_tcp.client.connectTNetwork("progress_server");
		} catch (Exception e) {
		}
	}

	public void startVisualizer() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				try {
					tcp.client.connectTNetwork("client");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				sendProgress(1, "Visualizing");

				creatElements();

				sendProgress(20, "Visualizing");
				Canvas_height = getCanvasHeight();
				Canvas_width = getCanvasWidth();

				if (Canvas_height > Canvas_width) {
					Canvas_width = Canvas_height;
				} else {
					Canvas_height = Canvas_width;
				}

				File uml_picture = null;
				if (Canvas_height > standard.normalredering_limit) {
					System.out.println("Canvas size exeeded limit of " + standard.normalredering_limit
							+ "px. Seqentual redering turned on");
					try {
						uml_picture = sequentialRendering();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Normal rendering : on");
					uml_picture = normalRender();
				}

				System.out.println("Visualizing comeplete");

				sendProgress(90, "Visualizing");

				try {
					tcp.client.sendFile(uml_picture);
				} catch (Exception e) {
				}

				System.out.println("Picture sent to clients");

				uml_picture.delete();
				cleanup();
				System.out.println("Cleanup comeplete");

				sendProgress(100, "Visualizing");
			}

		});

	}


	public void start(Stage primaryStage) throws Exception {
		

	}

	public File normalRender() {

		Canvas canvas = new Canvas();
		canvas.setHeight(Canvas_height);
		canvas.setWidth(Canvas_width);
		GraphicsContext cx = canvas.getGraphicsContext2D();

		drawElemements(cx);
		sendProgress(50, "Visualizing");
		drawArrows(cx);
		sendProgress(70, "Visualizing");

		return saveToImage(canvas, project_name);

	}

	public File[][] pictures;
	public ImageMerger imagemerger = new ImageMerger();

	public File sequentialRendering() throws IOException {

		int horisisontal = (int) Math.ceil(Canvas_height / standard.normalredering_limit);
		int vertical = (int) Math.ceil(Canvas_width / standard.normalredering_limit);

		pictures = new File[vertical][vertical];

		int totallAmount = horisisontal * vertical;
		int picture_number = 0;

		System.out.println("Rendering images");
		for (int y = 0; y < vertical; y++) {
			for (int x = 0; x < horisisontal; x++) {

				Canvas canvas = new Canvas();
				canvas.setHeight(standard.normalredering_limit);
				canvas.setWidth(standard.normalredering_limit);

				// System.out.println("offset_X : " + this.offsetX);
				// System.out.println("offset_Y : " + this.offsetY);
				GraphicsContext cx = canvas.getGraphicsContext2D();

				creatElements();

				drawElemements(cx);
				drawArrows(cx);

				String image_name = project_name + "(" + picture_number + ")";

				pictures[y][x] = saveToImage(canvas, image_name);

				// System.out.println("Created Image: (" + picture_number + ")");

				// : Amount of pictures
				picture_number++;

				this.offsetX -= standard.normalredering_limit;
				resetColor();

				// : Prints the progress
				int procentage = getProcetage(picture_number, totallAmount);
				// System.out.println(procentage + "%");
				sendProgress(procentage, "Rendering");
			}
			this.offsetX = 0;
			this.offsetY -= standard.normalredering_limit;

		}

		System.out.println("Merging " + picture_number + " Images");

		BufferedImage finalIMG = null;
		int merge_index = 0;
		for (int y = 0; y < vertical; y++) {
			BufferedImage rowIMG = null;
			for (int x = 0; x < horisisontal; x++) {
				BufferedImage i1 = ImageIO.read(pictures[y][x]);
				if (rowIMG != null)
					rowIMG = imagemerger.joinHorizontal(rowIMG, i1, 1);
				else
					rowIMG = i1;
				pictures[y][x].delete();
				merge_index++;
				int procentage = getProcetage(merge_index, totallAmount);
				sendProgress(procentage, "Merging");

			}
			if (finalIMG != null)
				finalIMG = imagemerger.joinVertical(finalIMG, rowIMG, 1);
			else
				finalIMG = rowIMG;
		}

		sendProgress(70, "Visualizing");
		System.out.println("Done Mergin images");
		System.out.println("Writing to file");
		File image_location = new File(project_name + ".png");
		ImageIO.write(finalIMG, "png", image_location);
		sendProgress(80, "Visualizing");
		return image_location;
	}

	public int getProcetage(int a, int b) {
		return (int) (a * 100.0f) / b;
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

	public static ArrayList<Relationship> getRellationships(UMLPackage inputPackage) {

		ArrayList<Relationship> newPackage = new ArrayList<Relationship>();

		newPackage.addAll(inputPackage.getRelationships());

		if (!inputPackage.Packages.isEmpty()) {
			for (int i = 0; i < inputPackage.Packages.size(); i++) {
				newPackage.addAll(getRellationships(inputPackage.Packages.get(i)));
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
				} catch (Exception e) {
					// System.err.println("Could not class Arrow for :" + e.getMessage());
				}
			}
		}

	}

	private double X;
	private double Y;
	private double targetX;
	private double targetY;

	private void drawArrows(GraphicsContext cx) {

		for (Relationship rel : rellationships) {

			try {

				Point p1 = getcordinate(rel.getSource());
				Point p2 = getcordinate(rel.getDestination());

				drawArrow(cx, (int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());

				// System.out.println("Arrow from : " + rel.getSource().getName() + " -> " +
				// rel.getDestination().getName());

			} catch (Exception e) {
				System.err.println("Could not draw Arrow : " + e.getMessage());
			}
		}

	}

	private Point getcordinate(UMLClass umlClass) {

		for (int y = 0; y < classes.length; y++) {
			for (int x = 0; x < classes[y].length; x++) {
				if (classes[y][x].getName().equals(umlClass.getName())) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	private void drawArrow(GraphicsContext cx, int pointA_X, int pointA_Y, int pointB_X, int pointB_Y) {

		DrawableCLass classA = classes[pointA_Y][pointA_X];
		DrawableCLass classB = classes[pointB_Y][pointB_X];

		increaseColor(200);

		Color arrow_color = this.getcolor();

		cx.setStroke(arrow_color);
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

		double ClassX = 0;
		double ClassY = 0;

		if (x == 0 && y == 0) {
			ClassX = this.offsetX + standard.padding;
			ClassY = this.offsetY + standard.padding;
		} else {
			ClassX = getpreviousX(x);
			ClassY = getpreviousY(y);
		}

		DrawableCLass newClass = new DrawableCLass(ClassX, ClassY, umlClass);

		return newClass;

	}

	public double getpreviousY(int lineY) {

		double maxY = this.offsetY;

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

		double maxX = this.offsetX;

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

	public File saveToImage(Canvas canvas, String picture_name) {

		int width = (int) canvas.getHeight();
		int height = (int) canvas.getWidth();

		WritableImage wim = new WritableImage(width, height);

		canvas.snapshot(null, wim);

		String imagename = "pictures/" + picture_name + ".png";

		File file = new File(imagename);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
		} catch (Exception s) {
		}
		return file;
	}

	private int state = 0;
	private int a = 255;
	private int r = 255;
	private int g = 0;
	private int b = 0;

	private Color getcolor() {
		if (state == 0) {
			g++;
			if (g == 255)
				state = 1;
		}
		if (state == 1) {
			r--;
			if (r == 0)
				state = 2;
		}
		if (state == 2) {
			b++;
			if (b == 255)
				state = 3;
		}
		if (state == 3) {
			g--;
			if (g == 0)
				state = 4;
		}
		if (state == 4) {
			r++;
			if (r == 255)
				state = 5;
		}
		if (state == 5) {
			b--;
			if (b == 0)
				state = 0;
		}
		int hex = (a << 24) + (r << 16) + (g << 8) + (b);

		javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, 1);
		return fxColor;
	}

	public void increaseColor(int amount) {
		for (int i = 0; i < amount; i++) {
			getcolor();
		}
	}

	public void resetColor() {
		state = 0;
		a = 255;
		r = 255;
		g = 0;
		b = 0;
	}

}
