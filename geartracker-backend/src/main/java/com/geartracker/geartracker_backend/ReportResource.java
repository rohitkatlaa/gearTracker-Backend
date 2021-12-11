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
	@Path("/equipment/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getEquipmentStatusReport(@PathParam("type") String status) {
		//Report for discarded/lost/broken
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
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getRequestCount() {
		//Report for number of closed requests per equipment category(name)
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
	@Path("/requests/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getRequestStatusReport(@PathParam("type") String status) {
		//Report for open/issued/...
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
	@Path("/equipment/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getEquipmentStatusReportWithinDate(@PathParam("type") String status, DatePair dPair) {
		//Report for discarded/lost/broken
		authenticate(Constants.HIGHER_USER_ROLES);
		if(!allowed_equipment_status.contains(status)) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		try {
			LocalDate startDate = LocalDate.parse(dPair.getStartDate());
			LocalDate endDate = LocalDate.parse(dPair.getEndDate());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Equipment> equipments = equipment_repo.getEquipmentsList();
			for(Equipment e: equipments) {
				if(e.getStatus().equals(status)) {
					LocalDate mDate = equipment_repo.getModifiedDate(e.getId());
					if(startDate.isBefore(mDate) && endDate.isAfter(mDate)) {
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
		//Report for number of closed requests per equipment category(name)
		authenticate(Constants.HIGHER_USER_ROLES);
		try {
			LocalDate startDate = LocalDate.parse(dPair.getStartDate());
			LocalDate endDate = LocalDate.parse(dPair.getEndDate());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			List<Request> requests = request_repo.getRequestsList();
			for(Request r: requests) {
				if(r.getStatus().equals(Constants.REQUEST_STATUS_CLOSED)) {
					LocalDate mDate = request_repo.getModifiedDate(r.getRequestId());
					if(startDate.isBefore(mDate) && endDate.isAfter(mDate)) {
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
