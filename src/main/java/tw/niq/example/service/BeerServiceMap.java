package tw.niq.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;

@Service
public class BeerServiceMap implements BeerService {
	
	private Map<UUID, BeerDto> beerMap;
	
	public BeerServiceMap() {

		this.beerMap = new HashMap<>();

		BeerDto beer1 = BeerDto.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Galaxy Cat")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12356")
				.price(new BigDecimal("12.99"))
				.quantityOnHand(122)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		BeerDto beer2 = BeerDto.builder()
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

		BeerDto beer3 = BeerDto.builder()
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
	public Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
		return new PageImpl<>(new ArrayList<>(beerMap.values()));
	}
	
	@Override
	public Optional<BeerDto> getBeerById(UUID beerId) {
		return Optional.of(beerMap.get(beerId));
	}

	@Override
	public BeerDto createBeer(BeerDto beer) {

		BeerDto beerCreated = BeerDto.builder()
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
	public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer) {
		
		BeerDto beerExisting = beerMap.get(beerId);
		
		beerExisting.setVersion(beerExisting.getVersion() + 1);
		beerExisting.setBeerName(beer.getBeerName());
		beerExisting.setBeerStyle(beer.getBeerStyle());
		beerExisting.setUpc(beer.getUpc());
		beerExisting.setPrice(beer.getPrice());
		beerExisting.setQuantityOnHand(beer.getQuantityOnHand());
		beerExisting.setUpdateDate(LocalDateTime.now());
		
		return Optional.of(beerExisting);
	}
	
	@Override
	public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer) {
		
		BeerDto beerExisting = beerMap.get(beerId);
		
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
		
		return Optional.of(beerExisting);
	}

	@Override
	public Boolean deleteBeerById(UUID beerId) {
		beerMap.remove(beerId);
		return true;
	}

}
