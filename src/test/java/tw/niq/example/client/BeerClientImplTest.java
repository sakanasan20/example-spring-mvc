package tw.niq.example.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;

@SpringBootTest
class BeerClientImplTest {

	@Autowired
	BeerClient beerClient;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListBeers() {
		
		Page<BeerDto> beersPage = beerClient.listBeers();
		
		assertThat(beersPage).isNotNull();
	}
	
	@Test
	void testListBeersByBeerName() {
		
		Page<BeerDto> beersPage = beerClient.listBeers("ALE", null, null, null, null);
		
		assertThat(beersPage).isNotNull();
	}
	
	@Test
	void testGetBeerById() {
		
		BeerDto beer = beerClient.getBeerById(beerClient.listBeers().getContent().get(0).getId());
		
		assertThat(beer).isNotNull();
	}
	
	@Test
	void testCreateBeer() {
		
		BeerDto beer = BeerDto.builder()
				.price(new BigDecimal(10.99))
				.beerName("Mango Bobs")
				.beerStyle(BeerStyle.IPA)
				.quantityOnHand(500)
				.upc("12345")
				.build();
		
		BeerDto beerCreated = beerClient.createBeer(beer);
		
		assertThat(beerCreated).isNotNull();
	}
	
	@Test
	void testUpdateBeer() {
		
		BeerDto beer = BeerDto.builder()
				.price(new BigDecimal(10.99))
				.beerName("Mango Bobs 2")
				.beerStyle(BeerStyle.IPA)
				.quantityOnHand(500)
				.upc("12345")
				.build();
		
		BeerDto beerCreated = beerClient.createBeer(beer);
		
		final String newName = "Mango Bobs 3";
		
		beerCreated.setBeerName(newName);
		
		BeerDto beerUpdated = beerClient.updateBeer(beerCreated);
		
		assertEquals(newName, beerUpdated.getBeerName());
	}
	
	@Test
	void testDeleteBeer() {
		
		BeerDto beer = BeerDto.builder()
				.price(new BigDecimal(10.99))
				.beerName("Mango Bobs 2")
				.beerStyle(BeerStyle.IPA)
				.quantityOnHand(500)
				.upc("12345")
				.build();
		
		BeerDto beerCreated = beerClient.createBeer(beer);
		
		beerClient.deleteBeer(beerCreated.getId());
		
		assertThrows(HttpClientErrorException.class, () -> {
			beerClient.getBeerById(beerCreated.getId());
		});
	}

}
