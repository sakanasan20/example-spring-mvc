package tw.niq.example.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import tw.niq.example.entity.Beer;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;
import tw.niq.example.repository.BeerRepository;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;
	
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 25;
	
	@Override
	public Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
		
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
		
		Page<Beer> beerPage;
		
		if (StringUtils.hasText(beerName) && beerStyle == null) {
			beerPage = listBeersByName(beerName, pageRequest);
		} else if (!StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = listBeersByStyle(beerStyle, pageRequest);
		} else if (StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
		} else {
			beerPage = beerRepository.findAll(pageRequest);
		}
		
		if (showInventory != null && !showInventory) {
			beerPage.forEach(beer -> beer.setQuantityOnHand(null));
		}
		
		return beerPage.map(beerMapper::beerToBeerDto);
	}
	
	public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
		
		int queryPageNumber;
		int queryPageSize;
		
		if (pageNumber != null && pageNumber > 0) {
			queryPageNumber = pageNumber - 1;
		} else {
			queryPageNumber = DEFAULT_PAGE;
		}
		
		if (pageSize != null) {
			if (pageSize > 1000) {
				queryPageSize = DEFAULT_PAGE_SIZE;
			} else {
				queryPageSize = pageSize;
			}
			
		} else {
			queryPageSize = DEFAULT_PAGE_SIZE;
		}
		
		Sort sort = Sort.by(Sort.Order.asc("beerName"));
		
		return PageRequest.of(queryPageNumber, queryPageSize, sort);
	}
	
	Page<Beer> listBeersByName(String beerName, Pageable pageable) {
		return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
	}
	
	Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
		return beerRepository.findAllByBeerStyle(beerStyle, pageable);
	}
	
	Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
		return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
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
