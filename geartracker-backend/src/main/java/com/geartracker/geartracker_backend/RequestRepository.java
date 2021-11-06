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
		} catch(Exception e) {
			System.out.println("hello");
			System.out.println(e);
		}
		
	}
	
	public List<Request> getRequestsList() {
		String sqlQuery = "select * from requests";
		try {
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
			System.out.println(e);
		}

		return this.requests;
	}
	
	public Request getRequestById(String id) {
		String sqlQuery = "select * from requests where request_id = '" + id + "'";
		Request r = new Request();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				r.setRequestId(rs.getInt("request_id"));
				r.setUserId(rs.getString("id_user"));
				r.setEquipmentId(rs.getString("id_equipment"));
				r.setIssueDate(rs.getDate("issue_date").toLocalDate());
				Date rd = rs.getDate("return_date");
				if(rs.wasNull())
				{
					System.out.println("null return date");
					r.setReturnDate((LocalDate) null);
				}
				else
				{
					System.out.println("Rona aa rha hai");
					r.setReturnDate(rd.toLocalDate());
				}
				r.setStatus(rs.getString("request_status"));
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return r;
	}
	
	public void createRequest(Request r) {
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
	
	public Request editRequest(String id, Request newR) {
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
