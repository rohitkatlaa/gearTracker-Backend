package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EquipmentRepository {
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
			System.out.println("hello");
			System.out.println(e);
		}
		
	}
	
	public static EquipmentRepository getInstance() {
		if(repo==null) {
			repo = new EquipmentRepository();
		}
		return repo;
	}
	
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

	
	public List<Equipment> getAvailableEquipment() //Return List of equipments filtered by availability
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

	public String editEquipmentStatus(String id, String status)  //Change status if open and return success else return failure
	{
		String sqlQuery = "UPDATE equipment SET equipment_status= '" + status + "' WHERE equipment_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sqlQuery);
			if(status.equalsIgnoreCase(Constants.EQUIPMENT_STATUS_LOST) || status.equalsIgnoreCase(Constants.EQUIPMENT_STATUS_BROKEN)) {
				
				RequestRepository req_repo = RequestRepository.getInstance();
				FineCalculation fineobj = FineCalculation.getInstance();
				List<Request> req_list = req_repo.getRequestsListForEquipment(id);
				
				for(Request req: req_list) {
					System.out.println("Reached edit Equip");
					fineobj.computeFine(req);
				}
			}
		}catch (Exception ex) {
			System.out.println(ex);
			return "failure";
		}
		return "success";
	}
	
	public String deleteEquipment(String id) {
		String sqlQuery_delete = "DELETE from equipment WHERE id = '" + id +"'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sqlQuery_delete);
			return Constants.SUCCESS_STATUS;
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return Constants.FAILURE_STATUS;
	}
}
