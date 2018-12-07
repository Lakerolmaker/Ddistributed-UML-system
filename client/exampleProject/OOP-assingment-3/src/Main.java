import java.util.ArrayList;
import java.util.Scanner;

import employees.*;

public class Main {

	public static void main(String[] args) {

		//: The scanner used in the menu
		Scanner sc = new Scanner(System.in);
		
		// The different class files used for changeing the files
		//: The menu class
		Menu menu = new Menu();
		//: The class that handles all the functionality
		ReusaxCorp reusaxCorp = new ReusaxCorp();
		
		//: Calls the menu annimation
		menu.printLogo();
		
		Main main = new Main();
		
		// : While-loop that keeps the program running forever.
		while (true) {
			// : Displays the menu
			System.out.print("\n" +
				      " ------------------------------------------ " + "\n"
					+ " |                 Menu                   | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 1 to register an employee        | " + "\n"
					+ " |--------------------------------------- | " + "\n"
					+ " | Press 2 to print all employees         | " + "\n"
					+ " |--------------------------------------- | " + "\n"
					+ " | Press 3 to print a specified employee  | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 4 to remove an employee          | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 5 to update an employee          | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 6 to change position of employee | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 7 to set director benefits       | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 8 to print total expenses        | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 9 to print number of employees   | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 10 to sort employees             | " + "\n"
					+ " ---------------------------------------- | " + "\n"
					+ " | Press 11 to quit the program           | " + "\n"
					+ " ---------------------------------------- | " + "\n" + " Input a choice : ");

			// : Gets the user input, with some fail safes
			Scanner menuScanner = new Scanner(System.in);
			int input = 0;
			try {
				input = menuScanner.nextInt();
			} catch (Exception e) {
			}

			//: Switch that handles all the cases
			switch (input) {

			//: New employee
			case (1):
				System.out.print("Enter the ID of the new employee: ");
				String employeeId = sc.next();

				if (reusaxCorp.findEmployee(employeeId) == true) {

					print("\n ------------------------------------ " + "\n" 
					      + " |     Error , ID already in use   | "+ "\n"
						+ " ------------------------------------ " + "\n" + "\n");
					// :Exits back to the main menu
					break;
				}

				System.out.println("Enter the position of the employee: (Available: 1.Regular, 2.Intern, 3.Manager, 4.Director.");
				int employeePosition = sc.nextInt();

				if (employeePosition == 1) {

					System.out.print("Enter the name of the regular employee: ");
					String employeeName = sc.next();
					System.out.print("Enter the gross salary of the regular employee: ");
					double employeesalary = sc.nextDouble();

					Employee createdEmpoyee = new Employee(employeeId, employeeName, employeesalary);
					reusaxCorp.registerEmployee(createdEmpoyee);

				} else if (employeePosition == 2) {

					System.out.print("Enter the name of the new intern: ");
					String employeeName = sc.next();
					System.out.print("Enter the gross salary of the new intern: ");
					double employeesalary = sc.nextDouble();
					System.out.print("Enter the GPA of the new intern : (Number from 1 to 10)");
					int gpa = sc.nextInt();

					Intern createdEmpoyee = new Intern(employeeId, employeeName, employeesalary, gpa);
					reusaxCorp.registerEmployee(createdEmpoyee);

				} else if (employeePosition == 3) {

					System.out.print("Enter the name of the new manager: ");
					String employeeName = sc.next();
					System.out.print("Enter the gross salary of the new manager: ");
					double employeesalary = sc.nextDouble();
					System.out.print("Enter the degree of the new manager ( Available : 1.BSc , 2.MSc , 3.PhD ) : ");
					int choice = sc.nextInt();

					String degree = "";

					if (choice == 1) {
						degree = "bsc";
					} else if (choice == 2) {
						degree = "msc";
					} else if (choice == 3) {
						degree = "phd";
					}

					Manager createdEmpoyee = new Manager(employeeId, employeeName, employeesalary, degree);
					reusaxCorp.registerEmployee(createdEmpoyee);
					
				} else if (employeePosition == 4) {

					System.out.print("Enter the name of the new director: ");
					String employeeName = sc.next();
					System.out.print("Enter the gross salary of the new director: ");
					double employeesalary = sc.nextDouble();
					System.out.print("Enter the degree of the new director: ( Available : 1.BSc , 2.MSc , 3.PhD ) ");
					int choice = sc.nextInt();

					String degree = "";

					if (choice == 1) {
						degree = "bsc";
					} else if (choice == 2) {
						degree = "msc";
					} else if (choice == 3) {
						degree = "phd";
					}

					System.out.println(
							"Enter the department of the new director: ( Available: Technical, Human resources, Business ) ");
					String department = sc.next();

					Director createdEmpoyee = new Director(employeeId, employeeName, employeesalary, department, degree);
					reusaxCorp.registerEmployee(createdEmpoyee);

					print( "\n" +
							" ------------------------------------ " + "\n" 
					      + " |     Success , employee created   | " + "\n"
						  + " ------------------------------------ " + "\n" + "\n");
				}
				break;
				
			//: Prints all employees
			case (2):
				
				// Print all employees sorted
				reusaxCorp.printAllEmployees();
			
				break;
				
			//: Prints a specific employee
			case (3):
				System.out.println("Enter the ID of the employee whose info you want to print: ");
				String id = sc.next();
				reusaxCorp.printEmployee(id);
				break;

			// Remove an employee
			case (4):
			
				System.out.println("Enter the ID of the employee whom you want to remove: ");
				id = sc.next(); // ??
				reusaxCorp.removeEmployee(id);
				break;
				
			// Update an employee
			case (5):
				
				System.out.println("Enter the ID of the employee you want to update:");
				String idChange = sc.next();
				// : Checks of the employee exits
				if (reusaxCorp.findEmployee(idChange) == false) {
					print(" ------------------------------------ " + "\n" 
					    + " |     Error , employee not found   | " + "\n"
					    + " ------------------------------------ " + "\n" + "\n");
					// :Exits back to the main menu
					break;
				}

				System.out.println("What would you like to change: ");
				System.out.println("1. Change employee name ");
				System.out.println("2. Change employee salary ");
				System.out.println("3. Change jobspecific attributes (tex if its a manager u can change its degree)");

				int userInp = sc.nextInt();

				if (userInp == 1) {
					System.out.print("Enter the new name of the employee: ");
					Scanner nameScanner = new Scanner(System.in);
					String employeeName = nameScanner.nextLine();
					reusaxCorp.updateEmployee(idChange, 1, employeeName);

				} else if (userInp == 2) {
					System.out.print("Enter the new salary of the employee: ");
					Scanner salaryScanner = new Scanner(System.in);
					double grossSalary = salaryScanner.nextDouble();
					reusaxCorp.updateEmployee(idChange, 2, grossSalary);

				} else if (userInp == 3) {

					String EmployeeClass =  reusaxCorp.getEmployee(idChange).getClassification();
					
					if (EmployeeClass == "Director") {
						// companyEmployee= new Director(companyEmployee);
						System.out.println("Select what you want to change for this director: ");
						System.out.println("1. Change degree");
						System.out.println("2. Change department");

						int directorScan = sc.nextInt();

						if (directorScan == 1) {
							System.out.println("Input new degree: (Available: 1.BSc 2.MSc 3.PhD) ");
							String degreeInput = "";
							int choice = sc.nextInt();

							if (choice == 1) {
								degreeInput = "bsc";
							} else if (choice == 2) {
								degreeInput = "msc";
							} else if (choice == 3) {
								degreeInput = "phd";
							}

							reusaxCorp.updateEmployee(idChange, 3, degreeInput);

						} else if (directorScan == 2) {
							System.out.println("Input new department : ");
							Scanner depertmentScan = new Scanner(System.in);
							String depertmentInput = depertmentScan.nextLine();
							reusaxCorp.updateEmployee(idChange, 4, depertmentInput);
						}

					} else if (EmployeeClass == "Manager") {
						System.out.println("Select what you want to change for this manager:  ");
						System.out.println("1. Change degree");

						int managerScan = sc.nextInt();

						if (managerScan == 1) {
							System.out.println("Input new degree: (Available: 1.BSc 2.MSc 3.PhD) ");
							// Scanner degreeScan = new Scanner(System.in);
							String degreeInput = "";
							int choice = sc.nextInt();

							if (choice == 1) {
								degreeInput = "bsc";
							} else if (choice == 2) {
								degreeInput = "msc";
							} else if (choice == 3) {
								degreeInput = "phd";
							}
							reusaxCorp.updateEmployee(idChange, 5, degreeInput);
						}

					} else if (EmployeeClass == "Intern") {

						System.out.println("Select what you want to change for this intern: ");
						System.out.println("1. Change GPA");

						int internscan = sc.nextInt();

						if (internscan == 1) {
							System.out.println("Input new GPA (Number between 1 to 10): ");
							// Scanner GpaScan = new Scanner(System.in);
							int gpaInpiut = sc.nextInt();
							reusaxCorp.updateEmployee(idChange, 6, gpaInpiut);
						}
					}
				}
				break;
			
			// Promotion
			case (6):
				System.out.println("Enter the ID of the employee you want to change position: ");
				id = sc.next(); // make a methods to fetch the object
				System.out.println("Enter the position you want to change it to: ");
				System.out.println("Available: 1.Regular, 2.Intern, 3.Manager, 4.Director");
				// Scanner ChoiceScan = new Scanner(System.in);
				int newPosition = sc.nextInt();
				// ReusaxCorp.updateEmployee(employeeId, newPosition);
				if (newPosition == 1) { // regular
					// put fetched object here
					reusaxCorp.promoteToEmployee(id);
					System.out.println("Changed to regular employee");

				} else if (newPosition == 2) {// intern
					System.out.println("Enter the GPA of the intern (number between 0-10");
					int gpa = sc.nextInt();
					reusaxCorp.promoteToIntern(id, gpa);
					System.out.println("Changed to intern");

				} else if (newPosition == 3) {// manager
					System.out.println("Enter the degree of the manager : ( Available : 1.BSc , 2.MSc , 3.PhD )");
					String degree = "";

					int choice = sc.nextInt();

					if (choice == 1) {
						degree = "bsc";
					} else if (choice == 2) {
						degree = "msc";
					} else if (choice == 3) {
						degree = "phd";
					}

					reusaxCorp.promoteToMananger(id, degree);
					System.out.println("Changed to manager");

				} else if (newPosition == 4) {// director
					System.out.println("Enter the degree of the director : ( Available : 1.BSc , 2.MSc , 3.PhD )");

					String degree = "";

					int choice = sc.nextInt();

					if (choice == 1) {
						degree = "bsc";
					} else if (choice == 2) {
						degree = "msc";
					} else if (choice == 3) {
						degree = "phd";
					}

					System.out.println(
							"Enter the department of the director : ( Available: Technical, Human resources, Business )");
					String department = sc.next();
					reusaxCorp.promoteToDirector(id, degree, department);
					System.out.println("Changed to director");
				}
				break;
			
			// : Change director bennefits
			case (7):
				System.out.print("Enter the new directors benefit: ");
				double amount = sc.nextDouble();
				reusaxCorp.setDirectorBenefits(amount);	
				   		print("\n" +
				   			  " ------------------------------------ " + "\n" 
						    + " |  Success , Director Benefits set | " + "\n"
						    + " ------------------------------------ " + "\n" + "\n");
				break;
				
			// : Prints totall expeonses
			case (8):
				// Print total expenses!!!
				System.out.println("What do you want to check?");
				System.out.println("1. Total gross 2. Total net");
				int choice = sc.nextInt();
				
				if(choice == 1) {
					Double totalGross = reusaxCorp.getTotalExpences();	
					print(" ------------------------------------ " + "\n" 
						+ " |  Total expenses : " + totalGross + "\n"
						+ " ------------------------------------ " + "\n" + "\n");		
					
				}else if(choice == 2) {
					Double totalNet = reusaxCorp.getTotalNet();	
					print(" ------------------------------------ " + "\n" 
							+ " |  Total expenses : " + totalNet + "\n"
							+ " ------------------------------------ " + "\n" + "\n");	
				}			
				break;
				
			//: Prints number of employees
			case (9):
				// PRINT NUMBER OF EMPLOYEEES
			int companySize = reusaxCorp.getNumberOfEmployees();

			print(" --------------------------- " + "\n");
			print(" Number of employees : " + companySize);
			print(" --------------------------- " + "\n");	
				break;
				
			//: Sorts employees
			case (10): 

				reusaxCorp.Sort();
			
			    print(" ------------------------------------ " + "\n" 
				    + " |         Employees Sorted         | " + "\n"
				    + " ------------------------------------ " + "\n" + "\n");
				break;
			
			//: Exits the program
			case (11):
				// : Exits the program
				System.exit(0);
				break;
				
				default:
				print("\n" + " ------------------------------------ " + "\n" 
				           + " |    Error , not a valid option    | " + "\n"
					       + " ------------------------------------ " + "\n" + "\n");
				break;
			}
		}
	}

	public static void print(Object o) {
		System.out.print(o);
	}

	public static void cls() {
		for (int i = 0; i < 100; i++) {
			print("\n");
		}
	}
}
