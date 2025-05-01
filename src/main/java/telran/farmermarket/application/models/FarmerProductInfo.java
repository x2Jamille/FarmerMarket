package telran.farmermarket.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FarmerProductInfo {
	private String productId;
	private String productName;
	private int quantity;
}
