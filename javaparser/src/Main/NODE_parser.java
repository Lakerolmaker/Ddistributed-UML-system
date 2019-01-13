package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.gson.Gson;

import FileClasses.Method;
import FileClasses.Progress;
import FileClasses.Relationship;
import FileClasses.Relationship.RelaType;
import FileClasses.UMLClass;
import FileClasses.UMLPackage;
import FileClasses.Variable;
import TCP.RunnableArg;
import TCP.TCP;
import TCP.TCP_data;
import TCP.ZIP;

/*
 * 
 * A class for parsing a java project folder into a preset format found in the FilClass folder
 * 
 */


public class NODE_parser {

	public static TCP tcp = new TCP();
	public static TCP progress_tcp = new TCP();
	public static TCP project_name_tcp = new TCP();
	public static Gson gson = new Gson();

	public static void main(String[] args) throws Exception {

		tcp.server.initializeServer();

		tcp.server.startFileServer(new RunnableArg<File>() {

			@Override
			public void run() {

				try {
					tcp.client.connectTNetwork("visualizer");
				} catch (Exception e) {
					System.err.println("TCP-Server - Error : No visualizer avalible");
				}

				System.out.println("Recived data");
				sendProgress(10, "Parsing");

				ZIP zip = new ZIP();
				File recivedFile = this.getArg();

				sendProgress(20, "Parsing");
				File unzipedFile = zip.uncompress(recivedFile);

				UMLPackage project = Parse(unzipedFile);

				sendProjectName(project.getName());

				sendProgress(80, "Parsing");

				Gson jsonParser = new Gson();
				String project_json = jsonParser.toJson(project);

				TCP_data data = new TCP_data();
				data.setData(project_json);
				data.setMetaData("Parsed data");

				String data_json = jsonParser.toJson(data);

				sendProgress(100, "Parsing");
				tcp.client.send(data_json);
				System.out.println("Parsed data sent");

				// : Deleted the files after the parsed data is sent to the visualizer.
				zip.deleteFile(unzipedFile);
				zip.deleteFile(recivedFile);
				System.out.println("File cleaned");
				System.out.println("Ready to parse");

			}
		});

		tcp.server.addToNetwork("parser");

	}

	public static void sendProjectName(String name) {
		project_name_tcp.client.onConnect(new RunnableArg<String>() {

			@Override
			public void run() {
				project_name_tcp.client.send(name);
			}

		});
		try {

			project_name_tcp.client.connectTNetwork("project_name_server");

		} catch (Exception e) {
		}
	}

	public static void sendProgress(int progress, String stage) {

		progress_tcp.client.onConnect(new RunnableArg<String>() {

			@Override
			public void run() {
				Progress prog = new Progress(progress, stage);
				String json = gson.toJson(prog);
				progress_tcp.client.send(json);
			}

		});
		try {

			progress_tcp.client.connectTNetwork("progress_server");

		} catch (Exception e) {
		}
	}

