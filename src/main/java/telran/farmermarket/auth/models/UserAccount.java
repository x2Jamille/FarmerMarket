package telran.farmermarket.auth.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import telran.farmermarket.auth.dto.RolesResponseDto;
import telran.farmermarket.auth.dto.UserResponseDto;

@Getter
@Setter
@Document(collection = "accounts")
public class UserAccount {
	@Id
	@Setter(value = AccessLevel.NONE)
	@Indexed(unique = true)
	private String email;
	private String hash;
	private String firstName;
	private String lastName;
	private Set<Role> roles;
	private LocalDateTime activationDate;

	public UserAccount(String email, String hash, String firstName, String lastName) {
		this();
		this.email = email;
		this.hash = hash;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public UserAccount() {
		roles = new HashSet<>();
//		roles.add(Role.USER);
		activationDate = LocalDateTime.now();
	}
	
	public boolean addRole(String role) {
		return roles.add(Role.valueOf(role.toUpperCase()));
	}
	
	public boolean removeRole(String role) {
		return roles.remove(Role.valueOf(role.toUpperCase()));
	}

	public UserResponseDto getUserResponseDto() {
		return UserResponseDto.builder().email(email).firstName(firstName).lastName(lastName).roles(roles).build();
	}
	
	public RolesResponseDto getRolesResponseDto() {
		return RolesResponseDto.builder().email(email).roles(roles).build();
	}
	
	public static class Fields{
		public static final String F_NAME = "firstName";
		public static final String L_NAME = "lastName";
		public static final String EMAIL = "email";		
	}
	
}
