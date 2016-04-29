package cyclients.psIterator;
/**
 * 
 */

/**
 * @author Sony
 *
 */
public class Employee {
	private int empId;
	private String empName;
	private String deptName;
	private int salary;
	
	public Employee() {
		
	}
	
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public String toString() {
		return empId+","+empName+","+deptName+","+salary;
	}
}
