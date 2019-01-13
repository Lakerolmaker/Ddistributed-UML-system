package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javafx.stage.FileChooser;

public class FileUpload {

	public File file;
	public void selectFile() {
		MainController mc = new MainController();
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter exFilter = new FileChooser.ExtensionFilter(" TEXT files", "*.fc.getExtensionFilters().add(exFilter)");	
		try {
			file = fc.showOpenDialog(null);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String readFile(File file) {
		StringBuilder stringBuffer = new StringBuilder();
		BufferedReader br = null;
		try { 
			br = new BufferedReader(new FileReader(file));
			String text;
			while((text = br.readLine())!=null) {
				stringBuffer.append(text +"\n");
			}
			br.close();
		}catch(Exception e){
			
		}
		
		return stringBuffer.toString();
		
	}
	
	
	
}
