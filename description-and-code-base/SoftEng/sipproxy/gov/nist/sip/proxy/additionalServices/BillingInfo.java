package gov.nist.sip.proxy.additionalServices;

public class BillingInfo {
	int id;
	long startTime;
	String caller;
	
	public BillingInfo(int id, long startTime, String caller) {
		this.id = id;
		this.startTime = startTime;
		this.caller = caller;
	}
}
