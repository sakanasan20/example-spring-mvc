package tw.niq.example.client;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import tw.niq.example.model.BeerDto;

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
	
	

}
