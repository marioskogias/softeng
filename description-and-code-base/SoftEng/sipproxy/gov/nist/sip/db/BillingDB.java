package gov.nist.sip.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDB {

	private final ParseXMLCredentials mDatabaseCredentials;
	private Connection mConnection;

	public BillingDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Database driver not found", e);
		}
		mDatabaseCredentials = new ParseXMLCredentials();
	}

	public int getPlan(String username){
		try {
			connectIfNeeded();
			String selectSql = "SELECT plan FROM users WHERE username=?;";
			PreparedStatement statement = mConnection.prepareStatement(selectSql);
			statement.setString(1, username);
			statement.setLong(2, -1);
			ResultSet query = statement.executeQuery();
			if (!query.first()) {
				return 0;
			}
			return query.getInt("plan");
		} catch (SQLException e) {
			throw new RuntimeException("Issue while querying the database", e);
		}
	}
	public boolean addBillingRecord(String username) {
		try {
			connectIfNeeded();
			String sql = "INSERT INTO billing (username, start_time, duration) VALUES (?, ?, ?);";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setLong(2, System.currentTimeMillis());
			statement.setLong(3, -1);
			int result = statement.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Issue while inserting into the database", e);
		}
	}
	
	public boolean checkRelation(String username, String to_user, String relation){
		try {
			connectIfNeeded();
			String sql =  "SELECT * FROM friendlist where fromuser = ? AND touser = ? AND relation = ?;";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, to_user);
			statement.setString(3, relation);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			} else
				return false;
		} catch (SQLException e) {
			throw new RuntimeException("Issue while inserting into the database", e);
		}
	}

	public long finalizeBillingRecord(String username) {
		try {
			ResultSet query = getCurrentBillingRecord(username);
			if (query == null) {
				return 0;
			}
			long id = query.getLong("id");
			long startTime = query.getLong("start_time");
			query.close();
			connectIfNeeded();
			String sql = "UPDATE billing SET duration=? WHERE id=? LIMIT 1;";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			// the duration is in minutes and round up
			long duration = (long) Math.ceil((System.currentTimeMillis() - startTime) / (double) 1000 / 60); 
			System.out.println("The duration is "+ Long.toString(duration));
			statement.setLong(1, duration);
			statement.setLong(2, id);
			int result = statement.executeUpdate();
			if (result > 0){
				return duration;
			}
			else {
				return 0;
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Issue while updating the database", e);
		}
	}
		
	private ResultSet getCurrentBillingRecord(String username) {
		try {
			connectIfNeeded();
			String selectSql = "SELECT id, start_time FROM billing WHERE username=? AND duration=?;";
			PreparedStatement statement = mConnection.prepareStatement(selectSql);
			statement.setString(1, username);
			statement.setLong(2, -1);
			ResultSet query = statement.executeQuery();
			if (!query.first()) {
				return null;
			}
			return query;
		} catch (SQLException e) {
			throw new RuntimeException("Issue while querying the database", e);
		}
	}
	
	private void connectIfNeeded() {
		try {
			if ((mConnection == null) || (mConnection.isClosed())) {
				createConnection();
			}
		} catch (SQLException e) {
			createConnection();
		}
	}
	
	private void createConnection() {
		String url = mDatabaseCredentials.getUrl();
		String username = mDatabaseCredentials.getUsername();
		String password = mDatabaseCredentials.getPassword();
		try {
			mConnection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to the database", e);
		}
	}
}
