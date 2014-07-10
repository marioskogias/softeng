package gov.nist.sip.proxy.additionalServices;

import java.util.Calendar;

public class FreeSundayCharger extends Charger {
	@Override
	public void charge(String username, String to_user, long duration) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1){
			System.out.print(0);
		}
		else{
			System.out.print(duration*10);
		}
	}
}
