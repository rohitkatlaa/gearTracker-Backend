package testcase;

import com.geartracker.geartracker_backend.*;

public class EquipmentRepository_test{
	public static void main(String[] args) {
		EquipmentRepository eq_repo = EquipmentRepository.getInstance();
		
		//Tests for getEquipmentId()
		if(eq_repo.getEquipmentId(1).equalsIgnoreCase("F1")) {
			System.out.println("Test case 1 for getEquipmentId() passed.");
		}
		else {
			System.out.println("Test case 1 for getEquipmentId() failed, output is " + eq_repo.getEquipmentId(1));
		}
		
		if(eq_repo.getEquipmentId(1000).equalsIgnoreCase("fail")) {
			System.out.println("Test case 2 for getEquipmentId() passed.");
		}
		else {
			System.out.println("Test case 2 for getEquipmentId() failed, output is " + eq_repo.getEquipmentId(1000));
		}
		
		//Tests for getSurrogateId()
		System.out.println("\n");
		if(eq_repo.getSurrogateId("F1") == 1) {
			System.out.println("Test case 1 for getSurrogateId() passed.");
		}
		else {
			System.out.println("Test case 1 for getSurrogateId() failed, output is " + eq_repo.getSurrogateId("F1"));
		}
		
		if(eq_repo.getSurrogateId("F3") == -1) {
			System.out.println("Test case 2 for getSurrogateId() passed.");
		}
		else {
			System.out.println("Test case 2 for getSurrogateId() failed, output is " + eq_repo.getSurrogateId("F3"));
		}
		
		//Tests for createEquipment
		System.out.println("\n");
		Equipment new_eq1 = new Equipment("Test1", "Reserved Equipment", "available", true,"Good"); 
		Equipment new_eq2 = new Equipment("Test2", "Unreserved Equipment", "available", false,"Good");
		
		eq_repo.createEquipment(new_eq1);
		eq_repo.createEquipment(new_eq2);
		
		if(eq_repo.getEquipmentById("Test1").getName().equals("Reserved Equipment")) {
			System.out.println("Test case 1 for createEquipment() passed.");
		}
		else {
			System.out.println("Test case 1 for createEquipment() failed, output is " + eq_repo.getEquipmentById("Test1").getName());
		}
		
		if(eq_repo.getEquipmentById("Test2").getName().equals("Unreserved Equipment")) {
			System.out.println("Test case 2 for createEquipment() passed.");
		}
		else {
			System.out.println("Test case 2 for createEquipment() failed, output is " + eq_repo.getEquipmentById("Test2").getName());
		}
		
		//Tests for editEquipment
		System.out.println("\n");
		new_eq1.setName("Reserved Eq");
		new_eq2.setName("Unreserved Eq");
		
		eq_repo.editEquipment("Test1", new_eq1);
		eq_repo.editEquipment("Test2", new_eq2);
		
		if(eq_repo.getEquipmentById("Test1").getName().equals("Reserved Eq")) {
			System.out.println("Test case 1 for editEquipment() passed.");
		}
		else {
			System.out.println("Test case 1 for editEquipment() failed, output is " + eq_repo.getEquipmentById("Test1").getName());
		}
		
		if(eq_repo.getEquipmentById("Test2").getName().equals("Unreserved Eq")) {
			System.out.println("Test case 2 for editEquipment() passed.");
		}
		else {
			System.out.println("Test case 2 for editEquipment() failed, output is " + eq_repo.getEquipmentById("Test2").getName());
		}
		
		//Tests for editEquipmentStatus
		System.out.println("\n");
		eq_repo.editEquipmentStatus("Test1", "issued");
		
		if(eq_repo.getEquipmentById("Test1").getStatus().equals("issued")) {
			System.out.println("Test case for editEquipmentStatus passed.");
		}
		else {
			System.out.println("Test case for editEquipmentStatus failed, output is " + eq_repo.getEquipmentById("Test1").getStatus());
		}
		
		//Tests for deleteEquipment
		System.out.println("\n");
		
		if(eq_repo.deleteEquipment("Test1").equals("success")) {
			System.out.println("Test case 1 for deleteEquipment passed.");
		}
		else {
			System.out.println("Test case 1 for deleteEquipment failed");
		}
		
		if(eq_repo.deleteEquipment("Test2").equals("success")) {
			System.out.println("Test case 2 for deleteEquipment passed.");
		}
		else {
			System.out.println("Test case 2 for deleteEquipment failed");
		}
		
		
	}
}