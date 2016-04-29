package cyclients.psIterator;
import java.util.Arrays;

/**
 * 
 */

/**
 * @author Sony
 *
 */
public class EmployeeTuple {
	
	public static Employee convertToEmployee(byte[] employeeTuple) {
		Employee employee = new Employee();
		
		employee.setEmpId(IntegerBytes.getInteger(Arrays.copyOfRange(employeeTuple, 0, 4)));
		employee.setEmpName(StringBytes.getString(Arrays.copyOfRange(employeeTuple, 4, 14)));
		employee.setDeptName(StringBytes.getString(Arrays.copyOfRange(employeeTuple, 14, 24)));
		employee.setSalary(IntegerBytes.getInteger(Arrays.copyOfRange(employeeTuple, 24, 28)));
		
		return employee;		
	}
	
	public static Employee convertToByteArray(Employee employee) {
		byte[] employeeBytes = new byte[28];

		try{
			System.arraycopy(IntegerBytes.getByteArray(employee.getEmpId()), 0, employeeBytes, 0, 4);
			System.arraycopy(StringBytes.getByteArray(employee.getEmpName()), 0, employeeBytes, 4, 10);
			System.arraycopy(StringBytes.getByteArray(employee.getDeptName()), 0, employeeBytes, 14, 10);
			System.arraycopy(IntegerBytes.getByteArray(employee.getSalary()), 0, employeeBytes, 24, 4);
		}
		catch(Exception ex) {
			System.out.println("Exception occured:"+ex.getMessage());
		}
		return employee;		
	}
	
	public static Department convertToDepartment(byte[] deptTuple) {
		Department dept = new Department();
		
		dept.setdName(StringBytes.getString(Arrays.copyOfRange(deptTuple, 0, 10)));
		dept.setmName(StringBytes.getString(Arrays.copyOfRange(deptTuple, 10, 20)));
		
		return dept;		
	}
/**
	** Method Used By Adopter Class for converting Emp and Dept
	**/
	
	public static Department convertToDept(byte[] deptTuple) {
		Department dept = new Department();
		
		dept.setdName(StringBytes.getString(Arrays.copyOfRange(deptTuple, 0, 10)));
		dept.setmName(StringBytes.getString(Arrays.copyOfRange(deptTuple, 10, 20)));
		
		return dept;		
	}
	
	public static Employee convertToEmp(byte[] empTuple) {
		Employee employee = new Employee();
		
		employee.setEmpName(StringBytes.getString(Arrays.copyOfRange(empTuple, 0, 10)));
		employee.setDeptName(StringBytes.getString(Arrays.copyOfRange(empTuple, 10, 20)));
		
		return employee;		
	}
}
