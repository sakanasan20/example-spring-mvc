package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import tw.niq.example.model.Beer;

public interface BeerService {
	
	Collection<Beer> listBeers();

	Optional<Beer> getBeerById(UUID beerId);

	Beer createBeer(Beer beer);

	void updateBeerById(UUID beerId, Beer beer);
	
	void patchBeerById(UUID beerId, Beer beer);

	void deleteBeerById(UUID beerId);
	
}
