package MainPackage;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.esotericsoftware.kryonet.Server;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
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

	ArrayList<UMLClass> umlClasses = new ArrayList<UMLClass>();
	DrawableCLass[][] classes;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Server server = new Server();
	    server.start();
	    server.bind(54555, 5000);
		
		TCP tcp = new TCP();

		tcp.server.initializeServer();

		tcp.server.start(new RunnableArg<String>() {

			@Override
			public void run() {

				String raw_data = this.getArg();

				Gson javaParser = new Gson();
				TCP_data data = javaParser.fromJson(raw_data, TCP_data.class);

				if (data.metaData.equals("parsed data")) {

					UMLPackage project = javaParser.fromJson(data.data, UMLPackage.class);
					
					umlClasses = getClasses(project);

					creatElements();

					double Canvas_height = getCanvasHeight();
					Canvas canvas = new Canvas();
					canvas.setHeight(Canvas_height);
					canvas.setWidth(Canvas_height);

					GraphicsContext cx = canvas.getGraphicsContext2D();

					drawElemements(cx);

					saveToImage(canvas);

					System.exit(0);

					try {

						JsonArray clients = tcp.client.getFromNetwork("client");

						for (int i = 0; i < clients.size(); i++) {

							JsonObject client = clients.get(i).getAsJsonObject();

							String ip = client.get("ip").getAsString();
							int port = client.get("port").getAsInt();

							tcp.client.connect(ip, port);
							tcp.client.send("file");

						}

					} catch (Exception e) {
					}

				}
			}
		});

		tcp.server.post.addPostParamter("master_node", "true");
		tcp.server.addToNetwork("visualizer");

	}

	public static ArrayList<UMLClass> getClasses(UMLPackage inputPackage) {

		ArrayList<UMLClass> newPackage = new ArrayList<UMLClass>();

		if (!inputPackage.classes.isEmpty()) {
			newPackage.addAll(inputPackage.classes);
		}

		if (!inputPackage.Packages.isEmpty()) {
			for (UMLPackage umlPackage : inputPackage.Packages) {
				newPackage.addAll(getClasses(umlPackage));
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
		return maxWidth + StandardValues.canvasPadding_Y;
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
		return maxHeight + StandardValues.canvasPadding_Y;
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

			classes[Y][X] = createClass(Y, X);

			// : checks for new row.
			if ((i != 0) && ((i + 1) % sqr == 0)) {
				Y++;
				X = 0;
			} else {
				// : increses the collum
				X++;
			}

		}

	}

	public DrawableCLass createClass(int y, int x) {

		double ClassX = getpreviousX(y, x);
		double ClassY = getpreviousY(y, x);

		ClassX += StandardValues.padding;
		ClassY += StandardValues.padding;

		DrawableCLass newClass = new DrawableCLass(ClassX, ClassY);

		return newClass;

	}

	public double getpreviousY(int y, int x) {

		double width = 0;

		try {

			width = classes[y - 1][x].getbottomY();

		} catch (Exception e) {
		}

		return width;
	}

	public double getpreviousX(int y, int x) {

		double height = 0;

		try {

			height = classes[y][x - 1].getrightX();

		} catch (Exception e) {
		}

		return height;

	}

	public void saveToImage(Canvas canvas) {

		int width = (int) canvas.getHeight();
		int height = (int) canvas.getWidth();

		WritableImage wim = new WritableImage(width, height);

		canvas.snapshot(null, wim);

		File file = new File("CanvasImage.png");

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
		} catch (Exception s) {
		}

	}

}
