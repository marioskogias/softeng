package gov.nist.sip.proxy.additionalServices;

import gov.nist.sip.db.BillingDB;

public class FriendsCharger extends Charger {
	@Override
	public void charge(String username, String to_user, long duration) {
		BillingDB mBillingDB = new BillingDB();
		if ((mBillingDB.checkRelation(username, to_user, "friends")) || 
				mBillingDB.checkRelation(username, to_user, "family")){
			System.out.print(duration*5);
		}
		else{
			System.out.print(duration*10);
		}
	}
}
