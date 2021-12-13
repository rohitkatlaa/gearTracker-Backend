package com.geartracker.geartracker_backend;

import java.time.LocalDate;
import java.util.ArrayList;
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
	/*
		Class that provides the APIs for Requests.
	*/	
	RequestRepository request_repo = RequestRepository.getInstance();
	EquipmentRepository equipment_repo = EquipmentRepository.getInstance();
	UserRepository user_repo = UserRepository.getInstance();
	Gson gson = new Gson();
	
	@Context
	private HttpHeaders httpHeaders;
	
	private void authenticate(ArrayList<String> roles) {
		/*
			Funtion to authenticate based on roles.
		*/
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
		/*
			API: GET - /webapi/requests
			API to fetch the list of requests.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			return request_repo.getRequestsList();
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}	
	}
	
	@GET
	@Path("/cron")
	@Produces(MediaType.APPLICATION_JSON)
	public String startCronJob() {
		/*
			API: GET - /webapi/requests/cron
			API to start the cron job of calculating the fine on the list of requests.
		*/
		try {
			if(!LateFineCalculation.getInstance().getRunning()) {
				LateFineCalculation.getInstance().setRunning(true);
				LateFineCalculation.getInstance().scheduleScan();
			}
			return Constants.SUCCESS_STATUS;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}	
	}
	
	@GET
	@Path("/student/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Request> getRequestsForStudent(@PathParam("id") String id) {
		/*
			API: GET - /webapi/requests/student/id
			API to fetch the list of requests of a student.
		*/
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
		/*
			API: GET - /webapi/requests/{id}
			API to fetch the request from the id.
		*/
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
		/*
			API: POST - /webapi/requests
			API to create the requests.
		*/
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
		/*
			API: PUT - /webapi/requests
			API to edit the requests.
		*/
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
		/*
			API: PUT - /webapi/requests/approve/{id}
			API to change the status of the request to approve.
		*/
		authenticate(Constants.SUPER_USER_ROLES);
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
		/*
			API: PUT - /webapi/requests/approve/{id}
			API to change the status of the request to close and set the status for the equipment.
		*/
		authenticate(Constants.SUPER_USER_ROLES);
		try {
			Status s = gson.fromJson(body, Status.class);
			Request r = request_repo.getRequestById(id);
			String e_id = equipment_repo.getEquipmentId(r.getEquipmentSurrId());
			Equipment e = equipment_repo.getEquipmentById(e_id);
			if(e.getStatus().equals(Constants.EQUIPMENT_STATUS_ISSUED)) {
				 if(s.getStatus().equals(Constants.EQUIPMENT_STATUS_LOST) || s.getStatus().equalsIgnoreCase(Constants.EQUIPMENT_STATUS_BROKEN)) {
					 UnusableCompute compObj = new UnusableCompute();
					 compObj.setEquipmentId(e_id);
					 compObj.setRequest(r);

					 FineCalculation.getInstance().setComputeObj(compObj);
					 FineCalculation.getInstance().scanRequest();
				 }
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