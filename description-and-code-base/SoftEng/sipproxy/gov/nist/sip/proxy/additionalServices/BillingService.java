package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderAddress;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

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
		HeaderAddress header = (HeaderAddress) request
				.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		
		ChargerFactory factory = chooseFactory(request);
		Charger charger = factory.createCharger();
		
		if (mBillingDB.finalizeBillingRecord(username)) {
			return true;
		} else {
			header = (HeaderAddress) request.getHeader(ToHeader.NAME);
			username = getUsernameFromHeader(header);
			return mBillingDB.finalizeBillingRecord(username);
		}
		//remove returns before
		charger.charge();
	}

	private String getUsernameFromHeader(HeaderAddress header) {
		URI uri = header.getAddress().getURI();
		String uriString = uri.toString();
		return uriString.substring(uriString.indexOf("sip:") + 4,
				uriString.indexOf("@"));
	}

	private ChargerFactory chooseFactory(Request request) {
		/*
		 * Based on request return the factory you want
		 * me switch
		 */
		
		return null;
	}
}
