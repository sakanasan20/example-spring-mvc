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
import tw.niq.example.entity.BeerOrder;
import tw.niq.example.entity.BeerOrderShipment;
import tw.niq.example.entity.Customer;
import tw.niq.example.service.BeerCsvServiceImpl;

@Import({DataLoader.class, BeerCsvServiceImpl.class})
@DataJpaTest
class BeerOrderRepositoryTest {
	
	@Autowired
	BeerOrderRepository beerOrderRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BeerRepository beerRepository;
	
	Customer testCustomer;
	Beer testBeer;

	@BeforeEach
	void setUp() throws Exception {
		testCustomer = customerRepository.findAll().get(0);
		testBeer = beerRepository.findAll().get(0);
	}

	@Transactional
	@Test
	void testBeerOrder() {
		
		final String customerRef = "Test Customer Ref";
		final String trackingNumber = "Test Tracking Number";
		
		BeerOrder beerOrder = BeerOrder.builder()
				.customerRef(customerRef)
				.customer(testCustomer)
				.beerOrderShipment(BeerOrderShipment.builder()
						.trackingNumber(trackingNumber)
						.build())
				.build();
		
		BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

		assertThat(savedBeerOrder).isNotNull();
		assertThat(savedBeerOrder.getCustomerRef()).isEqualTo(customerRef);
		assertThat(savedBeerOrder.getCustomer().getId()).isEqualTo(testCustomer.getId());
		assertThat(testCustomer.getBeerOrders()).isNotEmpty();
		assertThat(savedBeerOrder.getBeerOrderShipment()).isNotNull();
		assertThat(savedBeerOrder.getBeerOrderShipment().getTrackingNumber()).isEqualTo(trackingNumber);
	}

}
