package com.geartracker.geartracker_backend;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.sendemail.SendMail;

public class LateFineCalculation extends FineCalculation {
	/*
		Class that implements the fine calculation for the equipment that has not been returned withing the duration.
	*/
	private static LateFineCalculation fineobj = null;
	private boolean running = false;
	
	private LateFineCalculation(){
		running = true;
	}
	
	public static LateFineCalculation getInstance() {
		if(fineobj == null) {
			fineobj = new LateFineCalculation();
		}
		return fineobj;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public void setRunning(boolean newState) {
		running = newState;
	}

	public void calc(Request req){
		//Retrieving equipment from database.
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
			//Retrieving user from database.
			String usr_id = usr_repo.getUserId(req.getUserSurrId());
			User usr = usr_repo.getUserById(usr_id);
			
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
		System.out.println("Starting Cron Job");
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		// Call function on each request from the request table.
		Runnable scanner = () -> computeFine();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, Constants.SCAN_INITIAL_DELAY, Constants.SCAN_PERIOD, Constants.SCAN_TIMEUNIT);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, Constants.SCAN_DURATION, Constants.SCAN_DURATIONUNIT);
	}
	
}
