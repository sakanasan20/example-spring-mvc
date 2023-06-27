package tw.niq.example.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerControllerTest {
	
	@Autowired
	BeerController beerController;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void listBeers() {
		beerController.listBeers();
	}
	
	@Test
	void testGetBeerById() {
		beerController.getBeerById(UUID.randomUUID());
	}

}
