package telran.farmermarket.auth.controllers;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.farmermarket.auth.dto.RolesResponseDto;
import telran.farmermarket.auth.dto.UserRequestDto;
import telran.farmermarket.auth.dto.UserResponseDto;
import telran.farmermarket.auth.dto.UserUpdateDto;
import telran.farmermarket.auth.service.IUserAccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

	final IUserAccountService service;

	@PostMapping("/register")
	public UserResponseDto registration(@RequestBody UserRequestDto user) {
		return service.registration(user);
	}

	@DeleteMapping("/user")
	public UserResponseDto removeUser(@PathVariable String login) {
		return service.removeUser(login);
	}

	@PostMapping("/login")
	public UserResponseDto login(Principal principal) {
		return service.getUser(principal.getName());
	}

	@PutMapping("/user/{login}")
	public UserResponseDto updateUser(@PathVariable String login, @RequestBody UserUpdateDto data) {
		return service.updateUser(login, data);
	}

	@PutMapping("/password")
	public boolean updatePassword(Principal principal, @RequestHeader("X-New-Password") String password) {
		return service.updatePassword(principal.getName(), password);
	}


	@PutMapping("/user/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {
		return service.changeRolesList(login, role, true);
	}
	
	@DeleteMapping("/user/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {
		return service.changeRolesList(login, role, false);
	}

	@GetMapping("/password/{login}")
	public String getPasswordHash(@PathVariable String login) {
		return service.getPasswordHash(login);
	}
	
	@GetMapping("/activation_date/{login}")
	public LocalDateTime getActivationDate(@PathVariable String login) {
		return service.getActivationDate(login);
	}
	
	@GetMapping("/roles/{login}")
	public RolesResponseDto getRoles(@PathVariable String login) {
		return service.getRoles(login);
	}
}
