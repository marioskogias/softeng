package net.java.sip.communicator.forwardService;

import java.util.NoSuchElementException;

public class ForwardClient {

	public String getForward(String username) {
		return "forward_to";
	}
	
	public void setForward(String fromUser, String toUser) throws NoSuchElementException{
		throw new NoSuchElementException();
	}
}
