package gov.nist.sip.proxy.registrar;

import java.sql.*;
public class RegisterDB {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://snf-196443.vm.okeanos.grnet.gr/sip";

	// Database credentials
	static final String USER = "sipuser";
	static final String PASS = "123456";

	Connection conn = null;
	Statement stmt = null;


	public boolean checkRegister(String username) {

			try {	
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT id FROM users where username = '" + username + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return false;
	}
}