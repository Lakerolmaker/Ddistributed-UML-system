package FileClasses;

import java.util.ArrayList;

public class UMLClass {

	public String id;
	public String name;
	public ArrayList<Variable> Variables;
	public ArrayList<Method> Methods;
	public ArrayList<String> composistion;// : list of ids
	public ArrayList<String> aggregation;// : list of ids
	private boolean isInterface;

	public UMLClass() {
		super();
		Variables = new ArrayList<Variable>();
		Methods = new ArrayList<Method>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Variable> getVariables() {
		return Variables;
	}

	public void setVariables(ArrayList<Variable> variables) {
		Variables = variables;
	}

	public void addVariables(Variable variables) {
		Variables.add(variables);
	}

	public ArrayList<Method> getMethods() {
		return Methods;
	}

	public void setMethods(ArrayList<Method> methods) {
		Methods = methods;
	}

	public void addMethods(Method methods) {
		Methods.add(methods);
	}

	public ArrayList<String> getComposistion() {
		return composistion;
	}

	public void setComposistion(ArrayList<String> composistion) {
		this.composistion = composistion;
	}

	public ArrayList<String> getAggregation() {
		return aggregation;
	}

	public void setAggregation(ArrayList<String> aggregation) {
		this.aggregation = aggregation;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

}
