package telran.farmermarket.auth.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.farmermarket.auth.security.enums.Role;
import telran.farmermarket.auth.security.model.User;

@Component
@RequiredArgsConstructor
@Order(30)
public class AdminOrAccountOwnerFilter implements Filter {

	final MongoTemplate template;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if (checkEndpoint(req.getMethod(), req.getServletPath())) {
			
			User principal = (User) req.getUserPrincipal();
			String login = principal.getName();
			Set<String> roles = principal.getRoles();

			System.out.println("Login: " + login);
			System.out.println("Roles: " + roles.toString());

			String[] path = req.getServletPath().split("/");
			if (!(roles.contains(Role.ADMIN.toString()) || path[path.length - 1].equals(login))) {
				resp.sendError(403);
				return;
			}
		}
		chain.doFilter(req, resp);
	}

	private boolean checkEndpoint(String method, String servletPath) {
		return (method.equals("DELETE") && (servletPath.matches("/account/user/[^/]+")))

				|| (method.equals("GET") && (servletPath.matches("/account/roles/[^/]+")
						|| servletPath.matches("/account/password/[^/]+")
						|| servletPath.matches("/account/activation_date/[^/]+")))
				|| (method.equals("PUT") && (servletPath.matches("/account/user/[^/]+")));
	}

}
