package telran.farmermarket.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FarmerDto {
	private String email;
	private String name;
	private List<ProductInfoDto> products;
}
