package tw.niq.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import tw.niq.example.bootstrap.DataLoader;
import tw.niq.example.entity.Beer;
import tw.niq.example.entity.Category;
import tw.niq.example.service.BeerCsvServiceImpl;

@Import({DataLoader.class, BeerCsvServiceImpl.class})
@DataJpaTest
class CategoryRepositoryTest {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	BeerRepository beerRepository;
	
	Beer testBeer;

	@BeforeEach
	void setUp() throws Exception {
		testBeer = beerRepository.findAll().get(0);
	}

	@Transactional
	@Test
	void testCategory() {
		
		final String description = "Test description";
		
		Category savedCategory = categoryRepository.save(Category.builder()
				.description(description)
				.build());
		
		testBeer.addCategory(savedCategory);
		
		beerRepository.save(testBeer);
		
		assertThat(savedCategory).isNotNull();
		assertThat(savedCategory.getDescription()).isEqualTo(description);
		assertThat(savedCategory.getBeers()).isNotEmpty();
		assertThat(testBeer.getCategories()).isNotEmpty();
	}

}
