package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import com.google.gson.Gson;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;

import FileClasses.Progress;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.ZIP;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import ui.RingProgressIndicator;

public class MainController implements Initializable {

	//: the controller which handles all logic for the client
	
	TCP tcp = new TCP();
	TCP progressServer = new TCP();
	TCP nameServer = new TCP();
	
	ZIP zip = new ZIP();
	
	RingProgressIndicator progressbar;
	GridPane gridpane = new GridPane();
	BoxBlur bb = new BoxBlur();


	File selectedFile = null;

	@FXML
	private TextArea textArea;
	@FXML
	private StackPane stackPane;
	@FXML
	private Pane linkPane;
	@FXML
	private AnchorPane scrollpane;
	@FXML
	private ScrollPane scrollbar;
	@FXML
	private ImageView imagePane;
	@FXML
	private Button btn1;
	@FXML
	private Button selectFile;
	@FXML
	private Label Progress_label;
	@FXML
	private Label Project_name_label;

	@Override // it initialises
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//: hides the progress indicators from the beginning;
		hideProgress();
		blurBox();
		
		//: Adds the blur config to the progress indicators
		Project_name_label.setEffect(bb);
		Progress_label.setEffect(bb);

		//: connfig for the button scrollbar
		scrollbar.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollbar.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollbar.vvalueProperty().bind(gridpane.heightProperty());

		//: config for the grid of buttons
		gridpane.setVgap(20);
		gridpane.setLayoutX(10);
		gridpane.setLayoutY(9);
		scrollpane.getChildren().add(gridpane);

		String colour = "abcdef";
		linkPane.setBackground(
				new Background(new BackgroundFill(Color.web("#" + colour), CornerRadii.EMPTY, Insets.EMPTY)));
		progressbar = new RingProgressIndicator();
		progressbar.setRingWidth(30);
		progressbar.makeIndeterminate();

		//: Adds the progress bar to the window
		stackPane.getChildren().add(progressbar);

