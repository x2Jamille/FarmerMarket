package telran.farmermarket.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.farmermarket.auth.models.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserRequestDto {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
}
