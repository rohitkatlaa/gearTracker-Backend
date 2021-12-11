package testcase;

import com.geartracker.geartracker_backend.*;

public class UserRepository_test{
	public static void main(String[] args) {
		UserRepository usr_repo = UserRepository.getInstance();
		
		//Tests for getUserId()
		if(usr_repo.getUserId(1).equalsIgnoreCase("admin1")) {
			System.out.println("Test case 1 for getUserId() passed.");
		}
		else {
			System.out.println("Test case 1 for getUserId() failed, output is " + usr_repo.getUserId(1));
		}
		
		if(usr_repo.getUserId(1000).equalsIgnoreCase("fail")) {
			System.out.println("Test case 2 for getUserId() passed.");
		}
		else {
			System.out.println("Test case 2 for getUserId() failed, output is " + usr_repo.getUserId(1000));
		}
		
		//Tests for getSurrogateId()
		System.out.println("\n");
		if(usr_repo.getSurrogateId("admin1") == 1) {
			System.out.println("Test case 1 for getSurrogateId() passed.");
		}
		else {
			System.out.println("Test case 1 for getSurrogateId() failed, output is " + usr_repo.getSurrogateId("F1"));
		}
		
		if(usr_repo.getSurrogateId("F1000") == -1) {
			System.out.println("Test case 2 for getSurrogateId() passed.");
		}
		else {
			System.out.println("Test case 2 for getSurrogateId() failed, output is " + usr_repo.getSurrogateId("F3"));
		}
		
		//Tests for createUser
		System.out.println("\n");
		User new_usr1 = new User(); 
		User new_usr2 = new User();
		
		new_usr1.setId("Test1");
		new_usr1.setName("Sports User");
		new_usr1.setSportsStatus(true);
		
		new_usr2.setId("Test2");
		new_usr2.setName("Nonsports User");
		new_usr2.setSportsStatus(false);
		
		usr_repo.createUser(new_usr1);
		usr_repo.createUser(new_usr2);
		
		if(usr_repo.getUserById("Test1").getName().equals("Sports User")) {
			System.out.println("Test case 1 for createUser() passed.");
		}
		else {
			System.out.println("Test case 1 for createUser() failed, output is " + usr_repo.getUserById("Test1").getName());
		}
		
		if(usr_repo.getUserById("Test2").getName().equals("Nonsports User")) {
			System.out.println("Test case 2 for createUser() passed.");
		}
		else {
			System.out.println("Test case 2 for createUser() failed, output is " + usr_repo.getUserById("Test2").getName());
		}
		
		//Tests for editUser
		System.out.println("\n");
		new_usr1.setName("SportsUsr");
		new_usr2.setName("NonsportsUsr");
		
		usr_repo.editUser("Test1", new_usr1);
		usr_repo.editUser("Test2", new_usr2);
		
		if(usr_repo.getUserById("Test1").getName().equals("SportsUsr")) {
			System.out.println("Test case 1 for editUser() passed.");
		}
		else {
			System.out.println("Test case 1 for editUser() failed, output is " + usr_repo.getUserById("Test1").getName());
		}
		
		if(usr_repo.getUserById("Test2").getName().equals("NonsportsUsr")) {
			System.out.println("Test case 2 for editUser() passed.");
		}
		else {
			System.out.println("Test case 2 for editUser() failed, output is " + usr_repo.getUserById("Test2").getName());
		}
		
		//Tests for deleteUser
		System.out.println("\n");
		
		if(usr_repo.deleteUser("Test1").equals("success")) {
			System.out.println("Test case 1 for deleteUser passed.");
		}
		else {
			System.out.println("Test case 1 for deleteUser failed");
		}
		
		if(usr_repo.deleteUser("Test2").equals("success")) {
			System.out.println("Test case 2 for deleteUser passed.");
		}
		else {
			System.out.println("Test case 2 for deleteUser failed");
		}
		
		
	}
}