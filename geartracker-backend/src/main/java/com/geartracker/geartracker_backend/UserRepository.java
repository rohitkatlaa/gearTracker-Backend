package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class UserRepository {
	Connection conn = null;
	
	public UserRepository() {
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
	
	public List<User> getUsersList() {
		List<User> users = new ArrayList<>();
		String sqlQuery1 = "select * from user";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery1);
			while(rs.next()) {
				User u = new User();
				u.setId(rs.getString("user_id"));
				u.setName(rs.getString("name"));
				u.resetPassword(rs.getString("passwrd"));
				u.setEmail(rs.getString("email"));
				Integer val = rs.getInt("student");
				if(rs.wasNull())
					u.setStudent((Integer) null);
				else
					u.setStudent(val);	
				users.add(u);
			}
			for (User e : users) {
				String id = e.getId();
				String sqlQuery2 = "select role from user_role where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
				ResultSet rs2 = st.executeQuery(sqlQuery2);
				while(rs2.next()) {
					e.add_roles(rs2.getString("role"));
					System.out.println("Nachos");
				}				
			}
			for (User e : users) {
				if(e.getStudent()!= null)
				{
					Integer val = e.getStudent();
					String sqlQuery3 = "select fine,sports_team from student where surrogate_id ="+ val;
					ResultSet rs3 = st.executeQuery(sqlQuery3);
					rs3.next();
					e.setFine(rs3.getInt("fine"));
					int bool = rs3.getInt("sports_team");
					if(bool==0)
						e.setSportsStatus(false);
					else
						e.setSportsStatus(true);
				}	
			}	
		} catch(Exception e) {
			System.out.println(e);
		}
		return users;
	}

	public User getUserById(String id) {
		String sqlQuery1 = "select * from user where user_id = '" + id + "'";
		User u = new User();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery1);
			if(rs.next()) {
				u.setId(rs.getString("user_id"));
				u.setName(rs.getString("name"));
				u.resetPassword(rs.getString("passwrd"));
				u.setEmail(rs.getString("email"));
				Integer val = rs.getInt("student");
				if(rs.wasNull())
					u.setStudent((Integer) null);
				else
					u.setStudent(val);
			}
			String sqlQuery2 = "select role from user_role where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
			ResultSet rs2 = st.executeQuery(sqlQuery2);
			while(rs2.next()) {
				u.add_roles(rs2.getString("role"));
			}
			if(u.getStudent()!= null)
				{
					Integer val = u.getStudent();
					String sqlQuery3 = "select fine,sports_team from student where surrogate_id ="+ val;
					ResultSet rs3 = st.executeQuery(sqlQuery3);
					rs3.next();
					u.setFine(rs3.getInt("fine"));
					int bool = rs3.getInt("sports_team");
					if(bool==0)
						u.setSportsStatus(false);
					else
						u.setSportsStatus(true);
				}
		} catch(Exception exc) {
			System.out.println(exc);
			return null;
		}
		return u;
	}

	public void createUser(User u) {
		String sqlQuery1 = "insert into user (user_id,name,passwrd,email,student) values (?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(sqlQuery1);
			st.setString(1,u.getId());
			st.setString(2,u.getName());
			System.out.println(u.getName());
			st.setString(3,u.getPassword());
			System.out.println(u.getPassword());
			st.setString(4,u.getEmail());
			if(u.getStudent()==null)
			{
				st.setNull(5, Types.INTEGER);
				st.executeUpdate();
			}
			else
			{
				st.setInt(5,u.getStudent());
				st.executeUpdate();
				String sqlQuery2 = "insert into student (fine,sports_team) values (?,?)";
				PreparedStatement st2 = conn.prepareStatement(sqlQuery2);
				st2.setInt(1, u.getFine());
				if(u.getSportsStatus())
					st2.setInt(2, 1);
				else
					st2.setInt(2, 0);
				st2.executeUpdate();
			}
			int n = u.getRoles().size();
			String id = u.getId();
			String sqlQuery3 = "select surrogate_id from user where user_id ='"+ id +"'";
			Statement st3 = conn.createStatement();
			ResultSet rs = st3.executeQuery(sqlQuery3);
			rs.next();
			int _id = rs.getInt("surrogate_id");
			ArrayList<String> Queries = new ArrayList<String>();
			for(int i=0;i<n;i++)
			{
				Queries.add("insert into user_role (id_user, role) values ("+_id+",'"+u.getRoles().get(i)+"')");
			}
			for(int i=0;i<n;i++)
			{
				st3.addBatch(Queries.get(i));
			}
			st3.executeBatch();
			// for(int i=0;i<n;i++)
			// {
			// 	String sqlQuery3 = "insert into user_role (id_user,role) values (?,?)";
			// 	PreparedStatement st3 = conn.prepareStatement(sqlQuery3);
			// 	st3.setString(2, u.getRoles().get(i));
			// 	String sqlQuery4 = "select surrogate_id from user where user_id ='"+ id +"'";
			// 	Statement st4 = conn.createStatement();
			// 	ResultSet rs = st4.executeQuery(sqlQuery4);
			// 	rs.next();
			// 	st3.setInt(1, rs.getInt("surrogate_id"));
			// 	st3.executeUpdate();
			// }

		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	
}