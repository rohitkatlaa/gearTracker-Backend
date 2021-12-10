package com.geartracker.geartracker_backend;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class UserRepository {
	private Connection conn = null;
	private static UserRepository repo = null; 

	private UserRepository() {
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
	
	public static UserRepository getInstance() {
		if(repo==null) {
			repo = new UserRepository();
		}
		return repo;
	}

	public int getSurrogateId(String id) {
		String sqlQuery = "select surrogate_id from user where user_id = '" + id + "'";
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

	public String getUserId(int id) {
		String sqlQuery = "select user_id from user where surrogate_id = '" + id + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				String e_id = rs.getString("user_id");
				return e_id;
			}

		} catch(Exception exc) {
			System.out.println(exc);
		}
		return Constants.FAILURE_STATUS;
	}


	public User login(String id, String password) //Return user if id and password exists else return null
	{
		String sqlQuery1 = "select * from user where user_id = '" + id + "' AND password = '" + password + "'";
		User u = new User();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery1);
			if (!rs.isBeforeFirst()) {
				return null;
			}
			if(rs.next()) {
				u.setId(rs.getString("user_id"));
				u.setName(rs.getString("name"));
				u.resetPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				Integer val = rs.getInt("student");
				if(rs.wasNull())
					u.setStudent(Constants.STUDENT_DUMMY);
				else
					u.setStudent(val);
			}
			String sqlQuery2 = "select role from user_role where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
			ResultSet rs2 = st.executeQuery(sqlQuery2);
			while(rs2.next()) {
				u.add_roles(rs2.getString("role"));
			}
			if(u.getRoles().contains("student"))
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
				u.resetPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				Integer val = rs.getInt("student");
				if(rs.wasNull())
					u.setStudent(Constants.STUDENT_DUMMY);
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
				}				
			}
			for (User e : users) {
				if(e.getRoles().contains("student"))
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
				u.resetPassword(rs.getString("password"));
				u.setEmail(rs.getString("email"));
				Integer val = rs.getInt("student");
				if(rs.wasNull())
					u.setStudent(Constants.STUDENT_DUMMY);
				else
					u.setStudent(val);
			}
			String sqlQuery2 = "select role from user_role where id_user = (select surrogate_id from user where user_id = '"+ id +"')";
			ResultSet rs2 = st.executeQuery(sqlQuery2);
			while(rs2.next()) {
				u.add_roles(rs2.getString("role"));
			}
			if(u.getRoles().contains("student"))
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

	public User createUser(User u) {
		ResultSet rs4 = null;
        int stu_surrogate_id = 0;
		try {
			String sqlQuery1 = "insert into user (user_id,name,password,email,student) values (?,?,?,?,?)";
			PreparedStatement st = conn.prepareStatement(sqlQuery1);
			st.setString(1,u.getId());
			st.setString(2,u.getName());
			st.setString(3,u.getPassword());
			st.setString(4,u.getEmail());
			if(u.getRoles().contains("student"))
			{
				String sqlQuery4 = "insert into student (fine,sports_team) values (?,?)";
				PreparedStatement st2 = conn.prepareStatement(sqlQuery4,Statement.RETURN_GENERATED_KEYS);
				st2.setInt(1, u.getFine());
				if(u.getSportsStatus())
					st2.setInt(2, 1);
				else
					st2.setInt(2, 0);
				int row_affected = st2.executeUpdate();
				if(row_affected==1)
				{
					rs4=st2.getGeneratedKeys();
					if(rs4.next())
						stu_surrogate_id = rs4.getInt(1); 
				}
				st.setInt(5, stu_surrogate_id);
				st.executeUpdate();
			}
			else
			{
				st.setInt(5, Constants.STUDENT_DUMMY);
				st.executeUpdate();
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
		} catch(Exception exc) {
			System.out.println(exc);
		}
		return getUserById(u.getId());
	}

	public User editUser(String id,User newU)
	{
		String sqlQuery1 = "UPDATE user SET name=?,password=?,email=? WHERE user_id = '" + id + "'";
		try{
			PreparedStatement st = conn.prepareStatement(sqlQuery1);
			st.setString(1,newU.getName());
			st.setString(2,newU.getPassword());
			st.setString(3,newU.getEmail());
			st.executeUpdate();
			if(newU.getRoles().contains("student"))
			{
				String sqlQuery2 = "update student SET fine=?,sports_team=? where surrogate_id = "+ newU.getStudent();
				PreparedStatement st2 = conn.prepareStatement(sqlQuery2);
				st2.setInt(1, newU.getFine());
				System.out.println(newU.getSportsStatus());
				if(newU.getSportsStatus())
					st2.setInt(2, 1);
				else
					st2.setInt(2, 0);
				st2.executeUpdate();
			}
			ArrayList<String> old_roles = new ArrayList<String>();
			Statement st3 = conn.createStatement();
			String sqlQuery4 = "select surrogate_id from user where user_id ='"+ id +"'";
			ResultSet rs2 = st3.executeQuery(sqlQuery4);
			rs2.next();
			int _id = rs2.getInt("surrogate_id");
			String sqlQuery3 = "select role from user_role where id_user = "+ _id ;
			ResultSet rs = st3.executeQuery(sqlQuery3);
			while(rs.next()) {
				old_roles.add(rs.getString("role"));
			}
			ArrayList<String> new_roles = newU.getRoles();


			for (String e : old_roles) {
				if(!new_roles.contains(e))
				{
					String sqlDel = "delete from user_role where id_user = "+ _id + " AND role = '" + e + "'";
					System.out.println("I was deleting "+ sqlDel);
					st3.executeUpdate(sqlDel);
				}
			}
			for (String e : new_roles) {
				if(!old_roles.contains(e))
				{
					String sqlAdd = "insert into user_role (id_user, role) values ("+_id+",'"+ e +"')";
					System.out.println("I was inserting "+ sqlAdd);
					st3.executeUpdate(sqlAdd);
				}
			}
		}
		catch(Exception exc) {
			System.out.println(exc);
		} 
		return getUserById(newU.getId()); 
	}
	
	public String deleteUser(String id) {
		String sqlQuery_delete = "DELETE from user WHERE id = '" + id +"'";
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

