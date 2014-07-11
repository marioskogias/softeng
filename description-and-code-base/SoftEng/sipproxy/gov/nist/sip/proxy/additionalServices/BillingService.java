package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

import gov.nist.sip.proxy.additionalServices.FriendsFactory;
import gov.nist.sip.proxy.additionalServices.GeneralFactory;
import gov.nist.sip.proxy.additionalServices.FreeSundayFactory;

public class BillingService {

	private final BillingDB mBillingDB;

	public BillingService() {
		mBillingDB = new BillingDB();
	}

	public boolean startBilling(Request request) {
		HeaderAddress header = (HeaderAddress) request
				.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		return mBillingDB.addBillingRecord(username);
	}

	long calculateDuration(long startTime) {
		return (long) Math.ceil((System.currentTimeMillis() - startTime)
		//		/ (double) 1000 / 60);
				/ (double) 1000 );
	}

	public boolean stopBilling(Request request) {
		HeaderAddress header = (HeaderAddress) request
				.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		header = (HeaderAddress) request.getHeader(ToHeader.NAME);
		String to_user = getUsernameFromHeader(header);
		BillingInfo info = mBillingDB
				.getCurrentBillingRecord(to_user, username);
		try {
			long duration = calculateDuration(info.startTime);

			String caller = info.caller;
			String callee = username.equals(caller) ? to_user : username;
			
			ChargerFactory factory = chooseFactory(caller);
			Charger charger = factory.createCharger();
			

			long cost = charger.charge(caller, callee, duration);

			return mBillingDB.finalizeBillingRecord(info.id, duration, cost);

		} catch (NullPointerException e) {
			System.err.println("Caught IOException: " + e.getMessage());
			return false;
		}
	}

	private String getUsernameFromHeader(HeaderAddress header) {
		URI uri = header.getAddress().getURI();
		String uriString = uri.toString();
		return uriString.substring(uriString.indexOf("sip:") + 4,
				uriString.indexOf("@"));
	}

	private ChargerFactory chooseFactory(String username) {
		switch (mBillingDB.getPlan(username)) {
		case 1:
			FriendsFactory friendfactory = new FriendsFactory();
			return friendfactory;
		case 2:
			FreeSundayFactory sundayfactory = new FreeSundayFactory();
			return sundayfactory;
		default:
			GeneralFactory factory = new GeneralFactory();
			return factory;
		}
	}
}
