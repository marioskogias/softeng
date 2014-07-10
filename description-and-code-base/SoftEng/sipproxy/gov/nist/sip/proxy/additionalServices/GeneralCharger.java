package gov.nist.sip.proxy.additionalServices;


public class GeneralCharger extends Charger{

	@Override
	public void charge(String username, String to_user, long duration) {
			System.out.print(duration*10);
	}
}
