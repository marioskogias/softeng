package net.java.sip.communicator.sip.security;

public class SingleLogin implements RegistrationState {

	SecurityAuthority authority;

	public SingleLogin(SecurityAuthority auth) {
		authority = auth;
	}

	@Override
	public UserCredentials getCredentials(String realm,
			UserCredentials defaultValues) {
		return authority.obtainCredentials(realm, defaultValues);
	}
}
