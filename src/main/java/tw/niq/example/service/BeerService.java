package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;

public interface BeerService {
	
	Collection<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

	Optional<BeerDto> getBeerById(UUID beerId);

	BeerDto createBeer(BeerDto beer);

	Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer);
	
	Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer);

	Boolean deleteBeerById(UUID beerId);
	
}
