package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class EquipmentRepository {

	Connection conn = null;
	
	
	public EquipmentRepository() {
		String url = "jdbc:mysql://localhost:3306/geartracker_db";
		String username = "root";
		String password = "root";
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public List<Equipment> getEquipments() {
		List<Equipment> equipments = new ArrayList<>();
		
		String sqlQuery = "select * from equipment";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				Equipment e = new Equipment();
				// Assign the values to e
				
				equipments.add(e);
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return equipments;
	}
}
