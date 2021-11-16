package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("requests")
public class RequestResource {
	
	RequestRepository repo = new RequestRepository();
	EquipmentRepository equipment_repo = new EquipmentRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Request> getRequests() {
		return repo.getRequestsList();
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Request> getRequestsForStudent(@PathParam("id") String id) {
		return repo.getRequestsListForStudent(id);
	}
	
	@GET
	@Path("/approve/{id}")
	public String approveRequest(@PathParam("id") int id) {
		Request r = repo.getRequestById(id);
		String e_id = equipment_repo.getEquipmentId(r.getEquipmentId());
		Equipment e = equipment_repo.getEquipmentById(e_id);
		if(e.getStatus()==Constants.EQUIPMENT_STATUS_AVAILABLE) {
			return repo.editRequestStatus(id, Constants.REQUEST_STATUS_APPROVED);
		}
		return Constants.FAILURE_STATUS;
	}
	
	@GET
	@Path("/{id}")
	public Request getRequestById(@PathParam("id") int id) { 
		return repo.getRequestById(id);
	}
	
	@POST
	public Request createRequest(Request r) {
		repo.createRequest(r);
		return r;
	}
	
	@PUT
	@Path("/{id}")
	public Request editRequest(@PathParam("id") int id, Request r) {
		return repo.editRequest(id, r);
	}
	
	@PUT
	@Path("/close/{id}")
	public String closeRequest(@PathParam("id") int id, String status) {
		Request r = repo.getRequestById(id);
		String e_id = equipment_repo.getEquipmentId(r.getEquipmentId());
		Equipment e = equipment_repo.getEquipmentById(e_id);
		if(e.getStatus()==Constants.EQUIPMENT_STATUS_ISSUED) {
			equipment_repo.editEquipmentStatus(e_id, status);
			return repo.editRequestStatus(id, Constants.REQUEST_STATUS_CLOSED);
		}
		return Constants.FAILURE_STATUS;
	}
	
	@DELETE
	@Path("/{id}")
	public Request deleteRequest(@PathParam("id") int id) { 
		System.out.println(id);
		return null;
	}
}
