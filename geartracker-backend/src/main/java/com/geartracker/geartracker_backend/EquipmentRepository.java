package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EquipmentRepository {

	List<Equipment> equipments = new ArrayList<>();
	Connection conn = null;
	
	
	public EquipmentRepository() {
		String url = "jdbc:mysql://localhost:3306/geartracker_db?verifyServerCertificate=false&useSSL=true";
		String username = "root";
		String password = "narrativearc";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			System.out.println("hello");
			System.out.println(e);
		}
		
	}
	
	public List<Equipment> getEquipments() {
		String sqlQuery = "select * from equipment";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				Equipment e = new Equipment();
				e.setId(rs.getString("equipment_id"));
				e.setName(rs.getString("equipment_category"));
				e.setDescription(rs.getString("equipment_description"));
				e.setStatus(rs.getString("equipment_status"));
				int bool = rs.getInt("sports_team");
				if(bool==0)
					e.setReserved(false);
				else
					e.setReserved(true);
				this.equipments.add(e);
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}

		return this.equipments;
	}
	
	public Equipment getEquipment(String id) {
//		for(Equipment e: this.equipments) {
//			if(e.getId().equals(id)) {
//				return e;
//			}
//		}
//		return null;
		String sqlQuery = "select * from equipment where equipment_id = '" + id + "'";
		Equipment e = new Equipment();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				e.setId(rs.getString("equipment_id"));
				e.setName(rs.getString("equipment_category"));
				e.setDescription(rs.getString("equipment_description"));
				e.setStatus(rs.getString("equipment_status"));
				int bool = rs.getInt("sports_team");
				if(bool==0)
					e.setReserved(false);
				else
					e.setReserved(true);
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return e;
	}
	
	public void createEquipment(Equipment e) {
		//this.equipments.add(e);
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
	
	public Equipment editEquipment(String id, Equipment newE) {
		String sqlQuery = "UPDATE equipment SET equipment_id=?,equipment_category=?,sports_team=?,equipment_status=?,equipment_description=? WHERE equipment_id = '" + id + "'";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setString(1, newE.getId());
		    st.setString(2, newE.getName());
		    st.setString(4, newE.getStatus());
		    st.setString(5, newE.getDescription());
		    boolean reserved = newE.isReserved();
		    if(reserved==false)
				st.setInt(3, 0);
			else
				st.setInt(3, 1);
		    st.executeUpdate();
		                                                             
		}catch (Exception ex) {
			System.out.println(ex);
		}
		return newE;
	}
}
