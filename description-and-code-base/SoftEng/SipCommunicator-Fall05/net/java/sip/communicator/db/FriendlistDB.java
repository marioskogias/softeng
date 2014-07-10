package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class FriendlistDB {
	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public FriendlistDB() {
		dbCred = new ParseXMLCredentials();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getFriends(String username) {
		String friendlist = "";
		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("SELECT touser, relation FROM friendlist where fromuser = ? AND relation = friend");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				friendlist = friendlist + rs.getString("relation") + ": " + rs.getString("touser") + "\n";
			}
			if (friendlist.isEmpty()) {
				friendlist = "You have no contacts in your friendlist.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return friendlist;
	}

	public void addFriend(String fromUser, String toUser, String relation)
			throws NoSuchElementException, RuntimeException {

		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
		} catch (SQLException e) {
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
			e.printStackTrace();
		}
		try {
			stmt = conn
					.prepareStatement("SELECT * FROM friendlist where fromuser =? AND touser = ? AND relation = ?");
			stmt.setString(1, fromUser);
			stmt.setString(2, toUser);
			stmt.setString(3, relation);
			ResultSet re = stmt.executeQuery();
			if (re.next())
				return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt = conn
					.prepareStatement("insert into friendlist set fromuser=?, touser=?, relation=?");
			stmt.setString(1, fromUser);
			stmt.setString(2, toUser);
			stmt.setString(3, relation);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeFriend(String fromUser, String toUser) {
		try {
			if (conn == null)
				conn = DriverManager.getConnection(dbCred.getUrl(),
						dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("delete from friendlist where touser=? and fromuser=? ");
			stmt.setString(1, toUser);
			stmt.setString(2, fromUser);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}