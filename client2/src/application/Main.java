package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("Main.fxml"));
			//BorderPane root = new BorderPane();
			AnchorPane root = loader.load();
			MainController mainController = loader.getController();
			mainController.setMain(this);
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
