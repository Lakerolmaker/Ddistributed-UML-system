package employees;

public class Manager extends Employee implements EmployeeDefinition {

	private String degree;

	private double bonus;
	private double grossSalary;
	private String classification = "Manager";

	public Manager(String id, String employeeName, double grossSalaryInput, String degree) {
		super(id, employeeName, grossSalaryInput);
		this.grossSalary = grossSalaryInput;
		this.degree = degree;
		this.bonus = getManagerBonus();
	}

	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
		this.bonus = getManagerBonus();
	}

	public double getGrossSalary() {
		return super.getGrossSalary() + getManagerBonus();
	}
	
	public double getNetSalary() { 
		double netSalary = this.getGrossSalary() - (this.getGrossSalary() * 0.1);
		return netSalary;
	}

	public double getManagerBonus() {
		if (degree.equals("bsc")) {
			bonus = super.getGrossSalary() * 0.1;
		} else if (degree.equals("msc")) {
			bonus = super.getGrossSalary() * 0.2;
		} else if (degree.equals("phd")) {
			bonus = super.getGrossSalary() * 0.35;
		}
		return bonus;
	}

	@Override
	public String getClassification() {
		return this.classification;
	}

	@Override
	public String toString() {
		return "Manager ID: " + super.getId() + ", Name: " + super.getEmployeeName() + ", Original Gross Salary: "
				+ super.getGrossSalary() + ", Gross Salary after added bonus: " + this.getGrossSalary() + ", Degree: "
				+ this.degree + ", Bonus: " + this.bonus + ", Classification: " + this.classification;
	}	
}
