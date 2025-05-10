package telran.farmermarket.application.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.farmermarket.application.models.Product;


public interface ProductsRepository extends MongoRepository<Product, String>{

	Product findByName(String name);

}
