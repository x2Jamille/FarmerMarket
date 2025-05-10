package telran.farmermarket.application.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.farmermarket.application.dto.*;
import telran.farmermarket.application.models.*;
import telran.farmermarket.application.service.IFarmerMarketService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fmarket")
public class FarmerMarketController {

	final IFarmerMarketService fmService;

	// TODO Validation

//	//FIXME add roles by registration
//	@PostMapping("/client/add")
//	public ClientDto addClient(@RequestBody ClientDto client) {
//		// TODO Auto-generated method stub
//		return fmService.addClient(client);
//	}
//	//FIXME add roles by registration
//	@PostMapping("/farmer/add")
//	public FarmerDto addFarmer(@RequestBody FarmerDto farmer) {
//		// TODO Auto-generated method stub
//		return fmService.addFarmer(farmer);
//	}

	@PostMapping("/product/add")
	public ProductDto addProduct(@RequestBody ProductDto product) {
		// TODO Auto-generated method stub
		return fmService.addProduct(product);
	}

	@PatchMapping("/farmer/product/add")
	public FarmerDto addProductToFarmer(@RequestParam String productName, @RequestParam int quantity,
			Principal principal) {
		// TODO Auto-generated method stub
		return fmService.addProductToFarmer(productName, quantity, principal);
	}

	@PatchMapping("/farmer/product/sell")
	public ClientDto sellProductToClient(@RequestParam String farmerName,
			@RequestParam String productName, @RequestParam int quantity, Principal principal) {
		// TODO Auto-generated method stub
		return fmService.buyProductFromFarmer(farmerName, productName, quantity, principal);
	}

	@GetMapping("/farmer/products")
	public List<ProductInfoDto> getFarmerStock(@RequestParam String farmerName) {
		// TODO Auto-generated method stub
		return fmService.getFarmerStock(farmerName);
	}

	@GetMapping("/farmers")
	public List<FarmerDto> getAllFarmers() {
		return fmService.getAllFarmers();
	}

	@GetMapping("/clients")
	public List<ClientDto> getAllClients() {
		return fmService.getAllClients();
	}

	@GetMapping("/products")
	public List<ProductDto> getAllProducts() {
		return fmService.getAllProducts();
	}

	// FIXME relocate remove logic to auth package and remove by email?
	@DeleteMapping("/farmer/remove")
	public FarmerDto removeFarmer(@RequestParam String farmerName) {
		return fmService.removeFarmer(farmerName);
	}

	@DeleteMapping("/client/remove")
	public ClientDto removeClient(@RequestParam String clientEmail) {
		return fmService.removeClient(clientEmail);
	}

}
