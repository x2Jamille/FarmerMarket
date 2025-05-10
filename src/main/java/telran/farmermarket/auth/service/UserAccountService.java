package telran.farmermarket.auth.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import telran.farmermarket.auth.dto.*;
import telran.farmermarket.auth.dto.exceptions.*;
import telran.farmermarket.auth.models.*;
import telran.farmermarket.shared.events.*; 

@Service
@RequiredArgsConstructor
public class UserAccountService implements IUserAccountService, CommandLineRunner {

	final ApplicationEventPublisher publisher;
	
	final MongoTemplate template;
	//TODO add values to app properties
	@Value("${min.password.length:8}")
	private int minPasswordLength;
	@Value("${n.last.hash:3}")
	private int n_last_hash;
	

	private String createHash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	private boolean isValidPassword(String password) {
		if (password == null)
			return false;
		if (password.length() < minPasswordLength)
			return false;
		// TODO add more validations
		return true;
	}

	private boolean isValidPassword(String newPassword, UserAccount account) {
		if (!isValidPassword(newPassword))
			return false;
		if (BCrypt.checkpw(newPassword, account.getHash()))
			return false;
		LinkedList<String> lastHash = account.getLastHash();
		return lastHash.stream().noneMatch(h -> BCrypt.checkpw(newPassword, h));
	}

	private UserAccount getUserAccount(String login) {
		UserAccount account = template.findById(login, UserAccount.class);
		if (account == null)
			throw new UserNotFoundException(login);
		return account;

	}

	@Override
	public UserResponseDto registration(UserRequestDto user) {
		String password = user.getPassword();
		String email = user.getEmail();
		String fName = user.getFirstName();
		String lName = user.getLastName();
		String role = user.getRole();

		if (!isValidPassword(password))
			throw new PasswordNotValidException(password);
		UserAccount account = new UserAccount(email, createHash(password), fName, lName);
		account.getRoles().add(role);
		try {
			template.insert(account);
			publisher.publishEvent(new UserRegisteredEvent(email, role));
		} catch (DuplicateKeyException e) {
			throw new UserExistsException(email);
		}

		return account.getUserResponseDto();
	}

	@Override
	public UserResponseDto removeUser(String login) {
		Query query = new Query(Criteria.where(UserAccount.Fields.EMAIL).is(login));
		UserAccount account = template.findAndRemove(query, UserAccount.class);

		if (account == null)
			throw new UserNotFoundException(login);

		return account.getUserResponseDto();
	}

	@Override
	public UserResponseDto getUser(String login) {
		return getUserAccount(login).getUserResponseDto();
	}

	@Override
	public UserResponseDto updateUser(String login, UserUpdateDto data) {
		String fName = data.getFirstName();
		String lName = data.getLastName();
		if(fName == null || lName == null)
			throw new RuntimeException(); //FIXME throw proper exception 
		
		Query query = new Query(Criteria.where(UserAccount.Fields.EMAIL).is(login));
		Update update = new Update().set(UserAccount.Fields.F_NAME, fName).set(UserAccount.Fields.L_NAME, lName);
		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(false);
		UserAccount account = template.findAndModify(query, update, options, UserAccount.class);
		if(account == null)
			throw new UserNotFoundException(login);
		return account.getUserResponseDto();
	}

	@Override
	public boolean updatePassword(String login, String newPassword) {
		UserAccount account = getUserAccount(login);
		if (!isValidPassword(newPassword, account))
			throw new PasswordNotValidException(newPassword);
		LinkedList<String> lastHash = account.getLastHash();
		if (lastHash.size() == n_last_hash)
			lastHash.removeFirst();
		lastHash.add(account.getHash());
		account.setHash(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
		account.setActivationDate(LocalDateTime.now());
		template.save(account);
		return true;
	}

	@Override
	public boolean revokeAccount(String login) {
		UserAccount account = getUserAccount(login);
		if (account.isRevoked()) {
			return false;// FIXME throw exception?
		}
		account.setRevoked(true);
		template.save(account);
		return true;
	}

	@Override
	public boolean activateAccount(String login) {
		UserAccount account = getUserAccount(login);
		if (!account.isRevoked()) {
			return false; // FIXME throw exception?
		}
		account.setRevoked(false);
		account.setActivationDate(LocalDateTime.now());
		template.save(account);
		return true;
	}

	@Override
	public RolesResponseDto addRole(String login, String role) { // FIXME let know account already got role?
		UserAccount account = getUserAccount(login);
		HashSet<String> roles = account.getRoles();
		if (roles.add(role))
			template.save(account);
		return account.getRolesResponseDto();
	}

	@Override
	public RolesResponseDto removeRole(String login, String role) { // FIXME let know account don't have role?
		UserAccount account = getUserAccount(login);
		HashSet<String> roles = account.getRoles();
		if (roles.remove(role))
			template.save(account);
		return account.getRolesResponseDto();
	}

	@Override
	public String getPasswordHash(String login) {
		UserAccount account = getUserAccount(login);
		return account.isRevoked() ? null : account.getHash();
	}

	@Override
	public LocalDateTime getActivationDate(String login) {
		UserAccount account = getUserAccount(login);
		return account.isRevoked() ? null : account.getActivationDate();
	}

	@Override
	public RolesResponseDto getRoles(String login) {
		UserAccount account = getUserAccount(login);
		return account.isRevoked() ? null : account.getRolesResponseDto();
	}

	@Override
	public void run(String... args) throws Exception { //FIXME 
		Query query = new Query(Criteria.where(UserAccount.Fields.EMAIL).is("admin"));
		if(!template.exists(query, UserAccount.class)) {
			UserAccount admin = new UserAccount("admin", BCrypt.hashpw("admin", BCrypt.gensalt()),"","");
			admin.setRoles(new HashSet<String>(List.of("ADMIN")));
			template.save(admin);
		}
	}

}
