package com.geartracker.geartracker_backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

class UserId {
	private String user_id;
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}

@Path("equipments")
public class EquipmentResource {
	
	public static HashMap<String, Integer> stats_discarded = new HashMap<>();
	EquipmentRepository repo = new EquipmentRepository();
	UserRepository user_repo = new UserRepository();
	RequestRepository request_repo = new RequestRepository();
	Gson gson = new Gson(); 
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipments() {
		return repo.getEquipmentsList();
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipmentsForStudent(@PathParam("id") String id) {
		List<Request> requests = request_repo.getRequestsListForStudent(id);
		List<Equipment> equipments = new ArrayList<>();
		for(Request r: requests) {
			if(r.getStatus().equals(Constants.REQUEST_STATUS_APPROVED)) {
				Equipment e = repo.getEquipmentById(repo.getEquipmentId(r.getEquipmentSurrId()));
				equipments.add(e);
			}
		}
		return equipments;
	}
	
	
	@GET
	@Path("/available")
	public List<Equipment> getAvailableEquipment() { 
		return repo.getAvailableEquipment();
	}
	
	@GET
	@Path("/{id}")
	public Equipment getEquipment(@PathParam("id") String id) { 
		return repo.getEquipmentById(id);
	}
	
	@POST
	@Path("/book/{id}")
	public String bookEquipment(@PathParam("id") String id, String body) {
		UserId user_id = gson.fromJson(body, UserId.class);
		Equipment e = repo.getEquipmentById(id);
		if(e.getStatus().equals(Constants.EQUIPMENT_STATUS_AVAILABLE)) {
			int e_id = repo.getSurrogateId(id);
			int u_id = user_repo.getSurrogateId(user_id.getUser_id());
			if(e_id == Constants.ERROR_STATUS || u_id == Constants.ERROR_STATUS) {
				return Constants.FAILURE_STATUS;
			}
			Request r = new Request(id, e_id, user_id.getUser_id(), u_id, Constants.REQUEST_STATUS_OPEN, LocalDate.now(), (LocalDate)null);
			e.setStatus(Constants.EQUIPMENT_STATUS_REQUESTED);
			repo.editEquipment(id, e);
			request_repo.createRequest(r);
			return Constants.SUCCESS_STATUS;
		}
		return Constants.FAILURE_STATUS;
	}
	
	@POST
	public Equipment createEquipment(Equipment e) {
		repo.createEquipment(e);
		return e;
	}
	
	@PUT
	@Path("/{id}")
	public Equipment editEquipment(@PathParam("id") String id, Equipment e) {
		return repo.editEquipment(id, e);
	}
	
	@DELETE
	@Path("/{id}")
	public String deleteEquipment(@PathParam("id") String id) { 
		Equipment e = repo.getEquipmentById(id);
		String key = e.getName();
		if (stats_discarded.containsKey(key))
		{
			stats_discarded.put(key, stats_discarded.get(key)+1);
		}
		else
		{
			stats_discarded.put(key,1);
		}
		repo.editEquipmentStatus(id, Constants.EQUIPMENT_STATUS_DISCARDED);
		return Constants.SUCCESS_STATUS;
	}
}
