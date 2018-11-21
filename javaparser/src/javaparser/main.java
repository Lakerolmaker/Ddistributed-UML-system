package javaparser;

import java.io.File;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class main {

	public static void main(String[] args) throws Exception {

		String FILE_PATH = "C:\\Users\\duyka\\git\\MiniProject-DistributedSystem\\javaparser";

		File file = new File(FILE_PATH);
		// listClasses(file);
		listMethodCalls(file);

		System.out.println("done");

	}

	public static void listClasses(File projectDir) throws ParseException {
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			System.out.println(path);
			try {
				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(ClassOrInterfaceDeclaration n, Object arg) {
						super.visit(n, arg);
						System.out.println(" * " + n.getName());
					}
				}.visit(JavaParser.parse(file), null);
				System.out.println(); // empty line
			} catch (IOException e) {
				new RuntimeException(e);
			}
		}).explore(projectDir);
	}

	public static void listMethodCalls(File projectDir) throws ParseException {
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			System.out.println(path);
			try {
				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(MethodCallExpr n, Object arg) {
						super.visit(n, arg);
						System.out.println(" [L " + n.getBegin() + "] " + n);
					}
				}.visit(JavaParser.parse(file), null);
				System.out.println(); // empty line
			} catch (IOException e) {
				new RuntimeException(e);
			}
		}).explore(projectDir);
	}
}
