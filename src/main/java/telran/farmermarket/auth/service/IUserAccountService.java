package telran.farmermarket.auth.service;

import java.time.LocalDateTime;

import telran.farmermarket.auth.dto.RolesResponseDto;
import telran.farmermarket.auth.dto.UserRequestDto;
import telran.farmermarket.auth.dto.UserResponseDto;
import telran.farmermarket.auth.dto.UserUpdateDto;

public interface IUserAccountService {
	
	UserResponseDto registration(UserRequestDto user);
	UserResponseDto removeUser(String login);
	UserResponseDto getUser(String login);
	UserResponseDto updateUser(String login, UserUpdateDto user);

	boolean updatePassword(String login, String password);
	boolean revokeAccount(String login);
	boolean activateAccount(String login);
	
	RolesResponseDto addRole(String login, String role);
	RolesResponseDto removeRole(String login, String role);
	
	String getPasswordHash(String login);
	LocalDateTime getActivationDate(String login);
	RolesResponseDto getRoles(String login);
}
