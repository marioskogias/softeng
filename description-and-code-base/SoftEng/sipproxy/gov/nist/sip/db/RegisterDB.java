package gov.nist.sip.db;

import java.sql.*;

public class RegisterDB {

	private ParseXMLCredentials dbCred;

	Connection conn = null;
	Statement stmt = null;
	
	public RegisterDB() {
		dbCred = new ParseXMLCredentials();
		register();
	}

	private void register() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkRegister(String username) {

		System.out.println(dbCred.getUrl());
		try {
			
			if (conn == null)
				register();
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT id FROM users where username = '" + username + "'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("after results set");
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}
}