package application;


import java.net.URL;
import java.util.ResourceBundle;

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
import ui.RingProgressIndicator;

public class MainController implements Initializable{
/*Ä
	//Constructor
	public MainController(){
		
	}
	
	public void initComponents() {
	}
	
	private void createEvents() {	
	}*/
    
	@FXML
	public Button selectFile;
	@FXML
	public TextArea textArea;
	@FXML
	public StackPane stackPane;
	@FXML 
	public Pane linkPane;
	
	private Main main;
	public void setMain(Main main) {
		this.main = main;
		// TODO Auto-generated method stub
		
	}
	//test the text area
	public void addNumbers(ActionEvent event) {
		int a= 4, b = 6;
		int x =  a+b;
		textArea.setText(Integer.toString(x));
	}
	
	FileUpload fc = new FileUpload();
	public void uploadFile() {
		fc.selectFile();
		if(fc != null) {
			textArea.setText(fc.readFile(fc.file));
		}
	}

	@Override     //it initialises 
	public void initialize(URL arg0, ResourceBundle arg1) {
		String colour = "abcdef";
		linkPane.setBackground(new Background(new BackgroundFill(Color.web("#" + colour), CornerRadii.EMPTY, Insets.EMPTY)));
		// TODO Auto-generated method stub
		RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
		ringProgressIndicator.setRingWidth(30);
		ringProgressIndicator.makeIndeterminate();
		
		stackPane.getChildren().add(ringProgressIndicator);
		
		ProgressThread pt = new ProgressThread(ringProgressIndicator);
		pt.start();
	}


}

