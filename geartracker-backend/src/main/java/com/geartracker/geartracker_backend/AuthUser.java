package com.geartracker.geartracker_backend;

public class AuthUser extends User {
	/*
		User class that is used to send the auth token while logging in.
	*/

	private String authToken;

	public AuthUser(User u, String authToken) {
		this.id = u.getId();
		this.name = u.getName();
		this.password = u.password;
		this.email = u.getEmail();
		this.roles = u.getRoles();
		this.student = u.getStudent();
		this.fine = u.getFine();
		this.sportsStatus = u.getSportsStatus();
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
