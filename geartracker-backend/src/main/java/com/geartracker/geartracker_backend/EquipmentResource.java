package com.geartracker.geartracker_backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("equipments")
public class EquipmentResource {
	
	EquipmentRepository repo = new EquipmentRepository();
	UserRepository user_repo = new UserRepository();
	RequestRepository request_repo = new RequestRepository();
	
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
			if(r.getStatus()==Constants.REQUEST_STATUS_APPROVED) {
				Equipment e = repo.getEquipmentById(repo.getEquipmentId(r.getEquipmentId()));
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
	public String bookEquipment(@PathParam("id") String id, String user_id) { 
		Equipment e = repo.getEquipmentById(id);
		if(e.getStatus()==Constants.EQUIPMENT_STATUS_AVAILABLE) {
			int e_id = repo.getSurrogateId(id);
			int u_id = user_repo.getSurrogateId(user_id);
			if(e_id == Constants.ERROR_STATUS || u_id == Constants.ERROR_STATUS) {
				return Constants.FAILURE_STATUS;
			}
			Request r = new Request(e_id, u_id, Constants.REQUEST_STATUS_OPEN, LocalDate.now(), (LocalDate)null);
			e.setStatus(Constants.EQUIPMENT_STATUS_REQUESTED);
			repo.editEquipment(id, e);
			request_repo.createRequest(r);
		}
		return Constants.SUCCESS_STATUS;
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
	public Equipment deleteEquipment(@PathParam("id") String id) { 
		System.out.println(id);
		return null;
	}
}
