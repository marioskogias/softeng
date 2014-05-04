package gov.nist.sip.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BlockDB {

	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public BlockDB() {
		dbCred = new ParseXMLCredentials();
	}

	public boolean getBlock(String blocked, String blockedFrom) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("SELECT blocked FROM blocking where blockedFrom = ? AND blocked = ?");
			stmt.setString(1, blockedFrom);
			stmt.setString(2, blocked);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (blocked.contentEquals(rs.getString("blocked"))){
					return true;
				}
			} else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
