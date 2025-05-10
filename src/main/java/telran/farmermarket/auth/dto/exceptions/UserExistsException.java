package telran.farmermarket.auth.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException{
	
	public UserExistsException(String login) {
		super(String.format("User %s is already exists", login));
	}
}
