package com.geartracker.geartracker_backend;

import com.sendemail.SendMail;

public class UnusableFineCalculation extends FineCalculation{	
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
