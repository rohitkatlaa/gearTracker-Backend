package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;

public class RequestRepository {

	List<Request> requests = new ArrayList<>();
	Connection conn = null;
	
	public RequestRepository() {
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
	
	public List<Request> getRequestsList() {
		List<Request> requests = new ArrayList<>();
		String sqlQuery = "select * from requests";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_id = rs.getInt("id_user");
				int equipment_id = rs.getInt("id_equipment");
				LocalDate issue_date = rs.getDate("issue_date").toLocalDate();
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				Request r = new Request(request_id, equipment_id, user_id, status, issue_date, return_date);
				requests.add(r);
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return requests;
	}
	
	public List<Request> getRequestsListForStudent(String id) {
		List<Request> requests = new ArrayList<>();
		String sqlQuery = "select * from requests where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_id = rs.getInt("id_user");
				int equipment_id = rs.getInt("id_equipment");
				LocalDate issue_date = rs.getDate("issue_date").toLocalDate();
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				Request r = new Request(request_id, equipment_id, user_id, status, issue_date, return_date);
				requests.add(r);
			}
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		return requests;
	}
	
	public Request getRequestById(int id) {
		String sqlQuery = "select * from requests where surrogate_id = '" + id + "'";
		Request r = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				int request_id = rs.getInt("surrogate_id");
				int user_id = rs.getInt("id_user");
				int equipment_id = rs.getInt("id_equipment");
				LocalDate issue_date = rs.getDate("issue_date").toLocalDate();
				Date rd = rs.getDate("return_date");
				LocalDate return_date = (LocalDate)null;
				if(!rs.wasNull())
				{
					return_date = rd.toLocalDate();
				}
				String status = rs.getString("request_status");
				r = new Request(request_id, equipment_id, user_id, status, issue_date, return_date);
			}	
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return r;
	}
	
	public void createRequest(Request r) {
		String sqlQuery = "insert into requests (id_user,id_equipment,issue_date,return_date,request_status) values (?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setInt(1,r.getUserId());
			st.setInt(2, r.getEquipmentId());
			st.setDate(3, Date.valueOf(r.getIssueDate()));
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
	
	public Request editRequest(int id, Request newR) {
		String sqlQuery = "UPDATE requests SET id_user=?,id_equipment=?,issue_date=?,return_date=?,request_status=? WHERE surrogate_id = " + id;
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
		    st.setInt(1, newR.getUserId());
		    st.setInt(2, newR.getEquipmentId());
		    st.setDate(3, Date.valueOf(newR.getIssueDate()));
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
}
