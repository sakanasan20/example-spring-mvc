package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import tw.niq.example.model.BeerDto;

public interface BeerService {
	
	Collection<BeerDto> listBeers();

	Optional<BeerDto> getBeerById(UUID beerId);

	BeerDto createBeer(BeerDto beer);

	Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer);
	
	Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer);

	Boolean deleteBeerById(UUID beerId);
	
}
