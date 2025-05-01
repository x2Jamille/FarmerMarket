package telran.farmermarket.application.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import telran.farmermarket.application.dto.ClientDto;
import telran.farmermarket.application.dto.ProductInfoDto;

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
	private List<ProductInfo> products;

	public static Client of(ClientDto dto) {
		List<ProductInfo> products = dto.getProducts() == null ? new ArrayList<>()
				: dto.getProducts().stream()
						.map(pi -> new ProductInfo(pi.getProductId(), pi.getProductName(), pi.getQuantity()))
						.collect(Collectors.toList());
		return new Client(null, dto.getEmail(), dto.getName(), products);
	}

	public ClientDto build() {
		List<ProductInfoDto> prods = products.stream()
				.map(pi -> new ProductInfoDto(pi.getProductId(), pi.getProductName(), pi.getQuantity()))
				.collect(Collectors.toList());
		return ClientDto.builder().email(email).name(name).products(prods).build();
	}

	public void addProduct(ProductInfo newProduct) {
		Optional<ProductInfo> product = products.stream()
				.filter(pi -> pi.getProductId().equals(newProduct.getProductId())).findFirst();
		if(product.isPresent()) 
			product.get().setQuantity(product.get().getQuantity() + newProduct.getQuantity());
		else
			products.add(newProduct);
	}
}
