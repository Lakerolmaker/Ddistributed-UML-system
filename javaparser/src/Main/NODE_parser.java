package Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import FileClasses.Method;
import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import FileClasses.Variable;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
import TCP.ZIP;

public class NODE_parser {

	public static TCP tcp = new TCP();

	public static void main(String[] args) throws Exception {

		tcp.client.connectTNetwork("visualizer");

		tcp.server.initializeServer();
		tcp.server.startFileServer(new RunnableArg<File>() {

			@Override
			public void run() {

				System.out.println("Recived data");

				ZIP zip = new ZIP();
				File recivedFile = this.getArg();

				File unzipedFile = zip.uncompress(recivedFile);

				UMLPackage project = Parse(unzipedFile);

				Gson jsonParser = new Gson();
				String project_json = jsonParser.toJson(project);

				TCP_data data = new TCP_data();
				data.setData(project_json);
				data.setMetaData("Parsed data");

				String data_json = jsonParser.toJson(data);

				tcp.client.send(data_json + "\n");
				System.out.println("Parsed data sent");
				
				
				//: Deleted the files after the parsed data is sent to the visualizer.
				zip.deleteFile(unzipedFile);
				zip.deleteFile(recivedFile);
				System.out.println("File cleaned");
				System.out.println("Ready to parse");
			
			}
		});
		tcp.server.addToNetwork("parser");

	}

	public static UMLPackage Parse(File file) {

		UMLPackage UMLpackage = new UMLPackage();
		UMLpackage.name = file.getName();

		String url = file.getName();

		File[] files = file.listFiles();

		for (File newFile : files) {
			if ((newFile.isDirectory()) && (!newFile.isHidden())) {
				UMLPackage newPackage = Parse(newFile);
				UMLpackage.addPackage(newPackage);

			} else if ((newFile.isFile() && (!newFile.isHidden()) && (getFileType(newFile).equals(".java")))) {
				UMLClass newClass = parseFile(newFile);
				UMLpackage.classes.add(newClass);

			}
		}

		return UMLpackage;

	}

	private static String getFileType(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	public static UMLClass parseFile(File file) {

		UMLClass UMLclass = new UMLClass();

		try {

			UMLclass.name = classVisitor(file).name;
			UMLclass.Methods = methodVistor(file);
			UMLclass.Variables = variableVisitor(file);

			return UMLclass;
		} catch (Exception e) {
			return UMLclass;

		}

	}

	private static ArrayList<Method> methodVistor(File file) throws Exception {

		ArrayList<Method> Methods = new ArrayList<Method>();

		VoidVisitorAdapter<Object> methodAdapter = new VoidVisitorAdapter<Object>() {

			@Override
			public void visit(MethodDeclaration n, Object arg) {
				/*
				 * here you can access the attributes of the method. this method will be called
				 * for all methods in this CompilationUnit, including inner class methods
				 */

				super.visit(n, arg);

				Method method = new Method();
				method.name = n.getNameAsString();
				method.returnType = n.getType().toString();

				ArrayList<Variable> variables = new ArrayList<Variable>();

				NodeList<Parameter> nodes = n.getParameters();

				for (Parameter parameter : nodes) {

					String type = parameter.getType().toString();
					String name = parameter.getNameAsString();
					variables.add(new Variable(type, name));
				}

				Methods.add(method);
			}

		};

		methodAdapter.visit(JavaParser.parse(file), null);

		return Methods;

	}

	private static Variable classVisitor(File file) throws Exception {

		Variable var = new Variable();

		VoidVisitorAdapter<Object> methodAdapter = new VoidVisitorAdapter<Object>() {

			@Override
			public void visit(ClassOrInterfaceDeclaration n, Object arg) {

				super.visit(n, arg);
				var.name = n.getNameAsString();

			}

		};

		methodAdapter.visit(JavaParser.parse(file), null);

		return var;

	}

	private static ArrayList<Variable> variableVisitor(File file) throws Exception {

		ArrayList<Variable> Variables = new ArrayList<Variable>();

		VoidVisitorAdapter variableAdapter = new VoidVisitorAdapter<Object>() {

			@Override
			public void visit(VariableDeclarationExpr n, Object arg) {

				List<VariableDeclarator> list = n.getVariables();
				// as getVariables() returns a list we need to implement that way
				for (VariableDeclarator var : list) {

					String item = var.toString();

					if (item.contains("=")) {

						if (item != null && item.length() > 0) {

							int index = item.lastIndexOf("=");
							String variableName = item.substring(0, index);
							variableName = variableName.trim();
							if (!variableName.equals("i")) {
								String type = var.getType().toString();
								String name = var.getNameAsString();
								Variable variable = new Variable(type, name);
								Variables.add(variable);
							}
						}
					}

				}
			}
		};

		variableAdapter.visit(JavaParser.parse(file), null);

		return Variables;

	}

}
