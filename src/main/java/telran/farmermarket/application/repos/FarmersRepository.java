package telran.farmermarket.application.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.farmermarket.application.models.Farmer;
import telran.farmermarket.application.models.ProductInfo;


public interface FarmersRepository extends MongoRepository<Farmer, String>{

	Farmer findByName(String name);

	Farmer findByEmail(String email);
	
}
