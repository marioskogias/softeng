package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.message.Request;

public class BillingService {

	private final BillingDB mBillingDB;

	public BillingService() {
		mBillingDB = new BillingDB();
	}

	public boolean startBilling(Request request) {
		FromHeader header = (FromHeader) request.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		return mBillingDB.addBillingRecord(username);
	}
	
	public boolean stopBilling(Request request) {
		FromHeader header = (FromHeader) request.getHeader(FromHeader.NAME);
		String username = getUsernameFromHeader(header);
		return mBillingDB.finalizeBillingRecord(username);
	}

	private String getUsernameFromHeader(FromHeader header) {
		URI uri = header.getAddress().getURI();
		String uriString = uri.toString();
		return uriString.substring(uriString.indexOf("sip:") + 4,
				uriString.indexOf("@"));
	}
}
