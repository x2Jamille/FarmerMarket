package telran.farmermarket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import static telran.farmermarket.auth.models.Role.*;

@Configuration
public class AuthorizationConfiguration {
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception{
		http
		.httpBasic(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable())
		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.authorizeHttpRequests(authorize -> 
			authorize
			
			.requestMatchers("/actuator/**").permitAll() //FIXME remove
			
			.requestMatchers(HttpMethod.POST, "/account/register")
			.permitAll()
			
			.requestMatchers("/account/user/*/role/*", "/fmarket/product/add", "/fmarket/clients")
			.hasRole(ADMIN.name())
			
			.requestMatchers(HttpMethod.DELETE ,"/account/user/{login}")
			.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
			
			.requestMatchers(HttpMethod.GET ,"/account/*/{login}")
			.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
			
			.requestMatchers(HttpMethod.PATCH, "/fmarket/farmer/product/add")
			.hasRole(FARMER.name())
			
			.requestMatchers(HttpMethod.PATCH, "/fmarket/product/sell")
			.hasRole(CLIENT.name())
			
			.anyRequest().authenticated());
		
		
		return http.build();
	}
	
}
