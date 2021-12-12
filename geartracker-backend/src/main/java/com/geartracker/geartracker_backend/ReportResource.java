package com.geartracker.geartracker_backend;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("report")
public class ReportResource {
	/*
		Class that provides the APIs for Reports.
	*/	
	RequestRepository request_repo = RequestRepository.getInstance();
	EquipmentRepository equipment_repo = EquipmentRepository.getInstance();
	UserRepository user_repo = UserRepository.getInstance();
	ArrayList<String> allowed_equipment_status = new ArrayList<String>(Arrays.asList(
		Constants.EQUIPMENT_STATUS_BROKEN,
		Constants.EQUIPMENT_STATUS_DISCARDED,
		Constants.EQUIPMENT_STATUS_LOST));
	
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
	@Path("/equipment/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getEquipmentStatusReport(@PathParam("status") String status) {
		/*
			API: GET - /webapi/report/equipment/{status}
			API to fetch the count of each equipments with the given status.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		if(!allowed_equipment_status.contains(status)) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		try {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Equipment> equipments = equipment_repo.getEquipmentsList();
			for(Equipment e: equipments) {
				if(e.getStatus().equals(status)) {
					String key = e.getName();
					if (map.containsKey(key))
						{
							map.put(key, map.get(key)+1);
						}
						else
						{
							map.put(key,1);
						}
				}
			}
			return map;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/fine")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getTotalFine() {
		/*
		API: GET - /webapi/report/fine
		API to fetch the total fine collected
		*/
		authenticate(Constants.SUPER_USER_ROLES);
		int sum=0;
		List<User> lst = user_repo.getUsersList();
		for(User u: lst) {
			if(u.getRoles().contains(Constants.STUDENT_ROLE))
				sum += u.getFine();
		}
		return sum;
	}
	
	@GET
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getRequestCount() {
		/*
			API: GET - /webapi/report/requests
			API to fetch the count of closed requests per equipment category(name)
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Request> requests = request_repo.getRequestsList();
			for(Request r: requests) {
				if(r.getStatus().equals(Constants.REQUEST_STATUS_CLOSED)) {
					String e_id = equipment_repo.getEquipmentId(r.getEquipmentSurrId());
					Equipment e = equipment_repo.getEquipmentById(e_id);
					String key = e.getName();
					if (map.containsKey(key))
					{
						map.put(key, map.get(key)+1);
					}
					else
					{
						map.put(key,1);
					}
				}
			}
			return map;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/requests/aggregate")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getTotalCount() {
		/*
			API: GET - /webapi/report/requests/aggregate
			API to fetch the total count of closed requests.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			HashMap<String, Integer> map = getRequestCount();
			return map.values().stream().reduce(0,Integer::sum);
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/requests/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getRequestStatusReport(@PathParam("status") String status) {
		/*
			API: GET - /webapi/report/requests/{status}
			API to fetch the count of requests for the given status.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			List<Request> requests = request_repo.getRequestsList();
			int count = 0;
			for(Request r: requests) {
				if(r.getStatus().equals(status))
				{
					count ++;
				}
			}
			return count;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@POST
	@Path("/equipment/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getEquipmentStatusReportWithinDate(@PathParam("status") String status, DatePair dPair) {
		/*
			API: POST - /webapi/report/equipment/{status}
			API to fetch the count of each equipment with the given status(lost/broken/discarded) within the given dates.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		if(!allowed_equipment_status.contains(status)) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		try {
			LocalDate startDate = Utils.string_to_date(dPair.getStartDate());
			LocalDate endDate = Utils.string_to_date(dPair.getEndDate());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Equipment> equipments = equipment_repo.getEquipmentsList();
			for(Equipment e: equipments) {
				if(e.getStatus().equals(status)) {
					LocalDate mDate = equipment_repo.getModifiedDate(e.getId());
					if((startDate.isBefore(mDate) && endDate.isAfter(mDate)) || startDate.equals(mDate) || endDate.equals(mDate)) {
						String key = e.getName();
						if (map.containsKey(key)) {
							map.put(key, map.get(key)+1);
						} else {
							map.put(key,1);
						}
					}
				}
			}
			return map;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@POST
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getRequestCountWithinData(DatePair dPair) {
		/*
			API: POST - /webapi/report/requests
			API to fetch the count of requests for each equipment with the given date.
		*/
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			LocalDate startDate = Utils.string_to_date(dPair.getStartDate());
			LocalDate endDate = Utils.string_to_date(dPair.getEndDate());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Request> requests = request_repo.getRequestsList();
			for(Request r: requests) {
				if(r.getStatus().equals(Constants.REQUEST_STATUS_CLOSED)) {
					LocalDate mDate = request_repo.getModifiedDate(r.getRequestId());
					if((startDate.isBefore(mDate) && endDate.isAfter(mDate)) || startDate.equals(mDate) || endDate.equals(mDate)) {
						String e_id = equipment_repo.getEquipmentId(r.getEquipmentSurrId());
						Equipment e = equipment_repo.getEquipmentById(e_id);
						String key = e.getName();
						if (map.containsKey(key)) {
							map.put(key, map.get(key)+1);
						}
						else {
							map.put(key,1);
						}
					}
				}
			}
			return map;
		} catch(Exception e) {
			System.out.println(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

}
