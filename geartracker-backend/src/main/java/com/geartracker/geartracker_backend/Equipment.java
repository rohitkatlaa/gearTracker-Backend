package com.geartracker.geartracker_backend;

//import javax.xml.bind.annotation.XmlRootElement;


public class Equipment {	
	private String id;
	private String name;
	private String status;
	private boolean reserved;
	private String description;
	

//	Default constructor is needed for jersey POST request.
	public Equipment() {
		
	}
	
	public Equipment(String id, String name, String status, boolean reserved, String description) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.reserved = reserved;
		this.description = description;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isReserved() {
		return reserved;
	}
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
