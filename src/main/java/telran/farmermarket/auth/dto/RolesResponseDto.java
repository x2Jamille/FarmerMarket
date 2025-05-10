package telran.farmermarket.auth.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.farmermarket.auth.models.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RolesResponseDto {
	private String email;
	private Set<Role> roles;
}
