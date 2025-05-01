package telran.farmermarket.application.service;

import java.util.List;

import telran.farmermarket.application.dto.*;
import telran.farmermarket.application.models.*;

public interface IFarmerMarketService {

	ClientDto addClient(ClientDto clientDto);

	FarmerDto addFarmer(FarmerDto farmerDto);

	ProductDto addProduct(ProductDto productDto);

	FarmerDto addProductToFarmer(String farmerName, String productName, int quantity);

	FarmerDto sellProductToClient(String clientEmail, String farmerName, String productName, int quantity);

	List<ProductInfoDto> getFarmerStock(String farmerName);

	List<FarmerDto> getAllFarmers();

	List<ClientDto> getAllClients();

	List<ProductDto> getAllProducts();
	
	FarmerDto removeFarmer(String farmerName);
	
	ClientDto removeClient(String clientEmail);
}
