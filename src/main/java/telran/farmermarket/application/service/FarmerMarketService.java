package telran.farmermarket.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.farmermarket.application.dto.*;
import telran.farmermarket.application.models.*;
import telran.farmermarket.application.repos.*;

@Service
@RequiredArgsConstructor
public class FarmerMarketService implements IFarmerMarketService {

	final ClientsRepository clientsRepo;
	final FarmersRepository farmersRepo;
	final ProductsRepository productsRepo;

	private Farmer getFarmerByName(String name) {
		Farmer farmer = farmersRepo.findByName(name);
		if (farmer == null)
			throw new RuntimeException("FARMER_NOT_EXISTS");
		return farmer;
	}

	private Client getClientByEmail(String email) {
		Client client = clientsRepo.findByEmail(email);
		if (client == null)
			throw new RuntimeException("CLIENT_NOT_EXISTS");
		return client;
	}

	private Product getProductByName(String name) {
		Product product = productsRepo.findByName(name);
		if (product == null)
			throw new RuntimeException("PRODUCT_NOT_EXISTS");
		return product;
	}

	@Override
	public ClientDto addClient(ClientDto clientDto) {
		Client client = clientsRepo.findByEmail(clientDto.getEmail());
		if (client != null)
			throw new RuntimeException("ADD_CLIENT - EXISTS");
		return clientsRepo.save(Client.of(clientDto)).build();
	}

	@Override
	public FarmerDto addFarmer(FarmerDto farmerDto) {
		Farmer farmer = farmersRepo.findByName(farmerDto.getName());
		if (farmer != null)
			throw new RuntimeException("ADD_FARMER - EXISTS");
		return farmersRepo.save(Farmer.of(farmerDto)).build();
	}

	@Override
	public ProductDto addProduct(ProductDto productDto) {
		Product product = productsRepo.findByName(productDto.getName());
		if (product != null)
			throw new RuntimeException("ADD_PRODUCT - EXISTS");
		return productsRepo.save(Product.of(productDto)).build();
	}

	@Override
	public FarmerDto addProductToFarmer(String farmerName, String productName, int quantity) {
		Farmer farmer = getFarmerByName(farmerName);
		Product product = getProductByName(productName);

		farmer.addProduct(new ProductInfo(product.getId(), product.getName(), quantity));

		return farmersRepo.save(farmer).build();
	}

	@Override
	public FarmerDto sellProductToClient(String clientEmail, String farmerName, String productName, int quantity) {
		Client client = getClientByEmail(clientEmail);
		Farmer farmer = getFarmerByName(farmerName);
		Product product = getProductByName(productName);

		ProductInfo productInStock = farmer.getStock().stream().filter(fp -> fp.getProductId().equals(product.getId()))
				.findFirst().orElseThrow(() -> new RuntimeException("SELL_PRODUCT_TO_CLIENT - PRODUCT_NOT_IN_STOCK"));

		if (productInStock.getQuantity() < quantity)
			throw new RuntimeException("SELL_PRODUCT_TO_CLIENT - NOT_ENOUGH_IN_STOCK");

		productInStock.setQuantity(productInStock.getQuantity() - quantity);

		client.addProduct(new ProductInfo(product.getId(), product.getName(), quantity));
		clientsRepo.save(client);

		if (productInStock.getQuantity() == 0)
			farmer.getStock().remove(productInStock);

		return farmersRepo.save(farmer).build();
	}

	@Override
	public List<ProductInfoDto> getFarmerStock(String farmerName) {
		Farmer farmer = getFarmerByName(farmerName);
		return farmer.getStock().stream().map(fp -> new ProductInfoDto()).collect(Collectors.toList());
	}

	@Override
	public List<FarmerDto> getAllFarmers() {
		return farmersRepo.findAll().stream().map(Farmer::build).collect(Collectors.toList());
	}

	@Override
	public List<ClientDto> getAllClients() {
		return clientsRepo.findAll().stream().map(Client::build).collect(Collectors.toList());
	}

	@Override
	public List<ProductDto> getAllProducts() {
		return productsRepo.findAll().stream().map(Product::build).collect(Collectors.toList());
	}

	@Override
	public FarmerDto removeFarmer(String farmerName) {
		Farmer farmer = getFarmerByName(farmerName);
		farmersRepo.delete(farmer);
		return farmer.build();
	}

	@Override
	public ClientDto removeClient(String clientEmail) {
		Client client = getClientByEmail(clientEmail);
		clientsRepo.delete(client);
		return client.build();
	}

}
