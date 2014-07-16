package gov.nist.sip.proxy.additionalServices;


public class GeneralCharger extends Charger{

	long DEFAULT_COST = 10;
	
	@Override
	public long charge(String username, String to_user, long duration) {
			return duration*DEFAULT_COST;
	}
}
