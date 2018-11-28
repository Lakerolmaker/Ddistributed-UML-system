package application;
	
import fileClasses.ZIP;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class NODE_client extends Application {
	
	public static  Stage myStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		myStage = primaryStage;
		try {
			FXMLLoader loader = new FXMLLoader(NODE_client.class.getResource("Main.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Client");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private MainController mc;
    public void setMC(MainController mc) {
    	this.mc = mc;
    }
	
}
