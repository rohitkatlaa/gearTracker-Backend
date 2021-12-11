package com.geartracker.geartracker_backend;

public class LoginData {
	/*
		Class that is used to represent the data required for login.
	*/
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
