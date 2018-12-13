package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.ZIP;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import ui.RingProgressIndicator;

public class MainController implements Initializable {

	TCP tcp = new TCP();
	ZIP zip = new ZIP();
	RingProgressIndicator progressbar;
    public static GridPane gridpane = new GridPane();
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
    

	// test the text area
	public void addNumbers(ActionEvent event) {
		int a = 4, b = 6;
		int x = a + b;
		textArea.setText(Integer.toString(x));
	}

	DirectoryChooser directoryChooser = new DirectoryChooser();

	int index = 0;
	public void addbutton(File file) {
		
		Button newbtn = new Button();
		
		newbtn.setTextAlignment(TextAlignment.CENTER);
		newbtn.setPadding(new Insets(5, 5, 5, 5));
		newbtn.minWidth(122);
		newbtn.prefWidth(122);
		newbtn.maxWidth(122);
		
		newbtn.setText(removeFileEnding(file.getName()));
		
		newbtn.setTextOverrun(OverrunStyle.ELLIPSIS);
		
		
		newbtn.setCursor(Cursor.HAND);
		newbtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				openFile(file);
			}
			
		});
		
		gridpane.add(newbtn, 0 , index);
		gridpane.setHgrow(newbtn, Priority.ALWAYS);
		gridpane.setVgrow(newbtn, Priority.ALWAYS);
		
		scrollpane.setPrefHeight(gridpane.getHeight());
	
		index++;
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
		fileText += "Project ETA : " + "???" + "\n";

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

	public void sendFile() throws Exception {

		tcp.client.sendFile(selectedFile);

	}

	@Override // it initialises
	public void initialize(URL arg0, ResourceBundle arg1) {

		scrollbar.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollbar.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		
		scrollbar.vvalueProperty().bind(gridpane.heightProperty());
		
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

		stackPane.getChildren().add(progressbar);

		try {
			initTCP();
		} catch (Exception e) {
			System.err.println("Could not initialize the tcp : " + e.getMessage());
		}

	}
	
	public void addfile(File file) {
	
			Platform.runLater(()->{

				addbutton(file);

			});
		
	
		
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

	public void increase() {
		int progress = progressbar.getProgress();
		progress += 1;
		
		progressbar.setProgress(progress);
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

		tcp.client.connectTNetwork("parser");

	}

}
