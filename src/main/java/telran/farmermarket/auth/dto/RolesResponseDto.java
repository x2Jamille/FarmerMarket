package telran.farmermarket.auth.dto;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RolesResponseDto {
	private String email;
	private HashSet<String> roles;
}
