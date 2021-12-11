package com.geartracker.geartracker_backend;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.sendemail.SendMail;

public abstract class FineCalculation{
	protected EquipmentRepository eq_repo = EquipmentRepository.getInstance();
	protected RequestRepository req_repo = RequestRepository.getInstance();
	protected UserRepository usr_repo = UserRepository.getInstance();
	
	//protected static FineCalculation fineobj = null;

	public void scanRequest(List<Request> reqs){
		for(Request req: reqs) {
			calc(req);
		}
	}
	
	public abstract void calc(Request req);
	//public abstract void computeFine();
	public static void main(String[] args){
		LateFineCalculation.getInstance().scheduleScan();
	}
}

class LateFineCalculation extends FineCalculation{
	private static LateFineCalculation fineobj = null;

	private LateFineCalculation(){
		/*this.eq_repo = EquipmentRepository.getInstance();
		this.req_repo = RequestRepository.getInstance();
		this.usr_repo = UserRepository.getInstance();*/
		
	}
	
	public static LateFineCalculation getInstance() {
		if(fineobj == null) {
			fineobj = new LateFineCalculation();
		}
		return fineobj;
	}

	public void calc(Request req){
		//Retrieving equipment from database.
		//EquipmentRepository eq_repo = new EquipmentRepository();
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
			//Retrieving user from database.
			String usr_id = usr_repo.getUserId(req.getUserSurrId());
			User usr = usr_repo.getUserById(usr_id);
			
			//Standard rate is 5 rupees per day overdue. If the equipment is damaged, then instead add 100 rupees.
			if(req.daysOpen() >= Constants.FINE_CUTOFF_DAYS-1) {
				
				SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_LATE_SUBJECT, Constants.MAIL_LATE_BODY + Integer.toString(usr.getFine()) + ".");
			}
		
			if(req.daysOpen() > Constants.FINE_CUTOFF_DAYS){
				usr.addFine(Constants.FINE_LATE);
				usr_repo.editUser(usr_id, usr);
			}

		}
	}
	
	public void computeFine() {
		scanRequest(req_repo.getRequestsList());
	}
	
	public void scheduleScan(){
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		//Call function on each request from the request table.
		Runnable scanner = () -> computeFine();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, Constants.SCAN_INITIAL_DELAY, Constants.SCAN_PERIOD, Constants.SCAN_TIMEUNIT);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, Constants.SCAN_DURATION, Constants.SCAN_DURATIONUNIT);
	}
	
}


class UnusableFineCalculation extends FineCalculation{	
	private static UnusableFineCalculation fineobj = null;

	private UnusableFineCalculation(){
		this.eq_repo = EquipmentRepository.getInstance();
		this.req_repo = RequestRepository.getInstance();
		this.usr_repo = UserRepository.getInstance();		
	}
	
	public static UnusableFineCalculation getInstance() {
		if(fineobj == null) {
			fineobj = new UnusableFineCalculation();
		}
		return fineobj;
	}
	
	public void calc(Request req){
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
			String usr_id = usr_repo.getUserId(req.getUserSurrId());
			User usr = usr_repo.getUserById(usr_id);
			
			usr.addFine(Constants.FINE_DEFECTIVE);
			usr_repo.editUser(usr_id, usr);
			SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_DAMAGE_SUBJECT, Constants.MAIL_DAMAGE_BODY + Integer.toString(usr.getFine()) + ".");
		
		}
	}
	
	public void computeFine(String id){
		scanRequest(req_repo.getRequestsListForEquipment(id));
	}
}
	
	
	
	
