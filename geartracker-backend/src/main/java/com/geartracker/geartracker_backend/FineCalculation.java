package com.geartracker.geartracker_backend;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;



//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import com.sendemail.SendMail;

public class FineCalculation{

	public long daysOpen(Request req){
		if(req.getStatus().equalsIgnoreCase("Issued")){
			return DAYS.between(req.getIssueDate(), LocalDate.now());
		}
		else if(req.getStatus().equalsIgnoreCase("Closed")){
			return DAYS.between(req.getIssueDate(), req.getReturnDate());
		}
		else{
			return -1;
		}
	}

	public UserRepository computeFine(EquipmentRepository eq_repo, UserRepository usr_repo, Request req){
		//Retrieving equipment from database.
		//EquipmentRepository eq_repo = new EquipmentRepository();
		if(req.getStatus().equalsIgnoreCase("Issued")) {
			String eq_id = eq_repo.getEquipmentId(req.getEquipmentId()); //Using surrogate ID in request, get equipment ID.
			Equipment eq = eq_repo.getEquipmentById(eq_id); //Retrieve equipment.
			String status = eq.getStatus();

			//Retrieving user from database.
			//Standard rate is 5 rupees per day overdue. If the equipment is damaged, then instead add 100 rupees.

			//UserRepository usr_repo = new UserRepository();
			String usr_id = usr_repo.getUserId(req.getUserId());
			User usr = usr_repo.getUserById(usr_id);
		
			if(status.equalsIgnoreCase("Issued")){
				if(daysOpen(req) >= 6) {
					SendMail.sendmail(usr.getEmail(), "geartrackertesting486@gmail.com", "geartrackertesting684", "Reminder to return equipment", "Please return equipment possessed by you. Your current fine is " + Integer.toString(usr.getFine()) + ".");
				}
			
				if(daysOpen(req) > 7){
					usr.addFine(5);
					usr_repo.editUser(usr_id, usr);
				}
			}

			else if(status.equalsIgnoreCase("Defective")){			//To represent when equipment is lost or broken.
				usr.addFine(100);
				usr_repo.editUser(usr_id, usr);
				SendMail.sendmail(usr.getEmail(), "geartrackertesting486@gmail.com", "geartrackertesting684", "Fine for damaging equipment", "Your act of making our equipment unusable is unforgivable. Don't repeat this. Your current fine is " + Integer.toString(usr.getFine()) + ".");
			
				//Make change in database now. Use the req ID of this request, search in database and close request.
			}
			return usr_repo;
			//else{
			//	return null;
			//}
			//System.out.println(usr.getFine());		
			//return usr;
		}
	}
	
	public void scanRequest() {
		;
	}
	/*public void scanRequest(UserRepository usr_repo, RequestRepository req_repo) {
		for(int i=0; i< req_repo.getRequestsList().size(); i++) {
			usr_repo = computeFine(usr_repo, req_repo);
		}
	}*/
	
	public void scanRequest(RequestRepository req_repo){
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		//Call function on each request from the request table.
		Runnable scanner = () -> scanRequest();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, 1, 90, TimeUnit.SECONDS);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, 1, TimeUnit.HOURS);
	}
	public static void main(String[] args){
		//Request req = new Request(0, 1,2, "Issued", LocalDate.now().minusDays(10), LocalDate.now());
		//FineCalculation fineobj = new FineCalculation();
		//fineobj.computeFine(req);
	}

}