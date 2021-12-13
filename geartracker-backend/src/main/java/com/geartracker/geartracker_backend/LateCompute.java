package com.geartracker.geartracker_backend;
import java.util.List;
import com.sendemail.SendMail;

//ScanRequest takes Compute object in to function and then does it. Schedule scan takes no argument. If constructor for each compute could be different it would help. 

public class LateCompute implements Compute{
	private Request req;
	
	public Request getRequest() {
		return req;
	}
	
	public void setRequest(Request req) {
		this.req = req;
	}
	
	public List<Request> getRequestList(){
		return req_repo.getRequestsList();
	}
	
	public void calc() {
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
				SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_LATE_SUBJECT, Constants.MAIL_LATE_BODY + Integer.toString(usr.getFine()) + ".");
			}

		}
	}
}