package com.geartracker.geartracker_backend;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class FineCalculation{
	/*
		Base class for Fine Calculation.
	*/
	protected EquipmentRepository eq_repo = EquipmentRepository.getInstance();
	protected RequestRepository req_repo = RequestRepository.getInstance();
	protected UserRepository usr_repo = UserRepository.getInstance();
	
	private static FineCalculation fineobj = null;
	private Compute computeObj;
	
	private FineCalculation() {
		
	}
	
	public static FineCalculation getInstance() {
		if(fineobj == null) {
			synchronized(FineCalculation.class)
			{
				if(fineobj == null) {
					fineobj = new FineCalculation();
				}
			}
		}
		return fineobj;
	}
	
	public Compute getComputeObj() {
		return computeObj;
	}
	
	public void setComputeObj(Compute new_computeObj) {
		computeObj = new_computeObj;
	}
	
	
	public void scanRequest() {//List<Request> reqs){
		System.out.println("Reached here");
		List<Request> reqs = computeObj.getRequestList();
		for(Request req: reqs) {
			computeObj.setRequest(req);
			computeObj.calc();
		}
	}
	
	public void scheduleScan(){
		System.out.println("Starting Cron Job");
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		// Call function on each request from the request table.
		Runnable scanner = () -> scanRequest();
		ScheduledFuture<?> scanHandle = scheduler.scheduleAtFixedRate(scanner, Constants.SCAN_INITIAL_DELAY, Constants.SCAN_PERIOD, Constants.SCAN_TIMEUNIT);
		Runnable canceller = () -> scanHandle.cancel(false);
		scheduler.schedule(canceller, Constants.SCAN_DURATION, Constants.SCAN_DURATIONUNIT);
	}
	
	public static void main(String[] args){
		FineCalculation.getInstance().setComputeObj(new LateCompute());
		FineCalculation.getInstance().scheduleScan();
	}
}
