package gov.nist.sip.proxy.additionalServices;

public class FreeSundayFactory implements ChargerFactory{

	@Override
	public Charger createCharger() {
		return new FreeSundayCharger();
	}

}
