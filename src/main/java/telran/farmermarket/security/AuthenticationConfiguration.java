package telran.farmermarket.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;
import telran.farmermarket.auth.dto.exceptions.UserNotFoundException;
import telran.farmermarket.auth.models.UserAccount;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfiguration implements UserDetailsService {

	private final MongoTemplate template;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserAccount account = template.findById(username, UserAccount.class);

		if (account == null)
			throw new UserNotFoundException(username);

		String password = account.getHash();
		String[] authorities = account.getRoles().stream().map(r -> "ROLE_" + r).toArray(String[]::new);

		return new User(username, password, AuthorityUtils.createAuthorityList(authorities));
	}

}
