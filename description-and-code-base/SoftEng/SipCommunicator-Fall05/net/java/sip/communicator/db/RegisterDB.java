package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterDB {

	private ParseXMLCredentials dbCred;

	Connection conn = null;
	Statement stmt = null;

	public RegisterDB() {
		dbCred = new ParseXMLCredentials();
	}

	public void registerToDB(String username, String passwd, String email,
			String creditCard, int plan) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
			stmt = conn.createStatement();
			String sql = String
					.format("INSERT INTO users set username = '%s', email = '%s', password = '%s', creditCard = '%s', plan = '%d'",
							username, email, passwd, creditCard, plan);
			System.out.println(sql);
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*public static void main(String[] args) {
		RegisterDB r = new  RegisterDB();
		r.registerToDB("test","test","test","test");
	}
	*/
}
