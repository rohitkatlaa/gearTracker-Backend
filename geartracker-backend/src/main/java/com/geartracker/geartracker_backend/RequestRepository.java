package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDate;

public class RequestRepository {

	List<Request> requests = new ArrayList<>();
	Connection conn = null;
	
	
	public RequestRepository() {
		String url = "jdbc:mysql://localhost:3306/geartracker_db?verifyServerCertificate=false&useSSL=true";
		String username = "root";
		String password = "narrativearc";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			String sqlQuery = "select * from requests";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				Request r = new Request();
				r.setRequestId(rs.getInt("request_id"));
				r.setUserId(rs.getString("id_user"));
				r.setEquipmentId(rs.getString("id_equipment"));
				r.setIssueDate(rs.getDate("issue_date").toLocalDate());
				Date rd = rs.getDate("return_date");
				if(rs.wasNull())
				{
					r.setReturnDate((LocalDate) null);
				}
				else
				{
					r.setReturnDate(rd.toLocalDate());
				}
				r.setStatus(rs.getString("request_status"));
				this.requests.add(r);
			}
		} catch(Exception e) {
			System.out.println("hello");
			System.out.println(e);
		}
		
	}
	
	public List<Request> getRequestsList() {
		return this.requests;
	}
	
	public Request getRequestById(int id) {
		for(Request r: this.requests) {
			if(r.getRequestId() == id) {
				return r;
			}
		}
		return null;
	}
	
	public void createRequest(Request r) {
		//Updating the local list
		this.requests.add(r);
		//Updating the database
		String sqlQuery = "insert into requests (request_id,id_user,id_equipment,issue_date,return_date,request_status) values (?,?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setInt(1,r.getRequestId());
			st.setString(2,r.getUserId());
			st.setString(3, r.getEquipmentId());
			st.setDate(4, Date.valueOf(r.getIssueDate()));
			if(r.getReturnDate()==null) {
				st.setNull(5, Types.DATE);
			}
			else{
				st.setDate(5, Date.valueOf(r.getReturnDate()));
			}
			st.setString(6, r.getStatus());
			st.executeUpdate();
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	public Request editRequest(int id, Request newR) {
		//Updating the local list
		for(Request r: this.requests) {
			if(r.getRequestId() == id) {
				r.setRequestId(newR.getRequestId());
				r.setUserId(newR.getUserId());
				r.setEquipmentId(newR.getEquipmentId());
				r.setIssueDate(newR.getIssueDate());
				if(newR.getReturnDate() == null)
				{
					r.setReturnDate((LocalDate) null);
				}
				else
				{
					r.setReturnDate(newR.getReturnDate());
				}
				r.setStatus(newR.getStatus());
			}
		}
		//Updating the database
		String sqlQuery = "UPDATE requests SET request_id=?,id_user=?,id_equipment=?,issue_date=?,return_date=?,request_status=? WHERE request_id = " + id;
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery);
			st.setInt(1, newR.getRequestId());
		    st.setString(2, newR.getUserId());
		    st.setString(3, newR.getEquipmentId());
		    st.setDate(4, Date.valueOf(newR.getIssueDate()));
			if(newR.getReturnDate()==null) {
				st.setNull(5, Types.DATE);
			}
			else{
				st.setDate(5, Date.valueOf(newR.getReturnDate()));
			}
			st.setString(6, newR.getStatus());
		    st.executeUpdate();
		                                                             
		}catch (Exception ex) {
			System.out.println(ex);
		}
		return newR;
	}

}
