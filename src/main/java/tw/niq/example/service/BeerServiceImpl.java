package tw.niq.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tw.niq.example.model.Beer;
import tw.niq.example.model.BeerStyle;

@Service
public class BeerServiceImpl implements BeerService {

	@Override
	public Beer getBeerById(UUID id) {
		return Beer.builder()
				.id(id)
				.verison(1)
				.beerName("Galaxy Cat")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("123456")
				.price(new BigDecimal("12.99"))
				.quantityOnHand(122)
				.createdDate(LocalDateTime.now())
				.updatedDate(LocalDateTime.now())
				.build();
	}

}
