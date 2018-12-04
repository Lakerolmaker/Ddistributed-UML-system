import java.awt.List;

import java.util.ArrayList;

import java.util.Collections;

import employees.*;

// LUKAS was of telling it : 

//COMMENTS FOR PROMOTE/DEMOTE EMPLOYEE://
///PROMOTE DEMOTE METHODS (4 of them) COMMENTS: SINCE DOWNCASTING IS RISKY, 
//I THINK THIS IS A GOOD OPTION CAUSE WE ARE NOT USING ANY DOWNCASTING 
///AND NO UPCASTING EITHER). THIS IS ALSO NICE CAUSE IT DOESNT MATTER WHAT THE TYPE WAS PREVIOUSLY,
//WE CAN CHANGE wIT EASILY ANYWAY. 
//BAD THING IS THAT EVERY TIME WE ARE CREATING A NEW OBJECT AND DELETING AN OLD ONE, 
//INSTEAD OF JUST KEEPING THE SAME OBJECT AROUND, NOT DELETING IT BUT JUST CHANGING IT

//: JACOBs addition :

//: The benefits of having it the way we are is as follows :
//: It´s safe , and we don´t risk it not working if we add stuff
//: Its also scalable and you can chose what stuff gets cast.

//: The downside of doing it is at follows :
// if the code is changed it´s gonna be hard to fix it.
// you don´t get the new changes if the code is altered


/*
 * THE REUSAXCLASS CODE IN GENERAL:
 * WE USED A LOT OF CASTING, WHICH ISNT IDEAL, AND WE COULDVE DONE IT WITHOUT CASTING, BUT SINCE WE USED AN INTERFACE,
 *  IT SHOULD BE PRETTY SECURE, AND ITS AN EASY WAY FOR US TO CHANGE THINGS THAT WE WANTED TO CHANGE, AND PRINT CLASS SPECIFIC THINGS
 * 
 * */
	

public class ReusaxCorp {
	
	Main main = new Main();
	
	// ARRAYLIST TO HOLD ALL EMPLOYEES IN THE COMPANY
	private ArrayList<Object> companyEmployee = new ArrayList<Object>(); // should it be of object or of

	// 1. CREATE AN EMPLOYEE
	public void registerEmployee(Object newRecruit) {
		companyEmployee.add(newRecruit);
	}

	// 2. PRINT ALL EMPLOYEES
	public void printAllEmployees() {

		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);
			
