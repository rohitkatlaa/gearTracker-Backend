package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;

public class RequestRepository {
	/* 
		Class that is used to interact with the Requests table in the SQL database.
	*/
	List<Request> requests = new ArrayList<>();
	Connection conn = null;
	private static RequestRepository repo = null;
	
	EquipmentRepository equipment_repo = EquipmentRepository.getInstance();
	UserRepository user_repo = UserRepository.getInstance();
	
	private RequestRepository() {
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
	
	public static RequestRepository getInstance() {
		if(repo==null) {
			repo = new RequestRepository();
		}
		return repo;
	}
	
	/*
		Function to fetch the list of requests from the database.
	*/
	public List<Request> getRequestsList() {
		List<Request> requests = new ArrayList<>();
		String sqlQuery = "select * from requests";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_surr_id = rs.getInt("id_user");
				int equipment_surr_id = rs.getInt("id_equipment");
				Date isd = rs.getDate("issue_date");
				LocalDate issue_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					issue_date = isd.toLocalDate();
				}
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				String user_id = user_repo.getUserId(user_surr_id);
				String equipment_id = equipment_repo.getEquipmentId(equipment_surr_id);
				Request r = new Request(request_id, equipment_id, equipment_surr_id, user_id, user_surr_id, status, issue_date, return_date);
				requests.add(r);
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return requests;
	}
	
	/*
		Function to fetch the list of requests of a student from the database.
	*/
	public List<Request> getRequestsListForStudent(String id) {
		List<Request> requests = new ArrayList<>();
		String sqlQuery = "select * from requests where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_surr_id = rs.getInt("id_user");
				int equipment_surr_id = rs.getInt("id_equipment");
				Date isd = rs.getDate("issue_date");
				LocalDate issue_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					issue_date = isd.toLocalDate();
				}
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				String user_id = user_repo.getUserId(user_surr_id);
				String equipment_id = equipment_repo.getEquipmentId(equipment_surr_id);
				Request r = new Request(request_id, equipment_id, equipment_surr_id, user_id, user_surr_id, status, issue_date, return_date);
				requests.add(r);
			}
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return requests;
	}
	
	/*
		Function to fetch the request from the id from the database.
	*/
	public Request getRequestById(int id) {
		String sqlQuery = "select * from requests where surrogate_id = '" + id + "'";
		Request r = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_surr_id = rs.getInt("id_user");
				int equipment_surr_id = rs.getInt("id_equipment");
				Date isd = rs.getDate("issue_date");
				LocalDate issue_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					issue_date = isd.toLocalDate();
				}
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				String user_id = user_repo.getUserId(user_surr_id);
				String equipment_id = equipment_repo.getEquipmentId(equipment_surr_id);
				r = new Request(request_id, equipment_id, equipment_surr_id, user_id, user_surr_id, status, issue_date, return_date);
			}	
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return r;
	}
	
	/*
		Function to fetch the list of requests for an equipment from the database.
	*/
	public List<Request> getRequestsListForEquipment(String id) {
		List<Request> requests = new ArrayList<>();
		String sqlQuery = "select * from requests where id_equipment = (select surrogate_id from equipment where equipment_id = '"+ id +"')";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_surr_id = rs.getInt("id_user");
				int equipment_surr_id = rs.getInt("id_equipment");
				Date isd = rs.getDate("issue_date");
				LocalDate issue_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					issue_date = isd.toLocalDate();
				}
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				String user_id = user_repo.getUserId(user_surr_id);
				String equipment_id = equipment_repo.getEquipmentId(equipment_surr_id);
				Request r = new Request(request_id, equipment_id, equipment_surr_id, user_id, user_surr_id, status, issue_date, return_date);
				requests.add(r);
			}
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return requests;
	}
	
	/*
		Function to create an request in the database.
	*/
	public void createRequest(Request r) {
		String sqlQuery = "insert into requests (id_user,id_equipment,issue_date,return_date,request_status) values (?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setInt(1,r.getUserSurrId());
			st.setInt(2, r.getEquipmentSurrId());
			if(r.getIssueDate()==null) {
				st.setNull(3, Types.DATE);
			}
			else{
				st.setDate(3, Date.valueOf(r.getIssueDate()));
			}
			if(r.getReturnDate()==null) {
				st.setNull(4, Types.DATE);
			}
			else{
				st.setDate(4, Date.valueOf(r.getReturnDate()));
			}
			st.setString(5, r.getStatus());
			st.executeUpdate();
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	/*
		Function to edit an request in the database.
	*/
	public Request editRequest(int id, Request newR) {
		String sqlQuery = "UPDATE requests SET id_user=?,id_equipment=?,issue_date=?,return_date=?,request_status=? WHERE surrogate_id = " + id;
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
		    st.setInt(1, newR.getUserSurrId());
		    st.setInt(2, newR.getEquipmentSurrId());
		    if(newR.getIssueDate()==null) {
				st.setNull(3, Types.DATE);
			}
			else{
				st.setDate(3, Date.valueOf(newR.getIssueDate()));
			}
			if(newR.getReturnDate()==null) {
				st.setNull(4, Types.DATE);
			}
			else{
				st.setDate(4, Date.valueOf(newR.getReturnDate()));
			}
			st.setString(5, newR.getStatus());
		    st.executeUpdate();
		                                                             
		}catch (Exception ex) {
			System.out.println(ex);
		}
		return newR;
	}

	/*
		Function to edit the status of an request in the database.
	*/
	public String editRequestStatus(int id, String status) {
		String sqlQuery = "UPDATE requests SET request_status= '" + status + "' WHERE surrogate_id = " + id;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sqlQuery);
		}catch (Exception ex) {
			System.out.println(ex);
			return Constants.FAILURE_STATUS;
		}
		return Constants.SUCCESS_STATUS;
	}
	
	/*
		Function to edit the issued date of an request in the database.
	*/
	public String setRequestIssueDate(int id, LocalDate issue_date) {
		String sqlQuery = "UPDATE requests SET issue_date= ? WHERE surrogate_id = " + id;
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setDate(1, Date.valueOf(issue_date));
			st.executeUpdate();			
		}catch (Exception ex) {
			System.out.println(ex);
			return Constants.FAILURE_STATUS;
		}
		return Constants.SUCCESS_STATUS;
	}
	
	/*
		Function to edit the return date of an request in the database.
	*/
	public String setRequestReturnDate(int id, LocalDate return_date) {
		String sqlQuery = "UPDATE requests SET return_date= ? WHERE surrogate_id = " + id;
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setDate(1, Date.valueOf(return_date));
			st.executeUpdate();			
		}catch (Exception ex) {
			System.out.println(ex);
			return Constants.FAILURE_STATUS;
		}
		return Constants.SUCCESS_STATUS;
	}

	/*
		Function to fetch the last modified date of an request from the database.
	*/
	public LocalDate getModifiedDate(int id) {
		String sqlQuery = "select updated_at from requests where surrogate_id = " + id;
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
