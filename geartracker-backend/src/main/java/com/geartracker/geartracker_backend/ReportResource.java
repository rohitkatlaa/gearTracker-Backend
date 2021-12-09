package com.geartracker.geartracker_backend;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
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
	
	RequestRepository request_repo = new RequestRepository();
	EquipmentRepository equipment_repo = new EquipmentRepository();
	UserRepository user_repo = new UserRepository();
	
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
		authenticate(Constants.SUPER_USER_ROLES);
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
	}
	
	@GET
	@Path("/requests")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getRequestCount() {
		//Report for number of requests per equipment category(name)
		authenticate(Constants.SUPER_USER_ROLES);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<Request> requests = request_repo.getRequestsList();
		for(Request r: requests) {
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
		return map;
	}
	
	@GET
	@Path("/requests/aggregate")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getTotalCount() {
		authenticate(Constants.SUPER_USER_ROLES);
		HashMap<String, Integer> map = getRequestCount();
		return map.values().stream().reduce(0,Integer::sum);
	}
	
	@GET
	@Path("/requests/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getRequestStatusReport(@PathParam("type") String status) {
		//Report for open/issued/...
		authenticate(Constants.SUPER_USER_ROLES);
		List<Request> requests = request_repo.getRequestsList();
		int count = 0;
		for(Request r: requests) {
			if(r.getStatus().equals(status))
			{
				count ++;
			}
		}
		return count;
	}

}