			print ("\n" + castemployee.toString() );

		}
	
	}

	// 3. PRINT A SPECIFIC EMPLOYEE
	public void printEmployee(String id) {
		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {
				print("\n");
				print("--------------------------- " + "\n");
				print(castemployee.getClassification() + " Data : " + "\n");

				if (companyEmployee.get(i) instanceof Employee) {
					Employee UpdatedCast = (Employee) companyEmployee.get(i);
					print(UpdatedCast.toString() + "\n");

				} else if (companyEmployee.get(i) instanceof Intern) {
					Intern UpdatedCast = (Intern) companyEmployee.get(i);
					print(UpdatedCast.toString() + "\n");

				} else if (companyEmployee.get(i) instanceof Manager) {
					Manager UpdatedCast = (Manager) companyEmployee.get(i);
					print(UpdatedCast.toString() + "\n");

				} else if (companyEmployee.get(i) instanceof Director) {
					Director UpdatedCast = (Director) companyEmployee.get(i);
					print(UpdatedCast.toString() + "\n");
				}
				
				print(" --------------------------- " + "\n");
				print("\n");
				return;
			}
		}
	}
	//END PRINT SPECIFIC EMPLOYEE//

	// 4. REMOVE AN EMPLOYEE
	public void removeEmployee(String id) {
		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {
				companyEmployee.remove(i);
				return;
			}
		}
	}
	//END REMOVE AN EMPLOYEE//

	// 5. UPDATE AN EMPLOYEE
	public void updateEmployee(String id, int choice, Object Change) {
		if (choice == 1) { // CHANGE THE NAME
			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {

					if (companyEmployee.get(i) instanceof Employee) {
						Employee UpdatedCast = (Employee) companyEmployee.get(i);
						UpdatedCast.setEmployeeName(Change.toString());
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Intern) {
						Intern UpdatedCast = (Intern) companyEmployee.get(i);
						UpdatedCast.setEmployeeName(Change.toString());
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Manager) {
						Manager UpdatedCast = (Manager) companyEmployee.get(i);
						UpdatedCast.setEmployeeName(Change.toString());
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Director) {
						Director UpdatedCast = (Director) companyEmployee.get(i);
						UpdatedCast.setEmployeeName(Change.toString());
						companyEmployee.set(i, UpdatedCast);
					}
				}
			}
		} else if (choice == 2) {// CHANGE THE SALARY
			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {

					if (companyEmployee.get(i) instanceof Employee) {
						Employee UpdatedCast = (Employee) companyEmployee.get(i);
						UpdatedCast.setGrossSalary((Double) Change);
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Intern) {
						Intern UpdatedCast = (Intern) companyEmployee.get(i);
						UpdatedCast.setGrossSalary((Double) Change);
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Manager) {
						Manager UpdatedCast = (Manager) companyEmployee.get(i);
						UpdatedCast.setGrossSalary((Double) Change);
						companyEmployee.set(i, UpdatedCast);

					} else if (companyEmployee.get(i) instanceof Director) {
						Director UpdatedCast = (Director) companyEmployee.get(i);
						UpdatedCast.setGrossSalary((Double) Change);
						companyEmployee.set(i, UpdatedCast);
					}
				}
			}
		} else if (choice == 3) { // CHANGE JOB SPECIFIC ATTRIBUTES

			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {
					Director UpdatedCast = (Director) companyEmployee.get(i);
					UpdatedCast.setDegree(Change.toString());
					;
					companyEmployee.set(i, UpdatedCast);
				}
			}
			// : Change department for director
		} else if (choice == 4) {

			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {
					Director UpdatedCast = (Director) companyEmployee.get(i);
					UpdatedCast.setDepartment(Change.toString());
					companyEmployee.set(i, UpdatedCast);
				}
			}
		} else if (choice == 5) {

			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {
					Manager UpdatedCast = (Manager) companyEmployee.get(i);
					UpdatedCast.setDegree(Change.toString());
					companyEmployee.set(i, UpdatedCast);
				}
			}
		} else if (choice == 6) {

			for (int i = 0; i < companyEmployee.size(); i++) {

				EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

				if (castemployee.getId().equals(id)) {
					Intern UpdatedCast = (Intern) companyEmployee.get(i);
					UpdatedCast.setGpa((int) Change);
					companyEmployee.set(i, UpdatedCast);
				}
			}
		}

	}
	//END UPDATE EMPLOYEE//

	// 6. PROMOTE/DEMOTE EMPLOYEE - 4 methods //
	public void promoteToEmployee(String id) {
		// castEmployee = ((Employee) castEmployee); // ???
		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {
				Employee employee = new Employee(id, castemployee.getEmployeeName(), castemployee.getGrossSalary());
				removeEmployee(id);
				companyEmployee.add(employee);
			}
		}
	}
	public void promoteToIntern(String id, int gpa) {
		for (int i = 0; i < companyEmployee.size(); i++) {
			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {

				Intern intern = new Intern(id, castemployee.getEmployeeName(), castemployee.getGrossSalary(), gpa);
				removeEmployee(id);
				companyEmployee.add(intern);
			}
		}
	}
	public void promoteToMananger(String id, String degree) {
		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {

				Manager manager = new Manager(id, castemployee.getEmployeeName(), castemployee.getGrossSalary(),
						degree);
				removeEmployee(id);
				companyEmployee.add(manager);
			}
		}
	}
	public void promoteToDirector(String id, String degree, String department) {
		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id)) {

				Director director = new Director(id, castemployee.getEmployeeName(), castemployee.getGrossSalary(),
						department, degree);
				removeEmployee(id);
				companyEmployee.add(director);
			}
		}
	}	
	//END PROMOTE/DEMOTE EMPLOYEE - 4 methods//

	// 7. SET DIRECTORS BENEFIT
	public void setDirectorBenefits(double amount) {
		for (int i = 0; i < companyEmployee.size(); i++) {

			if (companyEmployee.get(i) instanceof Director) {
				Director castemployee = (Director) companyEmployee.get(i);
				castemployee.setDirectorsBenefit(amount);
			}
		}
	}

	// 8. PRINT TOTAL EXPENSES FOR THE COMPANY
	public double getTotalExpences() {

		double totalMoney = 0;

		for (int i = 0; i < companyEmployee.size(); i++) {
			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);
			totalMoney += castemployee.getGrossSalary();
		}
		
		return totalMoney;
		
	}
	public double getTotalNet() {

		double totalMoney = 0;

		for (int i = 0; i < companyEmployee.size(); i++) {
			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);
			totalMoney += castemployee.getNetSalary();
		}
		
		return totalMoney;
		
	}

	// 9. PRINT TOTAL NUMBER OF EMPLOYEES
	public int getNumberOfEmployees() {
		return companyEmployee.size();

	}

	// : Sorting algorithm
	public void Sort() {
		//: Pointer
		for (int i = 0; i < companyEmployee.size(); i++) {
			
			for (int z = i + 1; z < companyEmployee.size(); z++) {
				// : Gets the name of the employees
				String compare1 = ((EmployeeDefinition) companyEmployee.get(i)).getEmployeeName().toString();
				String compare2 = ((EmployeeDefinition) companyEmployee.get(z)).getEmployeeName().toString();
				// : compares the two
				int compare = compare1.compareTo(compare2);
				if (compare < 0) {
					// : If a is compare1 is bigger than compare2
					// : Does nothing and moves on
				} else if (compare > 0) {
					// : If a is compare2 is bigger than compare1
					// : Swaps the elements
					swap(z , i);
				} else {
					// : If a is compare1 is the same as compare2
					// : Checks the gross income
					// : Gets the name of the employees
					double compareInt1 = ((EmployeeDefinition) companyEmployee.get(i)).getGrossSalary();
					double compareInt2 = ((EmployeeDefinition) companyEmployee.get(z)).getGrossSalary();

					if (compareInt2 > compareInt1) {
						// : If a is compare2 is bigger than compare1
						// : Swaps the elements
						swap(z , i);
					}
				}
			}
		}
	}
	//END sorting algorithm//

	//: Swaps two elements in the array
	public void swap(int a , int b) {
		
		Object temp = companyEmployee.get(a);
		companyEmployee.set(a, companyEmployee.get(b));
		companyEmployee.set(b, temp);
		
	}
	
	//: a fuctions if an employee exists
	public boolean findEmployee(String id) {

		boolean found = false;

		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id))
				found = true;
		}
		return found;
	}

	//: gets an employee 
	public EmployeeDefinition getEmployee(String id) {

		EmployeeDefinition returnEmployee = null;

		for (int i = 0; i < companyEmployee.size(); i++) {

			EmployeeDefinition castemployee = (EmployeeDefinition) companyEmployee.get(i);

			if (castemployee.getId().equals(id))
				returnEmployee = castemployee;
		}
		return returnEmployee;
	}
	
	//: prints in the console
	public void print(Object o) {
		System.out.print(o);
	}
	
	
}



