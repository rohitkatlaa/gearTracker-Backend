package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Constants {
	static final String SQL_URL =  "jdbc:mysql://localhost:3306/geartracker_db?verifyServerCertificate=false&useSSL=true";
	static final String SQL_USERNAME = "hemc";
	static final String SQL_PASSWORD = "narrativearc";
	
	static final String MAIL_USERNAME = "geartrackertesting486@gmail.com";
	static final String MAIL_PASSWORD = "geartrackertesting684";
	static final String MAIL_LATE_SUBJECT = "Reminder to return equipment";
	static final String MAIL_LATE_BODY = "Please return equipment possessed by you. Your current fine is ";
	static final String MAIL_DAMAGE_SUBJECT = "Fine for damaging equipment";
	static final String MAIL_DAMAGE_BODY = "Your act of making our equipment unusable is unforgivable. Don't repeat this. Your current fine is ";
	
	static final int SCAN_INITIAL_DELAY = 1;
	static final int SCAN_PERIOD = 5; // Assuming in seconds. 
	static final TimeUnit SCAN_TIMEUNIT = TimeUnit.SECONDS;
	static final int SCAN_DURATION = 1;
	static final TimeUnit SCAN_DURATIONUNIT = TimeUnit.HOURS;
	
	static final String EQUIPMENT_STATUS_AVAILABLE = "available";
	static final String EQUIPMENT_STATUS_REQUESTED = "requested";
	static final String EQUIPMENT_STATUS_LOST = "lost";
	static final String EQUIPMENT_STATUS_BROKEN = "broken";
	static final String EQUIPMENT_STATUS_ISSUED = "issued";
	static final String EQUIPMENT_STATUS_DISCARDED = "discarded";
	
	static final String REQUEST_STATUS_OPEN = "open";
	static final String REQUEST_STATUS_APPROVED = "approved";
	static final String REQUEST_STATUS_CLOSED = "closed";
	//static final String REQUEST_STATUS_ISSUED = "issued";
//	static final LocalDate RETURN_DATE_DUMMY = LocalDate.of(2000, 1, 1);
	
	static final String USER_ACTIVE_STATUS = "User has active request, cannot be deleted yet.";
	static final String EQUIPMENT_ACTIVE_STATUS = "Equipment has been issued, cannot be deleted yet.";
	
	static final int FINE_CUTOFF_DAYS = 7;
	static final int FINE_LATE = 5;
	static final int FINE_DEFECTIVE = 100;
	
	static final String SUCCESS_STATUS = "success";
	static final String FAILURE_STATUS = "fail";
	
	static final int STUDENT_DUMMY = 0;
	
	static final int ERROR_STATUS = -1;


	static final String SECURITY_KEY = "geartracker";
	static final String ENCRYPTION_ALG = "Blowfish";
	
	static final String ADMIN_ROLE = "admin";
	static final String STUDENT_ROLE = "student";
	static final String SPORTS_COMM_ROLE = "sportscomm";
	
	static final ArrayList<String> ALL_ROLES = new ArrayList<String>(Arrays.asList(ADMIN_ROLE, STUDENT_ROLE, SPORTS_COMM_ROLE));
	static final ArrayList<String> SUPER_USER_ROLES = new ArrayList<String>(Arrays.asList(ADMIN_ROLE, SPORTS_COMM_ROLE));
}
