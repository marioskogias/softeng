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

	public boolean stopBilling(Request request) {
		boolean successful_charge = false;
		HeaderAddress header = (HeaderAddress) request
				.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		header = (HeaderAddress) request.getHeader(ToHeader.NAME);
		String to_user = getUsernameFromHeader(header);
		long duration = mBillingDB.finalizeBillingRecord(username);
		if (duration != 0) {
			ChargerFactory factory = chooseFactory(username);
			Charger charger = factory.createCharger();
			charger.charge(username, to_user, duration);
			successful_charge = true;
		} else {
			String tempname = username;
			username = to_user;
			to_user = tempname;
			duration = mBillingDB.finalizeBillingRecord(username);
			ChargerFactory factory = chooseFactory(username);
			Charger charger = factory.createCharger();
			charger.charge(username, to_user, duration);
			successful_charge = (duration > 0 );
		}
		return successful_charge;
	}

	private String getUsernameFromHeader(HeaderAddress header) {
		URI uri = header.getAddress().getURI();
		String uriString = uri.toString();
		return uriString.substring(uriString.indexOf("sip:") + 4,
				uriString.indexOf("@"));
	}

	private ChargerFactory chooseFactory(String username) {
		switch (mBillingDB.getPlan(username))
		{ 
		  case 0:
			  GeneralFactory factory = new GeneralFactory();
		        return factory;
		   case 1:
			  FriendsFactory friendfactory = new FriendsFactory();
			  return friendfactory;
		   case 2:
			   FreeSundayFactory sundayfactory = new FreeSundayFactory();
				  return sundayfactory;
		}
		return null;
	}
}
