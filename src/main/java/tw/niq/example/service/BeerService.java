package tw.niq.example.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;

public interface BeerService {
	
	Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

	Optional<BeerDto> getBeerById(UUID beerId);

	BeerDto createBeer(BeerDto beer);

	Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer);
	
	Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer);

	Boolean deleteBeerById(UUID beerId);
	
}
