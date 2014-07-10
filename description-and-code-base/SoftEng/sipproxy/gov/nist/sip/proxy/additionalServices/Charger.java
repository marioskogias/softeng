package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

public class Charger {
	private final BillingDB mBillingDB;

	public Charger() {
		mBillingDB = new BillingDB();
	}
	
	public void charge(String username, String to_user, long duration) {
		// overrided
	}
}
