package testcase;
import com.geartracker.geartracker_backend.*;
import java.time.LocalDate;

public class Request_test{
	public static void main(String[] args) {
		Request req = new Request();
		
		//Test case for getRequestId and setRequestId
		req.setRequestId(1);
		if(req.getRequestId() == 1) {
			System.out.println("Test case for getRequest and setRequest passed.");
		}
		else {
			System.out.println("Test case for getRequest and setRequest failed.Output is " + req.getRequestId());
		}
		
		//Test case for getEquipmentSurrId and setEquipmentSurrId
		req.setEquipmentSurrId(2);
		if(req.getEquipmentSurrId() == 2) {
			System.out.println("Test case for getEquipmentSurr and setEquipmentSurr passed.");
		}
		else {
			System.out.println("Test case for getEquipmentSurr and setEquipmentSurr failed.Output is " + req.getEquipmentSurrId());
		}
		
		//Tested above getters and setters, as rest of such functions are trivial, will check non-trivial functions.
		req.setIssueDate(LocalDate.now().minusDays(10));
		req.setStatus("approved");
		
		if(req.daysOpen() == 10) {
			System.out.println("Test case 1 for daysOpen passed.");
		}
		else {
			System.out.println("Test case 1 for daysOpen failed, output is " + req.daysOpen());
		}
		
		req.setStatus("closed");
		req.setReturnDate(LocalDate.now().plusDays(10));
		
		if(req.daysOpen() == 20) {
			System.out.println("Test case 2 for daysOpen passed.");
		}
		else {
			System.out.println("Test case 2 for daysOpen failed, output is " + req.daysOpen());
		}
		
	}
}