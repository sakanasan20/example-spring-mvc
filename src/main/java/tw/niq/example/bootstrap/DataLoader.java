package tw.niq.example.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.Beer;
import tw.niq.example.entity.Customer;
import tw.niq.example.model.BeerCsvRecord;
import tw.niq.example.model.BeerStyle;
import tw.niq.example.repository.BeerRepository;
import tw.niq.example.repository.CustomerRepository;
import tw.niq.example.service.BeerCsvService;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;
	private final BeerCsvService beerCsvService;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		loadBeers();
		loadCsvBeers();
		loadCustomers();
	}

	private void loadCsvBeers() throws FileNotFoundException {
		if (beerRepository.count() < 10) {
			
			File file = ResourceUtils.getFile("classpath:csv/beers.csv");
			
			List<BeerCsvRecord> beerCsvRecords = beerCsvService.convertCsv(file);
			
			beerCsvRecords.forEach(beerCsvRecord -> {
				BeerStyle beerStyle = switch(beerCsvRecord.getStyle()) {
					case "American Pale Lager" -> BeerStyle.LAGER;
					case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
						BeerStyle.ALE;
					case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
					case "American Porter" -> BeerStyle.PORTER;
					case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
					case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
					case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
					case "English Pale Ale" -> BeerStyle.PALE_ALE;
					default -> BeerStyle.PILSNER;
				};
				
				beerRepository.save(Beer.builder()
						.beerName(StringUtils.abbreviate(beerCsvRecord.getBeer(), 50))
						.beerStyle(beerStyle)
						.upc(beerCsvRecord.getRow().toString())
						.price(BigDecimal.TEN)
						.quantityOnHand(beerCsvRecord.getCount_x())
						.build());
			});
		}
	}

	private void loadCustomers() {
		
		Customer customer1 = Customer.builder().name("Customer 1").build();
		Customer customer2 = Customer.builder().name("Customer 2").build();
		Customer customer3 = Customer.builder().name("Customer 3").build();
		
		customerRepository.save(customer1);
		customerRepository.save(customer2);
		customerRepository.save(customer3);
	}

	private void loadBeers() {
		
		if (beerRepository.count() == 0) {
			Beer beer1 = Beer.builder()
					.beerName("Galaxy Cat")
					.beerStyle(BeerStyle.PALE_ALE)
					.upc("12356")
					.price(new BigDecimal("12.99"))
					.quantityOnHand(122)
					.build();

			Beer beer2 = Beer.builder()
					.beerName("Crank")
					.beerStyle(BeerStyle.PALE_ALE)
					.upc("12356222")
					.price(new BigDecimal("11.99"))
					.quantityOnHand(392)
					.build();

			Beer beer3 = Beer.builder()
					.beerName("Sunshine City")
					.beerStyle(BeerStyle.IPA)
					.upc("12356")
					.price(new BigDecimal("13.99"))
					.quantityOnHand(144)
					.build();
			
			beerRepository.save(beer1);
			beerRepository.save(beer2);
			beerRepository.save(beer3);	
		}
	}

}
