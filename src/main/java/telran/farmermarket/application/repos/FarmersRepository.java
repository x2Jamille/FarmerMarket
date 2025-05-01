package telran.farmermarket.application.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.farmermarket.application.models.Farmer;

@Repository
public interface FarmersRepository extends MongoRepository<Farmer, String>{

	Farmer findByName(String name);
	
}
