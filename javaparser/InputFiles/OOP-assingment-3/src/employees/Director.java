package employees;

public class Director extends Manager implements EmployeeDefinition{
	
	private String classification = "Director";

	private double directorsBenefit; 	
	private String department; 

	public Director(String id, String employeeName, double grossSalaryinput, String department, String degree) {
		super(id, employeeName, grossSalaryinput, degree);
		this.department = department;	
	}
	
	public double getDirectorsBenefit() {
		return directorsBenefit;
	}
	public void setDirectorsBenefit(double directorsBenefits) {
		this.directorsBenefit = directorsBenefits;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) { 
		this.department = department;
	}
	
	@Override
	public double getGrossSalary() {
		return super.getGrossSalary() + directorsBenefit;
	}
	public double getOriginalGrossSalary() {
		return super.getGrossSalary();
	}

	@Override
	public double getNetSalary() {
		
		double netSalary = 0;
		double lowIncome = 30000;
		double highIncome = 50000;
		
		double originalTax = 0.1;
		double lowIncomeTax = 0.2;
		double highIncomeTax = 0.4;
		
		if(this.getGrossSalary() < lowIncome) {//??
			netSalary = (this.getGrossSalary()  - (this.getGrossSalary() * originalTax)); //changed this
		} else if (this.getGrossSalary() >= lowIncome && this.getGrossSalary() < highIncome) {
			netSalary = (this.getGrossSalary()) - (((this.getGrossSalary()) * lowIncomeTax));
		} else {
			double first30 = (lowIncome * 0.8 ) ;
			double after30 = ((this.getGrossSalary() - lowIncome)  * 0.6);
			netSalary = (after30 +  first30);		
		}	
		return netSalary;	
	}
	
	@Override
	public String getClassification() {
		return this.classification;
	}

	@Override
	public String toString() {
		return "Director ID: " + super.getId() + ", Name: " + super.getEmployeeName() + "Original gross salary: " + this.getOriginalGrossSalary() + ", Gross salary with bonus: " + this.getGrossSalary() + ", Classification: " + this.classification + ", Degree: "+ super.getDegree()+", Department: "
				+ this.department;
	}
}
