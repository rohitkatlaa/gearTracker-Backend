package com.geartracker.geartracker_backend;

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
import javax.ws.rs.core.MediaType;

@Path("users")
public class UserResource {
	
	UserRepository repo = new UserRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return repo.getUsersList();
	}
	
	@GET
	@Path("/{id}")
	public User getUser(@PathParam("id") String id) { 
		return repo.getUserById(id);
	}
	
	@POST
	public User createUser(User e) {
		repo.createUser(e);
		return e;
	}
	
	// @PUT
	// @Path("/{id}")
	// public User editUser(@PathParam("id") String id, User e) {
	// 	return repo.editUser(id, e);
	// }
	
	// @DELETE
	// @Path("/{id}")
	// public User deleteUser(@PathParam("id") String id) { 
	// 	System.out.println(id);
	// 	return null;
	// }
}
