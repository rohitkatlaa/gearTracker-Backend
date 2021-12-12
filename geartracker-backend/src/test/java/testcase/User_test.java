package testcase;
import com.geartracker.geartracker_backend.*;
import java.time.LocalDate;

public class User_test{
	public static void main(String[] args) {
		User usr = new User();
		
		//Test case for getId and setId
		usr.setId("F1");
		if(usr.getId().equals("F1")) {
			System.out.println("Test case for getId and setId passed.");
		}
		else {
			System.out.println("Test case for getId and setId failed. Output is " + usr.getId());
		}
		
		//Test case for getFine, addFine, setFine
		usr.setFine(5);
		usr.addFine(10);
		
		if(usr.getFine() == 15) {
			System.out.println("Test case for getFine and setFine passed.");
		}
		else {
			System.out.println("Test case for getFine and setFine failed. Output is " + usr.getFine());
		}
		
		//All other getters and setters are similar and trivial so have been excluded here. 
		
	}
}