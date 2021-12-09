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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

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
	
	EquipmentRepository repo = new EquipmentRepository();
	UserRepository user_repo = new UserRepository();
	RequestRepository request_repo = new RequestRepository();
	Gson gson = new Gson(); 
	
	@Context
	private HttpHeaders httpHeaders;
	
	private void authenticate(ArrayList<String> roles) {
		String token = httpHeaders.getHeaderString("auth-token");
		LoginData ld = loginResource.getLoginCred(token);
		if(ld != null) {
			User u = user_repo.login(ld.getId(), ld.getPassword());
			if(u != null) {
				for(String role: u.getRoles()) {
					if(roles.contains(role)) {
						return;
					}
				}
			}
		}
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipments() {
		authenticate(Constants.ALL_ROLES);
		return repo.getEquipmentsList();
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipmentsForStudent(@PathParam("id") String id) {
		authenticate(Constants.ALL_ROLES);
		try {
			List<Request> requests = request_repo.getRequestsListForStudent(id);
			List<Equipment> equipments = new ArrayList<>();
			for(Request r: requests) {
				if(r.getStatus().equals(Constants.REQUEST_STATUS_APPROVED)) {
					Equipment e = repo.getEquipmentById(repo.getEquipmentId(r.getEquipmentSurrId()));
					equipments.add(e);
				}
			}
			return equipments;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	
	@GET
	@Path("/available")
	public List<Equipment> getAvailableEquipment() {
		authenticate(Constants.ALL_ROLES);
		try {
			return repo.getAvailableEquipment();
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/{id}")
	public Equipment getEquipment(@PathParam("id") String id) {
		authenticate(Constants.ALL_ROLES);
		try {
			return repo.getEquipmentById(id);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@POST
	@Path("/book/{id}")
	public String bookEquipment(@PathParam("id") String id, String body) {
		authenticate(Constants.ALL_ROLES);
		try {
			UserId user_id = gson.fromJson(body, UserId.class);
			Equipment e = repo.getEquipmentById(id);
			if(e.getStatus().equals(Constants.EQUIPMENT_STATUS_AVAILABLE)) {
				int e_id = repo.getSurrogateId(id);
				int u_id = user_repo.getSurrogateId(user_id.getUser_id());
				if(e_id == Constants.ERROR_STATUS || u_id == Constants.ERROR_STATUS) {
					return Constants.FAILURE_STATUS;
				}
				Request r = new Request(id, e_id, user_id.getUser_id(), u_id, Constants.REQUEST_STATUS_OPEN, (LocalDate)null, (LocalDate)null);
				e.setStatus(Constants.EQUIPMENT_STATUS_REQUESTED);
				repo.editEquipment(id, e);
				request_repo.createRequest(r);
				return Constants.SUCCESS_STATUS;
			}
			return Constants.FAILURE_STATUS;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@POST
	public Equipment createEquipment(Equipment equipment) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		try {
			repo.createEquipment(equipment);
			return equipment;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@PUT
	@Path("/{id}")
	public Equipment editEquipment(@PathParam("id") String id, Equipment equipment) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		try {
			return repo.editEquipment(id, equipment);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@DELETE
	@Path("/{id}")
	public String deleteEquipment(@PathParam("id") String id) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		try {
			repo.editEquipmentStatus(id, Constants.EQUIPMENT_STATUS_DISCARDED);
			return Constants.SUCCESS_STATUS;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}
