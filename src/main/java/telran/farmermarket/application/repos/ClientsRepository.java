package telran.farmermarket.application.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import telran.farmermarket.application.models.Client;

@Repository
public interface ClientsRepository extends MongoRepository<Client, String>{

	Client findByEmail(String email);

}
