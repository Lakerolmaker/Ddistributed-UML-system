package FileClasses;

import java.util.ArrayList;

public class UMLPackage {

	public String name;
	public ArrayList<UMLPackage> Packages;
	public ArrayList<UMLClass> classes;
	
	public UMLPackage() {
		super();
		Packages = new ArrayList<UMLPackage>();
		classes = new ArrayList<UMLClass>();
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
	
	
	
	
}
