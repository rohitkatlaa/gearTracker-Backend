package testcase;
import com.geartracker.geartracker_backend.*;

public class Equipment_test{
	public static void main(String[] args) {
		Equipment eq = new Equipment();
		
		//Test case for getId and setId
		eq.setId("F1");
		if(eq.getId().equals("F1")) {
			System.out.println("Test case for getId and setId passed.");
		}
		else {
			System.out.println("Test case for getId and setId failed. Output is " + eq.getId());
		}
		
		//Test case for getFine, addFine, setFine
		eq.setName("Baseball");
		if(eq.getName().equals("Baseball")) {
			System.out.println("Test case for getName and setName passed.");
		}
		else {
			System.out.println("Test case for getName and setName failed. Output is " + eq.getName());
		}
		
		//All other getters and setters are similar and trivial so have been excluded here. 
		
	}
}
