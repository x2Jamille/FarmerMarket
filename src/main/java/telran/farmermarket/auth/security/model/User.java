package telran.farmermarket.auth.security.model;

import java.security.Principal;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class User implements Principal{

	private final String login;
	private final Set<String> roles;
	
	
	
	@Override
	public String getName() {
		return login;
	}
	
	public boolean hasRole(String role) {
		return roles.contains(role);
	}
	
	public Set<String> getRoles(){
		return roles;
	}

}
