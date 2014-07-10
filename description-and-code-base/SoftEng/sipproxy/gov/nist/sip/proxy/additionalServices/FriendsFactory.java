package gov.nist.sip.proxy.additionalServices;

public class FriendsFactory implements ChargerFactory{
	
	@Override
	public Charger createCharger() {
		return new FriendsCharger();
	}
}
