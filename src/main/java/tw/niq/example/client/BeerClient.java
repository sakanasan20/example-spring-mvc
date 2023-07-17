package tw.niq.example.client;

import java.util.UUID;

import org.springframework.data.domain.Page;

import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;

public interface BeerClient {
	
	Page<BeerDto> listBeers();

	Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
			Integer pageSize);

	BeerDto getBeerById(UUID beerId);

	BeerDto createBeer(BeerDto beer);

	BeerDto updateBeer(BeerDto beer);

	void deleteBeer(UUID beerId);

}
