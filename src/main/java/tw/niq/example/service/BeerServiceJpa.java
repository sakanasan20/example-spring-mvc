package tw.niq.example.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
		return beerRepository.findAll()
				.stream()
				.map(beerMapper::beerToBeerDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BeerDto> getBeerById(UUID beerId) {
		return Optional.ofNullable(
				beerMapper.beerToBeerDto(
						beerRepository.findById(beerId).orElse(null)));
	}

	@Override
	public BeerDto createBeer(BeerDto beerDto) {

		return beerMapper.beerToBeerDto(
				beerRepository.save(
						beerMapper.beerDtoToBeer(beerDto)));
	}

	@Override
	public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beerDto) {
		
		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		
		beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
			
			foundBeer.setBeerName(beerDto.getBeerName());
			foundBeer.setBeerStyle(beerDto.getBeerStyle());
			foundBeer.setPrice(beerDto.getPrice());
			foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
			foundBeer.setUpc(beerDto.getUpc());
			
			atomicReference.set(
					Optional.of(
							beerMapper.beerToBeerDto(
									beerRepository.save(foundBeer))));
			
		}, () -> {
			
			atomicReference.set(Optional.empty());
		});
		
		return atomicReference.get();
	}

	@Override
	public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer) {

		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		
		beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
			
			if (StringUtils.hasText(beer.getBeerName())) {
				foundBeer.setBeerName(beer.getBeerName());
			}
			
			if (beer.getBeerStyle() != null) {
				foundBeer.setBeerStyle(beer.getBeerStyle());
			}
			
			if (StringUtils.hasText(beer.getUpc()))	{
				foundBeer.setUpc(beer.getUpc());
			}
			
			if (beer.getPrice()!= null)	{
				foundBeer.setPrice(beer.getPrice());
			}
			
			if (beer.getQuantityOnHand()!= null) {
				foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
			}
			
			atomicReference.set(
					Optional.of(
							beerMapper.beerToBeerDto(
									beerRepository.saveAndFlush(foundBeer))));
			
		}, () -> {
			atomicReference.set(Optional.empty());
		});
		
		return atomicReference.get();
	}

	@Override
	public Boolean deleteBeerById(UUID beerId) {
		
		if (beerRepository.existsById(beerId)) {
			beerRepository.deleteById(beerId);
			return true;
		}
		
		return false;
	}

}