		//: Initilize the tcp connections.
		try {
			initTCP();
		} catch (Exception e) {
			System.err.println("Could not initialize the tcp : " + e.getMessage());
		}

	}

	DirectoryChooser directoryChooser = new DirectoryChooser();

	int buttonIndex = 0;
	public void addbutton(File file) {

		Button newbtn = new Button();

		newbtn.setCursor(Cursor.HAND);

		newbtn.setTextAlignment(TextAlignment.CENTER);
		newbtn.setTextOverrun(OverrunStyle.ELLIPSIS);

		newbtn.setPadding(new Insets(8, 8, 8, 8));

		newbtn.setMinWidth(122);
		newbtn.setPrefWidth(122);
		newbtn.setMaxWidth(122);

		newbtn.setText(removeFileEnding(file.getName()));

		newbtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				openFile(file);
			}

		});

		newbtn.setOpacity(0);
		gridpane.add(newbtn, 0, buttonIndex);

		fadeIn(newbtn);

		scrollpane.setPrefHeight(gridpane.getHeight());

		buttonIndex++;
	}
	
	public boolean isVisable() {
		return Progress_label.isVisible();
	}
	
	//: Fade in a button.
	public void fadeIn(Button btn) {
		FadeTransition ft = new FadeTransition(Duration.millis(1200), btn);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}

	//: blur the progress boxes
	public void blurBox() {
		addBlur();
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				bb.setWidth(BlurCounter.getCounter());
				BlurCounter.increase();
			}

		}));

		timer.setCycleCount(100);
		timer.play();

		timer.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				hideProgress();
			}

		});

	}

	//: unblur the boxes
	public void unBlurBox() {

		showProgress();
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				bb.setWidth(BlurCounter.getCounter());
				BlurCounter.decrease();
			}

		}));

		timer.setCycleCount(100);
		timer.play();

		timer.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				removeBlur();
			}

		});
	}

	//: Removes the blurr effect
	public void addBlur() {
		bb.setIterations(1);
	}

	public void hideProgress() {
		Progress_label.setVisible(false);
		Project_name_label.setVisible(false);
	}
	
	public void showProgress() {
		Progress_label.setVisible(true);
		Project_name_label.setVisible(true);
	}
	
	//: Adds the blur effect
	public void removeBlur() {
		bb.setIterations(0);
	}

	public void selectFile() throws Exception {
		File selectedDirectory = directoryChooser.showDialog(NODE_client.myStage);
		if (selectedDirectory != null) {
			File file = new File(selectedDirectory.getAbsolutePath());
			if (isVailidProject(file)) {
				selectedFile = file;
				addFileInfo(selectedFile);
			} else {
				selectedFile = null;
				notValid();
			}

		}
	}

	public void notValid() {
		popup("Error", "Not a valid project");
		textArea.setText("choose file");
	}

	public boolean isVailidProject(File file) {

		if (file.isDirectory())
			return true;
		else
			return false;

	}

	public void popup(String title, String message) {

		JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

	}

	public void addFileInfo(File file) {

		String fileText = "";
		fileText += "Project size : " + file.getName() + "\n";
		fileText += "File size       : " + getSizeEnding(folderSize(file)) + "\n";
		fileText += "Project ETA : " + getTimeEnding(getEAT(folderSize(file))) + "\n";

		textArea.setText(fileText);

	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	public String removeFileEnding(String str) {
		return str.substring(0, str.lastIndexOf('.'));
	}

	public static String getSizeEnding(long size) {

		if (size < 1024) {
			return size + " Bytes";
		} else if (size < 1048576) {
			return size / 1024 + " Kilobytes";
		} else if (size < 1073741824) {
			return size / 1048576 + " Megabytes";
		} else if (size > 1073741824) {
			return size / 1073741824 + " Gigabytes";
		}
		return null;
	}

	public String getTimeEnding(long seconds) {
		// TimeUnit.HOURS.
		if (seconds < 60) {
			return seconds + " Seconds";
		} else if (seconds < 3600) {
			return seconds / 60 + " Minutes";
		} else if (seconds < 86400) {
			return seconds / 3600 + " Hours";
		} else if (seconds < 86400) {
			return seconds / 31536000 + " Days";
		} else if (seconds > 31536000) {
			return seconds / 31536000 + " Years";
		}
		return null;

	}

	public void sendFile() {

		if (selectedFile != null) {

			try {
				tcp.client.connectTNetwork("parser");
			} catch (Exception e) {
				popup("Error", "No notwork avaliable , contact your system administrator");
			}

			fileSize = folderSize(selectedFile);
			StartTime = System.nanoTime();

			tcp.client.send(selectedFile);

		} else {
			popup("Error", "Please select a project");
		}

	}

	public void printTotalltime() {

		endTime = System.nanoTime();

		long nanoSeconds = endTime - StartTime;

		long seconds = getSeconds(nanoSeconds);

		System.out.println("Totall time in nano-seconds : " + nanoSeconds);
		System.out.println("Totall time in seconds : " + seconds);

		//printETAConstant(nanoSeconds);
	}

	public void printETAConstant(long nanoSeconds) {

		long bytes = selectedFile.length();

		long bytesPernano = nanoSeconds / fileSize;

		System.out.println("ETA contant : " + bytesPernano);

	}

	public long ETAConstant = 757289;
	
	public long StartTime = 0;
	public long endTime = 0;
	public long fileSize = 0;

	public long getEAT(long bytes) {
		long nanoSec = bytes * ETAConstant;
		return getSeconds(nanoSec);
	}

	public long getSeconds(long nanoSecs) {
		return nanoSecs / 1000000000;
	}

	public void addfile(File file) {

		Platform.runLater(() -> {

			addbutton(file);
			printTotalltime();
			blurBox();
			resetProgressBar();

		});

	}

	public void addProjectName(String name) {

		Project_name_label.setText(name);
	}

	public void addProgress_Text(String progress) {
		
		Progress_label.setText(progress);
		
		if(!isVisable()) 
			unBlurBox();
	}

	public void setProgressBar(int progress) {

		progressbar.setProgress(progress);

	}

	public void resetProgressBar() {
		setProgressBar(-1);
	}
	
	public void openFile(File file) {
		Desktop dt = Desktop.getDesktop();

		try {
			dt.open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initTCP() throws Exception {

		tcp.server.initializeServer();
		tcp.server.startFileServer(new RunnableArg<File>() {

			@Override
			public void run() {
				File file = this.getArg();

				addfile(file);
			}

		});

		tcp.server.addToNetwork("client");

		ProjectName_Server();
		progress_Server();
	}

	public void ProjectName_Server() throws Exception {

		nameServer.server.initializeServer();
		nameServer.server.start(new RunnableArg<String>() {

			@Override
			public void run() {
				Platform.runLater(() -> {
					addProjectName(this.getArg());
				});
			}
		});
		nameServer.server.addToNetwork("project_name_server");
	}

	public void progress_Server() throws Exception {

		progressServer.server.initializeServer();
		progressServer.server.start(new RunnableArg<String>() {

			@Override
			public void run() {
				Gson gson = new Gson();
				Progress progress = gson.fromJson(this.getArg(), Progress.class);

				Platform.runLater(() -> {
					addProgress_Text(progress.stage);
					setProgressBar(progress.procentage);
				});

			}
		
		});
		progressServer.server.addToNetwork("progress_server");

	}

}
