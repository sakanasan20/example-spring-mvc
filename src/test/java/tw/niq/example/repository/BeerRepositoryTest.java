package tw.niq.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;
import tw.niq.example.entity.Beer;
import tw.niq.example.model.BeerStyle;

@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveBeer_withValidBeer_returnSavedBeer() {
		
		Beer beerSaved = beerRepository.saveAndFlush(Beer.builder()
				.beerName("Test Beer")
				.beerStyle(BeerStyle.ALE)
				.upc("Test UPC")
				.price(new BigDecimal("12.34"))
				.build());
				
		assertThat(beerSaved).isNotNull();
		
		assertThat(beerSaved.getId()).isNotNull();
	}
	
	@Test
	void testSaveBeer_withBeerNameTooLong_throwConstraintViolationException() {
		
		assertThrows(ConstraintViolationException.class, () -> {
			
			beerRepository.saveAndFlush(Beer.builder()
					.beerName("012345678901234567890123456789012345678901234567890")
					.beerStyle(BeerStyle.ALE)
					.upc("Test UPC")
					.price(new BigDecimal("12.34"))
					.build());
		});
	}

}
