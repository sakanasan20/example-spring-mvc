package tw.niq.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.niq.example.model.Beer;
import tw.niq.example.model.BeerStyle;

@Service
public class BeerServiceImpl implements BeerService {
	
	private Map<UUID, Beer> beerMap;
	
	public BeerServiceImpl() {

		this.beerMap = new HashMap<>();

		Beer beer1 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1).beerName("Galaxy Cat")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12356")
				.price(new BigDecimal("12.99"))
				.quantityOnHand(122)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Beer beer2 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Crank")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12356222")
				.price(new BigDecimal("11.99"))
				.quantityOnHand(392)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Beer beer3 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Sunshine City")
				.beerStyle(BeerStyle.IPA)
				.upc("12356")
				.price(new BigDecimal("13.99"))
				.quantityOnHand(144)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		beerMap.put(beer1.getId(), beer1);
		beerMap.put(beer2.getId(), beer2);
		beerMap.put(beer3.getId(), beer3);
	}

	@Override
	public Collection<Beer> listBeers() {
		return new ArrayList<>(beerMap.values());
	}
	
	@Override
	public Beer getBeerById(UUID beerId) {
		return beerMap.get(beerId);
	}

	@Override
	public Beer createBeer(Beer beer) {

		Beer beerCreated = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName(beer.getBeerName())
				.beerStyle(beer.getBeerStyle())
				.upc(beer.getUpc())
				.price(beer.getPrice())
				.quantityOnHand(beer.getQuantityOnHand())
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
		
		beerMap.put(beerCreated.getId(), beerCreated);
		
		return beerCreated;
	}

	@Override
	public void updateBeerById(UUID beerId, Beer beer) {
		
		Beer beerExisting = beerMap.get(beerId);
		
		beerExisting.setVersion(beerExisting.getVersion() + 1);
		beerExisting.setBeerName(beer.getBeerName());
		beerExisting.setBeerStyle(beer.getBeerStyle());
		beerExisting.setUpc(beer.getUpc());
		beerExisting.setPrice(beer.getPrice());
		beerExisting.setQuantityOnHand(beer.getQuantityOnHand());
		beerExisting.setUpdateDate(LocalDateTime.now());
	}
	
	@Override
	public void patchBeerById(UUID beerId, Beer beer) {
		
		Beer beerExisting = beerMap.get(beerId);
		
		Boolean isPatched = false;
		
		if (StringUtils.hasText(beer.getBeerName())) {
			beerExisting.setBeerName(beer.getBeerName());
			isPatched = true;
		}
		
		if (beer.getBeerStyle() != null) {
			beerExisting.setBeerStyle(beer.getBeerStyle());
			isPatched = true;
		}
		
		if (StringUtils.hasText(beer.getUpc()))	{
			beerExisting.setUpc(beer.getUpc());
			isPatched = true;
		}
		
		if (beer.getPrice()!= null)	{
			beerExisting.setPrice(beer.getPrice());
			isPatched = true;
		}
		
		if (beer.getQuantityOnHand()!= null) {
			beerExisting.setQuantityOnHand(beer.getQuantityOnHand());
			isPatched = true;
		}
		
		if (isPatched) {
			beerExisting.setVersion(beerExisting.getVersion() + 1);
			beerExisting.setUpdateDate(LocalDateTime.now());
		}
	}

	@Override
	public void deleteBeerById(UUID beerId) {
		beerMap.remove(beerId);
	}

}
