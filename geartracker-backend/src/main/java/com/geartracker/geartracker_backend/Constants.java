package com.geartracker.geartracker_backend;

public class Constants {
	static final String SQL_URL =  "jdbc:mysql://localhost:3306/geartracker_db?verifyServerCertificate=false&useSSL=true";
	static final String SQL_USERNAME = "rohit";
	static final String SQL_PASSWORD = "Rohit@123";
	
	static final String EQUIPMENT_STATUS_AVAILABLE = "available";
	static final String EQUIPMENT_STATUS_REQUESTED = "requested";
	static final String EQUIPMENT_STATUS_LOST = "lost";
	static final String EQUIPMENT_STATUS_BROKEN = "broken";
	static final String EQUIPMENT_STATUS_ISSUED = "issued";
	static final String EQUIPMENT_STATUS_DISCARDED = "discarded";
	
	static final String REQUEST_STATUS_OPEN = "open";
	static final String REQUEST_STATUS_APPROVED = "approved";
	static final String REQUEST_STATUS_CLOSED = "closed";
	
	
	static final String SUCCESS_STATUS = "success";
	static final String FAILURE_STATUS = "fail";
	
	static final int ERROR_STATUS = -1;
}
