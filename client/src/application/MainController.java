package application;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import TCP.RunnableArg;
import TCP.TCP;
import fileClasses.ZIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import ui.RingProgressIndicator;

public class MainController implements Initializable{
    
	TCP tcp = new TCP();
	
	@FXML
	public Button selectFile;
	@FXML
	public TextArea textArea;
	@FXML
	public StackPane stackPane;
	@FXML 
	public Pane linkPane;
	
	//test the text area
	public void addNumbers(ActionEvent event) {
		int a= 4, b = 6;
		int x =  a+b;
		textArea.setText(Integer.toString(x));
	}
	
	DirectoryChooser directoryChooser = new DirectoryChooser();
	public void uploadFile() throws Exception {
		File selectedDirectory = directoryChooser.showDialog(NODE_client.myStage);
		 if(selectedDirectory != null){
			 sendFile(selectedDirectory.getAbsolutePath());
         }
	}
	
	public void sendFile(String filePath) throws Exception{

		File newFile = new File(filePath);
		
		JsonArray network = tcp.client.getFromNetwork("parser");
		JsonObject client = network.get(0).getAsJsonObject();

		String ip = client.get("ip").getAsString();
		int port = client.get("port").getAsInt();

		tcp.client.connect(ip, port);
		tcp.client.sendFile(newFile);
		
	}

	@Override     //it initialises 
	public void initialize(URL arg0, ResourceBundle arg1)  {
		String colour = "abcdef";
		linkPane.setBackground(new Background(new BackgroundFill(Color.web("#" + colour), CornerRadii.EMPTY, Insets.EMPTY)));
		RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
		ringProgressIndicator.setRingWidth(30);
		ringProgressIndicator.makeIndeterminate();
		
		stackPane.getChildren().add(ringProgressIndicator);
		
		ProgressThread pt = new ProgressThread(ringProgressIndicator);
		pt.start();

	}


}

