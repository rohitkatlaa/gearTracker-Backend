package com.geartracker.geartracker_backend;

import java.util.ArrayList;

public class User {
	/*
		Class that used to represent an user.
	*/
	protected String id;
	protected String name;
	protected String password;
	protected String email;
	protected ArrayList <String> roles;
	protected int student;
	protected int fine;
	protected boolean sportsStatus;

	public User(){
		student = 0;
		roles = new ArrayList<String>();
		fine = 0;
		sportsStatus = false;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword(){
		return this.password;
	}

	public void resetPassword(String newPassword){
		this.password = newPassword;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public ArrayList<String> getRoles(){
		return roles;
	}
	public void add_roles(String role){
		roles.add(role);
	}
	
    public int getFine(){
		return fine;
	}

	public void setFine(int _updatedFine){
		fine = _updatedFine;
	}

	public void addFine(int _added){
		fine += _added;
	}

	public boolean getSportsStatus(){
		return sportsStatus;
	}
	public void setSportsStatus(boolean _status){
		sportsStatus = _status;
	}
	
	public int getStudent(){
		return student;
	}
	public void setStudent(int _sid){
		student = _sid;
	}
}