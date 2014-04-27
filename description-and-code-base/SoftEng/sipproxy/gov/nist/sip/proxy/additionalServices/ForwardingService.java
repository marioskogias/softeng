package gov.nist.sip.proxy.additionalServices;

import java.text.ParseException;

import gov.nist.sip.proxy.Proxy;

import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;

public class ForwardingService {

	Proxy proxy;

	public ForwardingService(Proxy p) {
		this.proxy = p;
	}

	public Request checkAndSetForwarding(Request request) {

		FromHeader header = (FromHeader) request.getHeader(FromHeader.NAME);
		if (header.getAddress().getURI().toString().contains("marios2")) {

			URI n;
			ToHeader newTo = null;
			try {
				n = proxy.getAddressFactory().createURI(
						"sip:marios3@192.168.1.6:4000");
				newTo = proxy.getHeaderFactory().createToHeader(
						proxy.getAddressFactory().createAddress(n), null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// request.setRequestURI(n);
			Request newreq = (Request) request.clone();
			newreq.setHeader(newTo);
			System.out.println("in my forward");
			return newreq;
		}
		return null;
	}
}
