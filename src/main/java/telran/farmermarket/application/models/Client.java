package telran.farmermarket.application.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import telran.farmermarket.application.dto.ClientDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "email")
@Document(collection = "clients")
public class Client {

	@Id
	private String id;
	@Indexed(unique = true)
	private String email;
	private String name;

	public static Client of(ClientDto dto) {
		return new Client(null, dto.getEmail(), dto.getName());
	}

	public ClientDto build() {
		return ClientDto.builder().email(email).name(name).build();
	}
}
