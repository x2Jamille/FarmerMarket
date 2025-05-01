package telran.farmermarket.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductInfoDto {
	private String productId;
	private String productName;
	private int quantity;
}
