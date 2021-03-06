package com.geartracker.geartracker_backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EquipmentRepository {
	/* 
		Class that is used to interact with the Equipment table in the SQL database.
	*/
	private Connection conn = null;
	private static EquipmentRepository repo = null;
	
	private EquipmentRepository() {
		String url = Constants.SQL_URL;
		String username = Constants.SQL_USERNAME;
		String password = Constants.SQL_PASSWORD;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public static EquipmentRepository getInstance() {
		if(repo==null) {
			repo = new EquipmentRepository();
		}
		return repo;
	}
	
	/*
		Function to fetch the equipment id from its surrogate id.
	*/
	public String getEquipmentId(int id) {
		String sqlQuery = "select equipment_id from equipment where surrogate_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				String e_id = rs.getString("equipment_id");
				return e_id;
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return Constants.FAILURE_STATUS;
	}
	
	/*
		Function to fetch the surrogate id from its equipment id.
	*/
	public int getSurrogateId(String id) {
		String sqlQuery = "select surrogate_id from equipment where equipment_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				int surrogate_id = rs.getInt("surrogate_id");
				return surrogate_id;
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return Constants.ERROR_STATUS;
	}
	
	/*
		Function to fetch the list of equipments from the database.
	*/
	public List<Equipment> getEquipmentsList() {
		List<Equipment> equipments = new ArrayList<>();
		String sqlQuery = "select * from equipment";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				String equipment_id = rs.getString("equipment_id");
				String name = rs.getString("equipment_category");
				String description = rs.getString("equipment_description");
				String status = rs.getString("equipment_status");
				boolean reserved = (rs.getInt("sports_team")!=0);
				Equipment e = new Equipment(equipment_id, name, status, reserved, description);
				equipments.add(e);
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}

		return equipments;
	}

	/*
		Function to fetch the list of equipments filtered by availability from the database.
	*/
	public List<Equipment> getAvailableEquipment()
	{
		List<Equipment> equipments = new ArrayList<>();
		String sqlQuery = "select * from equipment where equipment_status = 'available'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				String equipment_id = rs.getString("equipment_id");
				String name = rs.getString("equipment_category");
				String description = rs.getString("equipment_description");
				String status = rs.getString("equipment_status");
				boolean reserved = (rs.getInt("sports_team")!=0);
				Equipment e = new Equipment(equipment_id, name, status, reserved, description);
				equipments.add(e);
			}
			
		} catch(Exception e) {
			System.out.println(e);

			return null;
		}

		return equipments;
	}
	
	/*
		Function to fetch the equipment from the equipment_id from the database.
	*/
	public Equipment getEquipmentById(String id) {
		String sqlQuery = "select * from equipment where equipment_id = '" + id + "'";
		Equipment e = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				String equipment_id = rs.getString("equipment_id");
				String name = rs.getString("equipment_category");
				String description = rs.getString("equipment_description");
				String status = rs.getString("equipment_status");
				boolean reserved = (rs.getInt("sports_team")!=0);
				e = new Equipment(equipment_id, name, status, reserved, description);
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return e;
	}
	
	/*
		Function to create an equipment in the database.
	*/
	public void createEquipment(Equipment e) {
		String sqlQuery = "insert into equipment (equipment_id,equipment_category,sports_team,equipment_status,equipment_description) values (?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setString(1,e.getId());
			st.setString(2,e.getName());
			boolean reserved = e.isReserved(); 
			if(reserved==false)
				st.setInt(3, 0);
			else
				st.setInt(3, 1);
			st.setString(4, e.getStatus());
			st.setString(5, e.getDescription());
			st.executeUpdate();
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	/*
		Function to edit an equipment in the database.
	*/
	public Equipment editEquipment(String id, Equipment newE) {
		String sqlQuery = "UPDATE equipment SET equipment_category=?,sports_team=?,equipment_status=?,equipment_description=? WHERE equipment_id = '" + id + "'";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
		    st.setString(1, newE.getName());
		    st.setString(3, newE.getStatus());
		    st.setString(4, newE.getDescription());
		    boolean reserved = newE.isReserved();
		    if(reserved==false)
				st.setInt(2, 0);
			else
				st.setInt(2, 1);
		    st.executeUpdate();
		                                                             
		}catch (Exception ex) {
			System.out.println(ex);
		}
		return newE;
	}

	/*
		Function to edit the status of an equipment in the database.
	*/
	public String editEquipmentStatus(String id, String status)  //Change status if open and return success else return failure
	{
		String sqlQuery = "UPDATE equipment SET equipment_status= '" + status + "' WHERE equipment_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sqlQuery);
		}catch (Exception ex) {
			System.out.println(ex);
			return "failure";
		}
		return "success";
	}
	
	/*
		Function to delete an equipment from the database.
	*/
	public String deleteEquipment(String id) {
		String sqlQuery_delete = "DELETE from equipment WHERE equipment_id = '" + id +"'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sqlQuery_delete);
			return Constants.SUCCESS_STATUS;
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return Constants.FAILURE_STATUS;
	}
	
	/*
		Function to fetch the last modified date of an equipment from the database.
	*/
	public LocalDate getModifiedDate(String id) {
		String sqlQuery = "select updated_at from equipment where equipment_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				Date date = new Date(rs.getTimestamp("updated_at").getTime());
				return date.toLocalDate();
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return null;
	}
}
