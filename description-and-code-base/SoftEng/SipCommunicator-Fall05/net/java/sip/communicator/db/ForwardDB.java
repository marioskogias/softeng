package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ForwardDB {
	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public ForwardDB() {
		dbCred = new ParseXMLCredentials();
	}

	public String getForward(String username) {
		String forwardTo = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("SELECT forwardTo FROM forwarding where forwardFrom = ?");

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				forwardTo = rs.getString("forwardTo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return forwardTo;
	}

	public void setForward(String fromUser, String toUser)
			throws NoSuchElementException, RuntimeException {
		throw new RuntimeException();
	}
}
