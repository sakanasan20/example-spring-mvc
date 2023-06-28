package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.model.BeerDto;
import tw.niq.example.repository.BeerRepository;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;
	
	@Override
	public Collection<BeerDto> listBeers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<BeerDto> getBeerById(UUID beerId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public BeerDto createBeer(BeerDto beer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBeerById(UUID beerId, BeerDto beer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void patchBeerById(UUID beerId, BeerDto beer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBeerById(UUID beerId) {
		// TODO Auto-generated method stub

	}

}
