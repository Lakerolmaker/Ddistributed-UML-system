package FileClasses;

import java.util.ArrayList;

import FileClasses.Relationship.RelaType;

public class UMLPackage {

	public String name;
	public ArrayList<UMLPackage> Packages;
	public ArrayList<UMLClass> classes;
	private ArrayList<Relationship> relationships;

	public UMLPackage() {
		super();
		Packages = new ArrayList<UMLPackage>();
		classes = new ArrayList<UMLClass>();
		relationships = new ArrayList<Relationship>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPackage(UMLPackage newPackage) {
		this.Packages.add(newPackage);
	}

	public ArrayList<UMLPackage> getPackages() {
		return this.Packages;
	}

	public void setPackages(ArrayList<UMLPackage> packages) {
		this.Packages = packages;
	}

	public void addClass(UMLClass newclass) {
		this.classes.add(newclass);
	}

	public ArrayList<UMLClass> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<UMLClass> classes) {
		this.classes = classes;
	}

	public Relationship getRelationship(String source, String destination, RelaType relaType) {
		for (Relationship relationship : relationships) {
			if (relationship.getSource().getName().equals(source)
					&& (relationship.getDestination().getName().equals(destination))
					&& relationship.getType().equals(relaType)) {
				return relationship;
			}
		}
		return null;
	}

	public UMLClass getClassByName(String name) {
		for (UMLClass umlClass : classes) {
			if (umlClass.getName() != null) {
				if (umlClass.getName().equals(name))
					return umlClass;
			}
		}
		return null;
	}

	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}
}
