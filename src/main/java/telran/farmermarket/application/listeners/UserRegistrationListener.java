package telran.farmermarket.application.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.farmermarket.application.dto.ClientDto;
import telran.farmermarket.application.dto.FarmerDto;
import telran.farmermarket.application.service.FarmerMarketService;
import telran.farmermarket.shared.events.UserRegisteredEvent;

import static telran.farmermarket.auth.security.enums.Role.*;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

	final FarmerMarketService fmService;
	
	
	@EventListener
	public void onUserRegistered(UserRegisteredEvent event) {
		String role = event.getRole();
		String email = event.getEmail();
		String name = email.split("@")[0];
		
		
		
		
		if(role.equals(CLIENT.getRole())) {
			ClientDto dto = new ClientDto(email, name, new ArrayList<>());
			fmService.addClient(dto);
		}
		else if(role.equals(FARMER.getRole())) {
			FarmerDto dto = new FarmerDto(email, name, new ArrayList<>());
			fmService.addFarmer(dto);
		}
		
	}
	
	
}
