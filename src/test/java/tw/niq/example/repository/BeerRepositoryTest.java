package tw.niq.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolationException;
import tw.niq.example.entity.Beer;
import tw.niq.example.model.BeerStyle;

@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Rollback
	@Transactional
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
	
	@Test
	void testGetBeerByName() {
		
		Collection<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
		
		assertThat(beers.size()).isEqualTo(336);		
	}
	
	@Test
	void testGetBeerByStyle() {
		
		Collection<Beer> beers = beerRepository.findAllByBeerStyle(BeerStyle.IPA);
		
		assertThat(beers.size()).isEqualTo(548);		
	}
	
	@Test
	void testGetBeerByNameAndStyle() {
		
		Collection<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%IPA%", BeerStyle.IPA);
		
		assertThat(beers.size()).isEqualTo(310);		
	}

}
