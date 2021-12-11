package com.geartracker.geartracker_backend;

import java.util.List;

public abstract class FineCalculation{
	/*
		Base class for Fine Calculation.
	*/
	protected EquipmentRepository eq_repo = EquipmentRepository.getInstance();
	protected RequestRepository req_repo = RequestRepository.getInstance();
	protected UserRepository usr_repo = UserRepository.getInstance();
	
	public void scanRequest(List<Request> reqs){
		for(Request req: reqs) {
			calc(req);
		}
	}
	
	public abstract void calc(Request req);
	public static void main(String[] args){
		LateFineCalculation.getInstance().scheduleScan();
	}
}
