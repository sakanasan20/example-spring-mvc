package tw.niq.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tw.niq.example.entity.Beer;

@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveBeer() {
		Beer beerSaved = beerRepository.save(Beer.builder().beerName("Test Beer").build());
		assertThat(beerSaved).isNotNull();
		assertThat(beerSaved.getId()).isNotNull();
	}

}
