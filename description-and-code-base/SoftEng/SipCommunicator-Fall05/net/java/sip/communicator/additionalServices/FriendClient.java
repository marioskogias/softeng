package net.java.sip.communicator.additionalServices;

import java.util.NoSuchElementException;

import net.java.sip.communicator.db.FriendlistDB;

public class FriendClient {

	FriendlistDB dbManager;
	
	public FriendClient() {
		dbManager = new FriendlistDB();
	}
	
	public void enableBlock(){
		
	}
	public String getFriends(String username) {
		return dbManager.getFriends(username);
	}
	
	/**
	 * 
	 * @param fromUser
	 * @param toUser
	 * @throws NoSuchElementException in case no such user found
	 * @throws RuntimeException in case of a circle forwarding
	 */
	public void addFriend(String fromUser, String toUser, String relation) throws NoSuchElementException, RuntimeException{
		dbManager.addFriend(fromUser, toUser, relation);
	}
	
	public void removeFriend(String fromUser, String toUser) {
		dbManager.removeFriend(fromUser, toUser);
	}
}
