package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

class LoginData {
	private String id;
	private String password;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}


@Path("login")
public class loginResource {

	UserRepository repo = new UserRepository();
	Gson gson = new Gson(); 
	
	@POST
	public User login(String jsonString) {
		LoginData l_data = gson.fromJson(jsonString, LoginData.class);
		return repo.login(l_data.getId(), l_data.getPassword());
	}

}
