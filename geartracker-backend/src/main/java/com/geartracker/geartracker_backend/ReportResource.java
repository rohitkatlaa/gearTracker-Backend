package com.geartracker.geartracker_backend;

import java.util.List;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("report")
public class ReportResource {
	
	RequestRepository request_repo = new RequestRepository();
	EquipmentRepository equipment_repo = new EquipmentRepository();
	
	@GET
	@Path("/equipment/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Integer> getEquipmentStatusReport(@PathParam("type") String status) {
		//Report for discarded/lost/broken
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
//		System.out.println("I was here * 2");
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
		HashMap<String, Integer> map = getRequestCount();
		return map.values().stream().reduce(0,Integer::sum);
	}

}
