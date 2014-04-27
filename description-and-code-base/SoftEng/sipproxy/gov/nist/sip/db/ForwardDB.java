package gov.nist.sip.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForwardDB {

	private ParseXMLCredentials dbCred;

	Connection conn = null;
	PreparedStatement stmt = null;

	public ForwardDB() {
		dbCred = new ParseXMLCredentials();
	}

	public String getForward(String from) {
		String forwardTo = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbCred.getUrl(),
					dbCred.getUsername(), dbCred.getPassword());
			stmt = conn
					.prepareStatement("SELECT forwardTo FROM forwarding where forwardFrom = ?");
			
			String tempFrom = from; 
			while (true) {
				stmt.setString(1, tempFrom);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					forwardTo = rs.getString("forwardTo");
					tempFrom = forwardTo;
				} else
					break;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forwardTo;
	}
	
	/*public static void main(String[] args) {
		ForwardDB f = new ForwardDB();
		System.out.print("Forward to "+ f.getForward("marios"));
	}
	*/
}
