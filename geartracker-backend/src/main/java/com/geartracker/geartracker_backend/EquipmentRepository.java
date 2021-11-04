package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class EquipmentRepository {

//	Connection conn = null;
	List<Equipment> equipments = new ArrayList<>();
	
	
	public EquipmentRepository() {
//		String url = "jdbc:mysql://127.0.0.1:3306/sports_db";
//		String username = "rohit";
//		String password = "1256156@Rk";
////		String connectionString = "jdbc:mysql://localhost:3306/sports_db?user=rohit&password=1256156@Rk&useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection(url, username, password);
////			conn = DriverManager.getConnection(connectionString);
//		} catch(Exception e) {
//			System.out.println("hello");
//			System.out.println("hello");
//			System.out.println("hello");
//			System.out.println(e);
//		}
		Equipment e1 = new Equipment();
		e1.setId("e1");
		e1.setName("e1");
		e1.setReserved(false);
		e1.setStatus("vacant");
		
		
		equipments.add(e1);
		
	}
	
	public List<Equipment> getEquipments() {
		
		
		
//		String sqlQuery = "select * from equipment";
//		try {
//			Statement st = conn.createStatement();
//			ResultSet rs = st.executeQuery(sqlQuery);
//			while(rs.next()) {
//				Equipment e = new Equipment();
////				System.out.println();
//				e.setName(rs.getString(2));
//				equipments.add(e);
//			}
//			
//		} catch(Exception e) {
//			System.out.println(e);
//		}
		
		return this.equipments;
	}
	
	public Equipment getEquipment(String id) {
		for(Equipment e: this.equipments) {
			if(e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}
	
	public void createEquipment(Equipment e) {
		this.equipments.add(e);
	}
	
	public Equipment editEquipment(String id, Equipment newE) {
		// Code for editing
		return newE;
	}
}
