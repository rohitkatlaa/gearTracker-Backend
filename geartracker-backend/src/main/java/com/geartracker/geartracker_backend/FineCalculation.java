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
