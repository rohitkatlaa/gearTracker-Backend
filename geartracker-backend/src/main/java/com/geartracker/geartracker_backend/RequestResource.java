package com.geartracker.geartracker_backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

class Status {
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

@Path("requests")
public class RequestResource {
	
	RequestRepository request_repo = RequestRepository.getInstance();
	EquipmentRepository equipment_repo = EquipmentRepository.getInstance();
	UserRepository user_repo = UserRepository.getInstance();
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
	public List<Request> getRequests() {
		authenticate(Constants.SUPER_USER_ROLES);
		try {
			return request_repo.getRequestsList();
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}	
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Request> getRequestsForStudent(@PathParam("id") String id) {
		authenticate(Constants.ALL_ROLES);
		try {
			return request_repo.getRequestsListForStudent(id);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/{id}")
	public Request getRequestById(@PathParam("id") int id) {
		authenticate(Constants.ALL_ROLES);
		try {
			return request_repo.getRequestById(id);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@POST
	public Request createRequest(Request r) {
		authenticate(Constants.ALL_ROLES);
		try {
			request_repo.createRequest(r);
			return r;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@PUT
	@Path("/{id}")
	public Request editRequest(@PathParam("id") int id, Request r) {
		authenticate(Constants.ALL_ROLES);
		try {
			return request_repo.editRequest(id, r);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@PUT
	@Path("/approve/{id}")
	public String approveRequest(@PathParam("id") int id) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		try {
			Request r = request_repo.getRequestById(id);
			String e_id = equipment_repo.getEquipmentId(r.getEquipmentSurrId());
			Equipment e = equipment_repo.getEquipmentById(e_id);
			if(e.getStatus().equals(Constants.EQUIPMENT_STATUS_REQUESTED)) {
				equipment_repo.editEquipmentStatus(e_id, Constants.EQUIPMENT_STATUS_ISSUED);
				request_repo.setRequestIssueDate(id, LocalDate.now());
				request_repo.editRequestStatus(id, Constants.REQUEST_STATUS_APPROVED);
				return Constants.SUCCESS_STATUS;
			}
			return Constants.FAILURE_STATUS;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@PUT
	@Path("/close/{id}")
	public String closeRequest(@PathParam("id") int id, String body) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		try {
			Status s = gson.fromJson(body, Status.class);
			Request r = request_repo.getRequestById(id);
			String e_id = equipment_repo.getEquipmentId(r.getEquipmentSurrId());
			Equipment e = equipment_repo.getEquipmentById(e_id);
			if(e.getStatus().equals(Constants.EQUIPMENT_STATUS_ISSUED)) {
				equipment_repo.editEquipmentStatus(e_id, s.getStatus());
				request_repo.editRequestStatus(id, Constants.REQUEST_STATUS_CLOSED);
				request_repo.setRequestReturnDate(id, LocalDate.now());
				return Constants.SUCCESS_STATUS;
			}
			return Constants.FAILURE_STATUS;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}