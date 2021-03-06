package com.geartracker.geartracker_backend;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

public class Request {
	/*
		Class that used to represent an request.
	*/
	private int requestId;
	private String equipmentId;
	private int equipmentSurrId;
	private String userId;
	private int userSurrId;
	private String status;
	private LocalDate issueDate;
	private LocalDate returnDate = null;
	
	// Default constructor is needed for jersey POST request.
	public Request() {
		
	}

	// This constructor is used when request id is not needed(When creating a new request). 
	public Request(String equipmentId, int equipmentSurrId, String userId, int userSurrId, String status, LocalDate issueDate, LocalDate returnDate) {
		this.equipmentSurrId = equipmentSurrId;
		this.userSurrId = userSurrId;
		this.equipmentId = equipmentId;
		this.userId = userId;
		this.status = status;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}
	
	public Request(int requestId, String equipmentId, int equipmentSurrId, String userId, int userSurrId, String status, LocalDate issueDate, LocalDate returnDate) {
		this.requestId = requestId;
		this.equipmentSurrId = equipmentSurrId;
		this.userSurrId = userSurrId;
		this.equipmentId = equipmentId;
		this.userId = userId;
		this.status = status;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public int getEquipmentSurrId() {
		return equipmentSurrId;
	}

	public void setEquipmentSurrId(int equipmentSurrId) {
		this.equipmentSurrId = equipmentSurrId;
	}

	public int getUserSurrId() {
		return userSurrId;
	}

	public void setUserSurrId(int userSurrId) {
		this.userSurrId = userSurrId;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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

	public long daysOpen(){
		if(getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)){
			return DAYS.between(getIssueDate(), LocalDate.now());
		}
		else if(getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_CLOSED)){
			return DAYS.between(getIssueDate(), getReturnDate());
		}
		else{
			return -1;
		}
	}
}
