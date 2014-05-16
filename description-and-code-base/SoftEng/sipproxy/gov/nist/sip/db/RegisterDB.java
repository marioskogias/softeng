package gov.nist.sip.db;

import java.sql.*;
import java.util.HashMap;

public class RegisterDB {

	private ParseXMLCredentials dbCred;
	
	private String REAML = "192.168.1.6:4000";

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
		}
		return false;
	}

	public HashMap<String, String> getUserPasswords(String realm) {

		HashMap<String, String> res = new HashMap<String, String>();
		ResultSet rs;
		try {
			if (conn == null)
				register();
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT username, password FROM users ";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res.put(rs.getString("username") + "@" + realm, rs.getString("password") );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}
}