package javaparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class main {

	
	public static void main(String[] args) throws Exception {
		
		String FILE_PATH = "/Users/jacobolsson/Google Drive/Programs/Java/Unit-01";
		
		File file = new File(FILE_PATH);
		
		CompilationUnit cu = JavaParser.parse(file);

		System.out.println("done");
		
	}

}
