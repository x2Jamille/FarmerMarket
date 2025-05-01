package telran.farmermarket.application.models;

import java.util.ArrayList;
import java.util.List;
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
	private List<FarmerProductInfo> stock;

	public static Farmer of(FarmerDto dto) {
		List<FarmerProductInfo> stock = dto.getProducts() == null ? new ArrayList<>() : dto.getProducts().stream()
				.map(fp -> new FarmerProductInfo(fp.getProductId(), fp.getProductName(), fp.getQuantity())).collect(Collectors.toList());
		return new Farmer(null, dto.getName(), stock);
	}

	public FarmerDto build() {
		List<FarmerProductDto> prods = stock.stream().map(fp -> new FarmerProductDto(fp.getProductId(), fp.getProductName(), fp.getQuantity())).collect(Collectors.toList());
		return FarmerDto.builder().name(name).products(prods).build();
	}
}
