package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import tw.niq.example.model.BeerDto;

public interface BeerService {
	
	Collection<BeerDto> listBeers();

	Optional<BeerDto> getBeerById(UUID beerId);

	BeerDto createBeer(BeerDto beer);

	void updateBeerById(UUID beerId, BeerDto beer);
	
	void patchBeerById(UUID beerId, BeerDto beer);

	void deleteBeerById(UUID beerId);
	
}
