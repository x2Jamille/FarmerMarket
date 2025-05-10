package telran.farmermarket.auth.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.farmermarket.auth.models.UserAccount;
import telran.farmermarket.auth.security.model.User;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	final MongoTemplate template;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
	
		if (checkEndpoint(req.getMethod(), req.getServletPath())) {
			String auth = req.getHeader("Authorization");

			if (auth == null) {
				resp.sendError(401, "Header Authorization doesn't exists");
				return;
			}

			try {
				String[] basicAuth = auth.split(" ");
				String decode = new String(Base64.getDecoder().decode(basicAuth[1]));
				String[] credentials = decode.split(":");
				String login = credentials[0];
				String password = credentials[1];

				UserAccount account = template.findById(login, UserAccount.class);

				if (account == null || !BCrypt.checkpw(password, account.getHash())) {
					resp.sendError(401, "User or password not valid");
					return;
				}
				
				req = new WrapperRequest(req, login, account.getRoles());
			} catch (Exception e) {
				resp.sendError(401, "Authorization not valid");
				return;
			}

		}
		chain.doFilter(req, resp);
	}

	private boolean checkEndpoint(String method, String servletPath) {
		return !(method.equals("POST") && servletPath.matches("/account/register"));
	}

	private class WrapperRequest extends HttpServletRequestWrapper{
		
		String login;
		Set<String> roles;
		
		public WrapperRequest(HttpServletRequest request, String login, Set<String> roles) {
			super(request);
			this.login = login;
			this.roles = roles;
		}
		
		@Override
		public Principal getUserPrincipal() {
			return new User(login, roles);
		}
	}
}
