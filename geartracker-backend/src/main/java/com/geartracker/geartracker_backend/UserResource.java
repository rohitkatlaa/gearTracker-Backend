package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

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
	Gson gson = new Gson(); 
	
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
	public User createUser(String json) {
		User e = gson.fromJson(json, User.class);
		User u = repo.createUser(e);
		return u;
	}
	
	@PUT
	@Path("/{id}")
	public User editUser(@PathParam("id") String id, String json) {
		User e = gson.fromJson(json, User.class);
		// return e;
		return repo.editUser(id, e);
	}
	
}
