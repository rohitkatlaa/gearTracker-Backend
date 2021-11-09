package com.geartracker.geartracker_backend;
import java.time.LocalDate;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.DAYS;

public class Request {
	private int requestId;
	private int equipmentId;
	private int userId;
	private String status;
	private LocalDate issueDate;
	private LocalDate returnDate;

	// Getter and setter functions. Can be modified or dropped if unnecessary
	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public LocalDate getIssueDate(){
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate){
		this.issueDate = issueDate;
	}

	public LocalDate getReturnDate(){
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate){
		this.returnDate = returnDate;
	}

	public long dateDiff(){
		/*if(status.equalsIgnoreCase("Issued")){
			return DAYS.between(issueDate, LocalDate.now());
		}
		else{
		
			return DAYS.between(issueDate, returnDate);
		}*/
		return DAYS.between(issueDate, returnDate);
	}

	//Didn't add email class yet and will add after APIs decided and we implement for others.
//	public static void main(String[] args){
//		Request req = new Request();
//		req.setIssueDate(LocalDate.now());
//		req.setStatus
//		req.setReturnDate(req.getIssueDate().plusDays(10));
//		System.out.println(req.dateDiff());
//		//User user1 = new User("x1","Hemanth", "password", "email@gmail.com");
//		//System.out.println(user1.getName() + " " + user1.getId() + " " + user1.getPassword() + " " + user1.getEmail());
//	}
}
