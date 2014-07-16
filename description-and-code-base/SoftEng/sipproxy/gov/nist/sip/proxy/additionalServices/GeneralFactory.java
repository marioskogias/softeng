package gov.nist.sip.proxy.additionalServices;

public class GeneralFactory implements ChargerFactory{

	@Override
	public Charger createCharger() {
		return new GeneralCharger();
	}

}
