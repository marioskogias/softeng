package gov.nist.sip.db;

import gov.nist.sip.proxy.additionalServices.BillingInfo;
import gov.nist.sip.proxy.additionalServices.BillingService;

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
			//statement.setLong(2, -1);
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
	
	public String getRelation(String username, String to_user){
		try {
			connectIfNeeded();
			String sql =  "SELECT relation FROM friendlist where fromuser = ? AND touser = ?;";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, to_user);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getString("relation");
			} else
				return null;
		} catch (SQLException e) {
			throw new RuntimeException("Issue while inserting into the database", e);
		}
	}

	public boolean finalizeBillingRecord(int id, long duration, long cost) {
		try {
			connectIfNeeded();
			String sql = "UPDATE billing SET duration=?, cost=? WHERE id=? LIMIT 1;";
			PreparedStatement statement = mConnection.prepareStatement(sql);
			statement.setLong(1, duration);
			statement.setLong(2, cost);
			statement.setLong(3, id);
			int result = statement.executeUpdate();
			return (result > 0);
		} catch (SQLException e) {
			throw new RuntimeException("Issue while updating the database", e);
		}
	}
		
	public BillingInfo getCurrentBillingRecord(String username1, String username2) {
		try {
			connectIfNeeded();
			String selectSql = "SELECT id, start_time, username FROM billing WHERE (username=? OR username = ?) AND duration=?;";
			PreparedStatement statement = mConnection.prepareStatement(selectSql);
			statement.setString(1, username1);
			statement.setString(2, username2);
			statement.setLong(3, -1);
			System.out.format("\n\n\n\nThe query is %s \n\n\n\n", statement.toString());
			ResultSet query = statement.executeQuery();
			if (!query.first()) {
				return null;
			}
			return new BillingInfo(query.getInt("id"), query.getLong("start_time"), query.getString("username"));
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
