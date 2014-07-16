package gov.nist.sip.proxy.additionalServices;

import java.util.Calendar;


public class FreeSundayCharger extends Charger {
	
	int DEFAULT_COST = 10;
	
	@Override
	public long charge(String username, String to_user, long duration) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1){
			return 0;
		}
		else{
			return duration*DEFAULT_COST;
		}
	}
}
