package telran.farmermarket.auth.security;

import java.io.IOException;

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
@Order(32)
public class ClientFilter implements Filter {

	final MongoTemplate template;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (checkEndpoint(req.getMethod(), req.getServletPath())) {
			User principal = (User) req.getUserPrincipal();

			if (!principal.getRoles().contains(Role.CLIENT.toString())) {
				resp.sendError(403);
				return;
			}
		}
		chain.doFilter(req, resp);
	}

	private boolean checkEndpoint(String method, String servletPath) {
		return method.equals("PATCH") && (servletPath.matches("/fmarket/farmer/product/sell"));

	}

}
