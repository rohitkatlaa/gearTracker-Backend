package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("equipments")
public class EquipmentResource {
	
	EquipmentRepository repo = new EquipmentRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Equipment> getEquipments() {
		return repo.getEquipmentsList();
	}
	
	@GET
	@Path("/{id}")
	public Equipment getEquipment(@PathParam("id") String id) { 
		return repo.getEquipmentById(id);
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
}
