package com.geartracker.geartracker_backend;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("equipment")
public class EquipmentResource {
	
	EquipmentRepository repo = new EquipmentRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipments() {
		
		
		return repo.getEquipments();
	}
}
