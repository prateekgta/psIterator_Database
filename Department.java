package cyclients.psIterator;
/**
 * 
 */

/**
 * @author Sony
 *
 */
public class Department {
	private String dName;
	private String mName;
	
	public Department() {
		
	}
	
	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	@Override
	public String toString() {
		return "Department [dName=" + dName + ", mName=" + mName + "]";
	}
	
	
}
