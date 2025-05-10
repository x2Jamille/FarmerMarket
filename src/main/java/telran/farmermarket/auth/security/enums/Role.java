package telran.farmermarket.auth.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
	UNAUTHORIZED("UN"), OWNER("OWNER"), ADMIN("ADMIN"), CLIENT("CLIENT"), FARMER("FARMER");

	private final String role;

	public String getAuthority() {
		return "ROLE_" + role;
	}
}
