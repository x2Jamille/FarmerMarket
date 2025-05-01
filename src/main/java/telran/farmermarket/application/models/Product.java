package telran.farmermarket.application.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import telran.farmermarket.application.dto.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Document(collection = "products")
public class Product {
	@Id
	private String id;
	@Indexed(unique = true)
	private String name;

	public Product(String name) {
		this.name = name;
	}

	public static Product of(ProductDto dto) {
		return new Product(dto.getName());
	}

	public ProductDto build() {
		return ProductDto.builder().name(name).build();
	}
}
