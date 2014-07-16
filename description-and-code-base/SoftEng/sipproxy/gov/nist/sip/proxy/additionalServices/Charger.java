package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

public class Charger {
	protected final BillingDB mBillingDB;

	public Charger() {
		mBillingDB = new BillingDB();
	}
	
	public long charge(String username, String to_user, long duration) {
		throw new  UnsupportedOperationException();
	}
}
