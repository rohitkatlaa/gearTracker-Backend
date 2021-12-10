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
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)){
			return DAYS.between(req.getIssueDate(), LocalDate.now());
		}
		else if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_CLOSED)){
			return DAYS.between(req.getIssueDate(), req.getReturnDate());
		}
		else{
			return -1;
		}
	}

	public void computeFine(Request req){
		//Retrieving equipment from database.
		//EquipmentRepository eq_repo = new EquipmentRepository();
		System.out.println(req.getRequestId());
		System.out.println(req.getStatus());
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
			String eq_id = eq_repo.getEquipmentId(req.getEquipmentSurrId()); //Using surrogate ID in request, get equipment ID.
			Equipment eq = eq_repo.getEquipmentById(eq_id); //Retrieve equipment.
			String status = eq.getStatus();

			//Retrieving user from database.
			//Standard rate is 5 rupees per day overdue. If the equipment is damaged, then instead add 100 rupees.

			String usr_id = usr_repo.getUserId(req.getUserSurrId());
			User usr = usr_repo.getUserById(usr_id);
			System.out.println(usr_id);
		
			if(status.equalsIgnoreCase(Constants.EQUIPMENT_STATUS_ISSUED)){
				System.out.println(daysOpen(req));
				if(daysOpen(req) >= Constants.FINE_CUTOFF_DAYS-1) {
					
					SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_LATE_SUBJECT, Constants.MAIL_LATE_BODY + Integer.toString(usr.getFine()) + ".");
				}
			
				if(daysOpen(req) > Constants.FINE_CUTOFF_DAYS){
					usr.addFine(Constants.FINE_LATE);
					usr_repo.editUser(usr_id, usr);
				}
			}

			else if(status.equalsIgnoreCase(Constants.EQUIPMENT_STATUS_LOST) || status.equalsIgnoreCase(Constants.EQUIPMENT_STATUS_BROKEN)){			//To represent when equipment is lost or broken.
				usr.addFine(Constants.FINE_DEFECTIVE);
				usr_repo.editUser(usr_id, usr);
				SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_DAMAGE_SUBJECT, Constants.MAIL_DAMAGE_BODY + Integer.toString(usr.getFine()) + ".");
				
				//Make change in database now. Use the req ID of this request, search in database and close request.
				req.setStatus(Constants.REQUEST_STATUS_CLOSED);
				req_repo.editRequest(req.getRequestId(), req);
			}
		}
	}
	
	public void scanRequest() {
		for(Request req:req_repo.getRequestsList()) {
			computeFine(req);
		}
	}
	
	public void scheduleScan(){
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		//Call function on each request from the request table.
		Runnable scanner = () -> scanRequest();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, Constants.SCAN_INITIAL_DELAY, Constants.SCAN_PERIOD, Constants.SCAN_TIMEUNIT);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, Constants.SCAN_DURATION, Constants.SCAN_DURATIONUNIT);
	}
	
	public static void main(String[] args){
		
		
		UserRepository usr_repo = new UserRepository();
		usr_repo.getUserById("test");
		
		EquipmentRepository eq_repo = new EquipmentRepository();
		//eq_repo.editEquipmentStatus("CBA1", Constants.EQUIPMENT_STATUS_LOST);
		eq_repo.getEquipmentById("F1");
		
		RequestRepository req_repo = new RequestRepository();
		req_repo.getRequestById(1);
		
		FineCalculation fineobj = new FineCalculation(eq_repo, req_repo, usr_repo);
		fineobj.scheduleScan();
	}
}
