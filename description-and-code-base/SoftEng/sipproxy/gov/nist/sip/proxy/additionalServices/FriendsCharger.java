package gov.nist.sip.proxy.additionalServices;

public class FriendsCharger extends Charger {
	int FF_COST = 2;
	int DEFAULT_COST = 10;
	
	@Override
	public long charge(String username, String to_user, long duration) {
		String relation = mBillingDB.getRelation(username, to_user);
		if (relation.equals("friends") || relation.equals("family")){
			return FF_COST*duration; 
		}
		else{
			return DEFAULT_COST*duration; 
		}
	}
}
