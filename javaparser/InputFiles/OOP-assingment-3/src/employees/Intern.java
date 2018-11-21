package employees;

public class Intern extends Employee implements EmployeeDefinition{
	

	private double newGrossSalary;	
	private String classification = "Intern";
	private int gpa;

	public Intern(String id, String employeeName, double grossSalary, int gpa) {
		super(id, employeeName, grossSalary);
		this.gpa = gpa;
		this.newGrossSalary = getGrossSalary(); //??
	}
	
	public int getGpa() {
		return gpa;
	}
	public void setGpa(int gpa) {
		this.gpa = gpa;
	}

	public double getGrossSalary() {	
		if (this.gpa <= 5) { // have this.getgpa() instead?
			newGrossSalary = 0; 
		} else if (this.gpa > 5 && gpa < 8) {
			newGrossSalary = super.getGrossSalary();
		} else if (this.gpa >= 8) {
			int bonus = 1000;
			newGrossSalary = super.getGrossSalary() + bonus;	
		} 
		return newGrossSalary;
	}

	@Override
	public double getNetSalary() {
		double netSalary = this.getGrossSalary(); //??
		return netSalary;
	}

	@Override
	public String getClassification() {
		return this.classification;
	}
	
	@Override
	public String toString() {
		return "Intern ID: " + super.getId() + ", Name: " + super.getEmployeeName() + ", Original Gross Salary: " + super.getGrossSalary()
				+ ", Gross Salary Depending on GPA: " + this.getGrossSalary() + ", Classification: " + this.classification + ", GPA: " + this.gpa;
	}
}
