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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Request> getRequests() {
		return repo.getRequestsList();
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
	
	@DELETE
	@Path("/{id}")
	public Request deleteRequest(@PathParam("id") int id) { 
		System.out.println(id);
		return null;
	}
}
