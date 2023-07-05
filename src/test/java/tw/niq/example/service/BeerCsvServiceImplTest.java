package tw.niq.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import tw.niq.example.model.BeerCsvRecord;

class BeerCsvServiceImplTest {
	
	BeerCsvService beerCsvService;

	@BeforeEach
	void setUp() throws Exception {
		beerCsvService = new BeerCsvServiceImpl();
	}

	@Test
	void testConvertCsv() throws FileNotFoundException {
		
		File file = ResourceUtils.getFile("classpath:csv/beers.csv");
		
		List<BeerCsvRecord> beerCsvRecords = beerCsvService.convertCsv(file);
		
		assertThat(beerCsvRecords.size()).isGreaterThan(0);
	}

}
