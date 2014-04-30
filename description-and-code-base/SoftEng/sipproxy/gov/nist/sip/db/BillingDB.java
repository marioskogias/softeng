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
	
	public boolean finalizeBillingRecord(String username) {
		try {
			ResultSet query = getCurrentBillingRecord(username);
			if (query == null) {
				return false;
			}
			long id = query.getLong("id");
			long startTime = query.getLong("start_time");
			query.close();
			connectIfNeeded();
			String sql = "UPDATE billing SET duration=? WHERE id=? LIMIT 1;";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			long duration = (System.currentTimeMillis() - startTime) / 1000 / 60; 
			statement.setLong(1, duration);
			statement.setLong(2, id);
			int result = statement.executeUpdate();
			return result > 0;
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
