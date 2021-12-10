package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

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

@Path("users")
public class UserResource {
	
	UserRepository repo = new UserRepository();
	Gson gson = new Gson();
	
	@Context
	private HttpHeaders httpHeaders;
	
	private void authenticate(ArrayList<String> roles) {
		String token = httpHeaders.getHeaderString("auth-token");
		LoginData ld = loginResource.getLoginCred(token);
		if(ld != null) {
			User u = repo.login(ld.getId(), ld.getPassword());
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
	public List<User> getUsers() {
		authenticate(Constants.SUPER_USER_ROLES);
		return repo.getUsersList();
	}
	
	@GET
	@Path("/{id}")
	public User getUser(@PathParam("id") String id) {
		authenticate(Constants.ALL_ROLES);
		return repo.getUserById(id);
	}
	
	@POST
	public User createUser(String json) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		User e = gson.fromJson(json, User.class);
		User u = repo.createUser(e);
		return u;
	}
	
	@PUT
	@Path("/{id}")
	public User editUser(@PathParam("id") String id, String json) {
		authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
		User e = gson.fromJson(json, User.class);
		return repo.editUser(id, e);
	}
	
	@PUT
	@Path("/delete/{id}")
	public String deleteUser(@PathParam("id") String id) {
	 	authenticate(new ArrayList<String>(Arrays.asList(Constants.ADMIN_ROLE)));
	 	
	 	RequestRepository req_repo = new RequestRepository();
	 	List<Request> req_list = req_repo.getRequestsListForStudent(id);
	 	
	 	for(Request req:req_list) {
	 		if(req.getStatus().equalsIgnoreCase(Constants.REQUEST_STATUS_APPROVED)) {
	 			return Constants.USER_ACTIVE_STATUS;
	 		}
	 	}
	 	return repo.deleteUser(id);
	}
	
}
