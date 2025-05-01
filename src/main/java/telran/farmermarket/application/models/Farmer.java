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
import telran.farmermarket.application.dto.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Document(collection = "farmers")
public class Farmer {

	@Id
	private String id;
	@Indexed(unique = true)
	private String name;
	private List<ProductInfo> stock;

	public static Farmer of(FarmerDto dto) {
		List<ProductInfo> stock = dto.getProducts() == null ? new ArrayList<>() : dto.getProducts().stream()
				.map(pi -> new ProductInfo(pi.getProductId(), pi.getProductName(), pi.getQuantity())).collect(Collectors.toList());
		return new Farmer(null, dto.getName(), stock);
	}

	public FarmerDto build() {
		List<ProductInfoDto> prods = stock.stream().map(pi -> new ProductInfoDto(pi.getProductId(), pi.getProductName(), pi.getQuantity())).collect(Collectors.toList());
		return FarmerDto.builder().name(name).products(prods).build();
	}
	
	public void addProduct(ProductInfo newProduct) {
		Optional<ProductInfo> product = stock.stream()
				.filter(pi -> pi.getProductId().equals(newProduct.getProductId())).findFirst();
		if(product.isPresent()) 
			product.get().setQuantity(product.get().getQuantity() + newProduct.getQuantity());
		else
			stock.add(newProduct);
	}
}
