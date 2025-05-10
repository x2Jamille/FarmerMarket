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
public class UserResponseDto {
	private String email;
	private String firstName;
	private String lastName;
	private Set<Role> roles;
}
