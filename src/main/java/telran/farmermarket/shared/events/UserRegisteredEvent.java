package telran.farmermarket.shared.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRegisteredEvent {
	private final String email;
	private final String role;
}
