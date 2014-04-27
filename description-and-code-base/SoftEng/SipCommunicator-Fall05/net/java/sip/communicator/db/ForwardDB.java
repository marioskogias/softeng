package net.java.sip.communicator.db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.NoSuchElementException;

public class ForwardDB {
	private ParseXMLCredentials dbCred;

	Connection conn = null;
	Statement stmt = null;

	public ForwardDB() {
		dbCred = new ParseXMLCredentials();
	}
	
	public String getForward(String username) {
		return "forward_to";
	}
	
	public void setForward(String fromUser, String toUser)  throws NoSuchElementException, RuntimeException{
		throw new RuntimeException();
	}
}
