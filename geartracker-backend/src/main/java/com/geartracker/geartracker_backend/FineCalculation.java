package com.geartracker.geartracker_backend;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import com.sendemail.SendMail;

public class FineCalculation{
	private EquipmentRepository eq_repo;
	private RequestRepository req_repo;
	private UserRepository usr_repo;
	
	public FineCalculation(EquipmentRepository eq_repo, RequestRepository req_repo, UserRepository usr_repo){
		this.eq_repo = eq_repo;
		this.req_repo = req_repo;
		this.usr_repo = usr_repo;
	}
	
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

	public void computeFine(Request req){
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
				req.setStatus("Closed");
				req_repo.editRequest(req.getId(), req);
			}
			//return usr_repo;
			//else{
			//	return null;
			//}
			//System.out.println(usr.getFine());		
			//return usr;
		}
	}
	
	public void scanRequest() {
		for(Request req:req_repo.getRequestsList()) {
			computeFine(req);
		}
	}
	/*public void scanRequest(UserRepository usr_repo, RequestRepository req_repo) {
		for(int i=0; i< req_repo.getRequestsList().size(); i++) {
			usr_repo = computeFine(usr_repo, req_repo);
		}
	}*/
	
	public void scheduleScan(){
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		//Call function on each request from the request table.
		Runnable scanner = () -> scanRequest();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, 1, 90, TimeUnit.SECONDS);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, 1, TimeUnit.HOURS);
	}
	
	public static void main(String[] args){
		User usr = new User();
		usr.setId("2");
		usr.setEmail("hemanthx9@gmail.com");
		Equipment eq = new Equipment("1","Badminton", "Issued", false, "Sturdy");
		Request req = new Request(0, 1,2, "Issued", LocalDate.now().minusDays(10),null);
		
		UserRepository usr_repo = new UserRepository();
		usr_repo.createUser(usr);
		
		EquipmentRepository eq_repo = new EquipmentRepository();
		eq_repo.createEquipment(eq);
		
		RequestRepository req_repo = new RequestRepository();
		req_repo.createRequest(req);
		
		FineCalculation fineobj = new FineCalculation(eq_repo, req_repo, usr_repo);
		fineobj.scheduleScan();
	}
}
