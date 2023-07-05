package tw.niq.example.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.Beer;
import tw.niq.example.entity.Customer;
import tw.niq.example.model.BeerStyle;
import tw.niq.example.repository.BeerRepository;
import tw.niq.example.repository.CustomerRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;

//	@Transactional
	@Override
	public void run(String... args) throws Exception {
		loadBeers();
		loadCustomers();
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
