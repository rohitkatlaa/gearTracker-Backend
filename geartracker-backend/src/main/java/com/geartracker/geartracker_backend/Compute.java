package com.geartracker.geartracker_backend;
import java.util.List;

public interface Compute{
	public final EquipmentRepository eq_repo = EquipmentRepository.getInstance();
	public final RequestRepository req_repo = RequestRepository.getInstance();
	public final UserRepository usr_repo = UserRepository.getInstance();
	
	public void calc();
	public abstract Request getRequest();
	public abstract void setRequest(Request new_req);
	public List<Request> getRequestList();
}