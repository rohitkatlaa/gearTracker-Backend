package com.geartracker.geartracker_backend;
import java.util.List;
import com.sendemail.SendMail;

//ScanRequest takes Compute object in to function and then does it. Schedule scan takes no argument. If constructor for each compute could be different it would help. 

public class UnusableCompute implements Compute{
	private Request req;
	private String equip_id;
	
	public Request getRequest() {
		return req;
	}
	
	public void setRequest(Request req) {
		this.req = req;
	}
	
	public String getEquipmentId() {
		return equip_id;
	}
	
	public void setEquipmentId(String new_equip_id) {
		equip_id = new_equip_id;
	}
	
	public List<Request> getRequestList(){
		return req_repo.getRequestsListForEquipment(equip_id);
	}
	
	public void calc() {
		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
			String usr_id = usr_repo.getUserId(req.getUserSurrId());
			User usr = usr_repo.getUserById(usr_id);
			
			usr.addFine(Constants.FINE_DEFECTIVE);
			usr_repo.editUser(usr_id, usr);
			SendMail.sendmail(usr.getEmail(), Constants.MAIL_USERNAME, Constants.MAIL_PASSWORD, Constants.MAIL_DAMAGE_SUBJECT, Constants.MAIL_DAMAGE_BODY + Integer.toString(usr.getFine()) + ".");
		
		}
	}
	
	public static void main(String[] args) {
		UnusableCompute unus = new UnusableCompute();
		unus.setRequest(req_repo.getRequestById(2));
		unus.setEquipmentId("CBA1");
		unus.calc();
	}
}