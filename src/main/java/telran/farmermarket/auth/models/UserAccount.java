package telran.farmermarket.auth.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

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
@Document(collection = "user-accounts")
public class UserAccount {
	@Id
	@Setter(value = AccessLevel.NONE)
//	private String id;
	@Indexed(unique = true)
	private String email;
	private String hash;
	private String firstName;
	private String lastName;
	private HashSet<String> roles = new HashSet<>();
	private LocalDateTime activationDate;
	private boolean revoked;
	private LinkedList<String> lastHash = new LinkedList<>();

	public UserAccount(String email, String hash, String firstName, String lastName) {
		super();
		this.email = email;
		this.hash = hash;
		this.firstName = firstName;
		this.lastName = lastName;
		roles.add("USER");
		activationDate = LocalDateTime.now();
	}

	public UserAccount() {
		roles.add("USER");
		activationDate = LocalDateTime.now();
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
