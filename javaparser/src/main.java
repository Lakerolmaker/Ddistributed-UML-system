import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class main {

	
	public static void main(String[] args) throws Exception {
		
		String CurrentDir = System.getProperty("user.dir");
		String inputFolder_Path = CurrentDir + "/InputFiles";
		
		Parse(new File(inputFolder_Path));
			
	}
	
	public static void Parse(File file) {
		
		if((file.isFile() && (!file.isHidden()))) {
			System.out.println(parseFile(file));
		}else if(file.isDirectory()) {
			parseFolder(file);
		}

	}
	
	public static void parseFolder(File file){
			
		File[] files = file.listFiles();
		
		for (File newFile : files) {
			Parse(newFile);
		}
		
	}
	
	public static CompilationUnit parseFile(File file) {
		
		CompilationUnit cu;
		try {
			cu = JavaParser.parse(file);
			return cu;
		} catch (Exception e) {
			return null;
		}

	}
	
}