	public static UMLPackage Parse(File file) { // Parse a project folder into a single Java class
		// Initiate the Java class
		UMLPackage UMLpackage = new UMLPackage();
		UMLpackage.name = file.getName();
		// Get all children files/folders inside the project folder
		File[] files = file.listFiles();

		for (File newFile : files) {
			// If the component in the array is a folder, parse the folder and put it in the
			// Java class
			if ((newFile.isDirectory()) && (!newFile.isHidden())) {
				UMLPackage newPackage = Parse(newFile);
				UMLpackage.addPackage(newPackage);
				// If the component in the array is a file, parse the file and put it in the
				// Java class
			} else if ((newFile.isFile() && (!newFile.isHidden()) && (getFileType(newFile).equals(".java")))) {
				UMLClass newClass = parseFile(newFile);
				UMLpackage.classes.add(newClass);
			}
		}

		for (File newFile : files) {
			// If the component in the array is a file, add all relationships to the Java
			// class
			if ((newFile.isFile() && (!newFile.isHidden()) && (getFileType(newFile).equals(".java")))) {
				try {
					createEdges(newFile, UMLpackage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		for (Relationship relationship : UMLpackage.getRelationships()) {
			if (relationship.getType() != null) {
				if (relationship.getType() == RelaType.ASSOCIATION) {
					System.out.println(relationship.getSource().getName() + " ASSOCIATES WITH "
							+ relationship.getDestination().getName());
				} else if (relationship.getType() == RelaType.EXTENDS) {
					System.out.println(
							relationship.getSource().getName() + " EXTENDS " + relationship.getDestination().getName());
				} else if (relationship.getType() == RelaType.IMPLEMENTS) {
					System.out.println(relationship.getSource().getName() + " IMPLEMENTS "
							+ relationship.getDestination().getName());
				}
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

	public static UMLClass parseFile(File file) { // Parse a .java file type to a class

		UMLClass UMLclass = new UMLClass();

		try {

			UMLclass.name = classVisitor(file).name;
			UMLclass.Methods = methodVistor(file);
			UMLclass.Variables = variableVisitor(file);
			UMLclass.composistion = compositionAdder(UMLclass.Variables);

			return UMLclass;
		} catch (Exception e) {
			return UMLclass;

		}

	}

	private static ArrayList<String> compositionAdder(ArrayList<Variable> variables) {

		ArrayList<String> composists = new ArrayList<String>();

		for (Variable variable : variables) {

			switch (variable.type) {
			case "String":
				break;
			case "int":
				break;
			case "double":
				break;
			case "boolean":
				break;
			case "char":
				break;
			case "float":
				break;
			case "Object":
				break;
			default:
				addToList(composists, variable.type);
				break;
			}

		}

		return composists;
	}

	private static void addToList(ArrayList<String> variables, String entry) {
		boolean found = false;
		for (String variable : variables) {
			if (variable.equals(entry)) {
				found = true;
			}
		}
		if (!found) {
			variables.add(entry);
		}
	}

	private static ArrayList<Method> methodVistor(File file) throws Exception {
		// Find all method declarations in a java file and parse their names and types
		// into the Java class
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
		// Find all class declarations in a java file and parse their names into the
		// Java class
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
		// Find all variables in a java file and parse their names and types into the
		// Java class
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

	private static void createEdges(File file, UMLPackage umlPackage) throws Exception {
		// Get all relationships in a folder project and parse them into a single Java
		// class
		CompilationUnit cu = getCompilationUnit(file);
		List<TypeDeclaration<?>> td = cu.getTypes();
		for (TypeDeclaration<?> typeDeclaration : td) {
			createAssociationEdges((ClassOrInterfaceDeclaration) typeDeclaration, umlPackage);
			createExtendsImplementsEdges((ClassOrInterfaceDeclaration) typeDeclaration, umlPackage);
		}
	}

	private static void createAssociationEdges(ClassOrInterfaceDeclaration typeDeclaration, UMLPackage umlPackage) {
		// Find all relationships of type Association
		createAssociationEdgeForConstructor(typeDeclaration, umlPackage);
		UMLClass umlClass = umlPackage.getClassByName(typeDeclaration.getNameAsString());
		List<BodyDeclaration<?>> methods = typeDeclaration.getMembers();
		// List<MethodDeclaration> removeMethods = new
		// ArrayList<MethodDeclaration>(0);
		for (BodyDeclaration<?> bodyDeclaration : methods) {
			MethodDeclaration methodDeclaration = null;
			if (bodyDeclaration instanceof MethodDeclaration) {
				methodDeclaration = (MethodDeclaration) bodyDeclaration;
				if (methodDeclaration.getName().equals("main")) {
					Relationship relationship = new Relationship(umlClass, umlPackage.getClassByName("Component"),
							RelaType.ASSOCIATION);
					umlPackage.getRelationships().add(relationship);
				}
				List<Parameter> parameters = methodDeclaration.getParameters();
				if (parameters != null) {
					for (Parameter parameter : parameters) {
						if (isReferenceType(parameter.getType())) {
							UMLClass refClass = umlPackage.getClassByName(parameter.getType().toString());
							if (refClass != null) {
								if (umlPackage.getRelationship(typeDeclaration.getNameAsString(), refClass.getName(),
										RelaType.ASSOCIATION) == null) {
									Relationship relationship = new Relationship(umlClass, refClass,
											RelaType.ASSOCIATION);
									umlPackage.getRelationships().add(relationship);
								}
								// removeMethods.add(methodDeclaration);
							}
						}
					}
				}
			}
		}
	}

	private static void createAssociationEdgeForConstructor(ClassOrInterfaceDeclaration typeDeclaration,
			UMLPackage umlPackage) {
		// Find all relationships of type Association for constructor
		UMLClass umlClass = umlPackage.getClassByName(typeDeclaration.getNameAsString());
		List<BodyDeclaration<?>> methods = typeDeclaration.getMembers();
		for (BodyDeclaration bodyDeclaration : methods) {
			ConstructorDeclaration methodDeclaration = null;
			if (bodyDeclaration instanceof ConstructorDeclaration) {
				methodDeclaration = (ConstructorDeclaration) bodyDeclaration;

				List<Parameter> parameters = methodDeclaration.getParameters();
				if (parameters != null) {
					for (Parameter parameter : parameters) {
						if (isReferenceType(parameter.getType())) {
							UMLClass refClass = umlPackage.getClassByName(parameter.getType().toString());
							if (refClass != null) {

								if (!typeDeclaration.isInterface() && refClass.isInterface()) {
									if (umlPackage.getRelationship(typeDeclaration.getNameAsString(),
											refClass.getName(), RelaType.ASSOCIATION) == null) {
										Relationship relationship = new Relationship(umlClass, refClass,
												RelaType.ASSOCIATION);
										umlPackage.getRelationships().add(relationship);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static void createExtendsImplementsEdges(ClassOrInterfaceDeclaration typeDeclaration,
			UMLPackage umlPackage) {
		// Find all relationships of type Extends
		List<ClassOrInterfaceType> extendsList = typeDeclaration.getExtendedTypes();
		if (extendsList != null) {
			for (ClassOrInterfaceType classOrInterfaceType : extendsList) {
				UMLClass source = umlPackage.getClassByName(typeDeclaration.getNameAsString());
				UMLClass destination = umlPackage.getClassByName(classOrInterfaceType.getNameAsString());
				if (source != null && destination != null) {
					Relationship relationship = new Relationship(source, destination, RelaType.EXTENDS);
					umlPackage.getRelationships().add(relationship);
				}
			}
		}
		// Find all relationships of type Implements
		List<ClassOrInterfaceType> implementsList = typeDeclaration.getImplementedTypes();
		if (implementsList != null) {
			for (ClassOrInterfaceType classOrInterfaceType : implementsList) {
				UMLClass source = umlPackage.getClassByName(typeDeclaration.getNameAsString());
				UMLClass destination = umlPackage.getClassByName(classOrInterfaceType.getNameAsString());
				if (source != null && destination != null) {
					Relationship relationship = new Relationship(source, destination, RelaType.IMPLEMENTS);
					umlPackage.getRelationships().add(relationship);
				}
			}
		}
	}

	private static CompilationUnit getCompilationUnit(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CompilationUnit cu = null;
		try {
			cu = JavaParser.parse(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cu;
	}

	private static boolean isReferenceType(Type type) {
		if ((type instanceof PrimitiveType) || (type instanceof ReferenceType
				&& (type.toString().equals("String") || type.toString().contains("[]")))) {
			return false;
		}
		return true;
	}

}
