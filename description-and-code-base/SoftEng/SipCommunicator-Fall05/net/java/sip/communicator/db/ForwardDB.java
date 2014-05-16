package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class ForwardDB {
	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public ForwardDB() {
		dbCred = new ParseXMLCredentials();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getForward(String username) {
		String forwardTo = "";
		try {
			if (conn == null)
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
		}

		return forwardTo;
	}

	public void setForward(String fromUser, String toUser)
			throws NoSuchElementException, RuntimeException {

		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// first check if there is such user
		try {
			stmt = conn
					.prepareStatement("SELECT * FROM users where username = ?");
			stmt.setString(1, toUser);
			ResultSet rs = stmt.executeQuery();
			if ((rs == null) || (!rs.next()))
				throw new NoSuchElementException();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// second check if circle
		HashSet<String> userSet = new HashSet<String>();
		userSet.add(fromUser);
		userSet.add(toUser);

		try {
			stmt = conn
					.prepareStatement("SELECT forwardTo FROM forwarding where forwardFrom = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String tempFrom = toUser;
		String forwardTo;
		while (true) {
			try {
				stmt.setString(1, tempFrom);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					forwardTo = rs.getString("forwardTo");
					if (!userSet.add(forwardTo))
						throw new RuntimeException();
					tempFrom = forwardTo;
				} else
					break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/*
		 * ready to set the new forward first delete the old if exists and then
		 * set the new
		 */

		try {
			stmt = conn
					.prepareStatement("delete from forwarding where forwardFrom=?");
			stmt.setString(1, fromUser);
			stmt.executeUpdate();
			stmt = conn
					.prepareStatement("insert into forwarding set forwardFrom=?, forwardTo=?");
			stmt.setString(1, fromUser);
			stmt.setString(2, toUser);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void resetForward(String user) {
		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("delete from forwarding where forwardFrom=?");
			stmt.setString(1, user);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
