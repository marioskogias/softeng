package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class BlockDB {
	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public BlockDB() {
		dbCred = new ParseXMLCredentials();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getBlocks(String username) {
		String blocklist = "";
		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("SELECT blocked FROM blocking where blockedFrom = ?");

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				
				blocklist = blocklist + rs.getString("blocked") + "\n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return blocklist;
	}

	public void blockUser(String fromUser, String toUser)
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
		/*
		 * ready to set the new forward first delete the old if exists and then
		 * set the new
		 */
		try {
			stmt = conn
					.prepareStatement("SELECT * FROM blocking where blocked =? AND blockedFrom = ?");
			stmt.setString(2, fromUser);
			stmt.setString(1, toUser);
			ResultSet re = stmt.executeQuery();
			if (re.next())
				return;
		} catch (SQLException e){
			e.printStackTrace();
		}
		try {
			stmt = conn
					.prepareStatement("insert into blocking set blockedFrom=?, blocked=?");
			stmt.setString(1, fromUser);
			stmt.setString(2, toUser);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void unblockUser(String fromUser, String toUser) {
		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("delete from blocking where blocked=? and blockedFrom=?");
			stmt.setString(1, toUser);
			stmt.setString(2, fromUser);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
