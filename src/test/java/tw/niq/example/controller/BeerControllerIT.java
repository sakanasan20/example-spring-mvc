package tw.niq.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import tw.niq.example.entity.Beer;
import tw.niq.example.exception.NotFoundException;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.model.BeerDto;
import tw.niq.example.repository.BeerRepository;

@SpringBootTest
class BeerControllerIT {
	
	@Autowired
	BeerController beerController;
	
	@Autowired
	BeerRepository beerRepository;
	
	@Autowired
	BeerMapper beerMapper;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListBeers_whenBeersIsNotEmpty_returnBeers() {
		
		Collection<BeerDto> beerDtos = beerController.listBeers();
		
		assertThat(beerDtos.size()).isEqualTo(3);
	}
	
	@Rollback
	@Transactional
	@Test
	void testListBeers_whenBeersIsEmpty_returnEmptyList() {
		
		beerRepository.deleteAll();
		
		Collection<BeerDto> beerDtos = beerController.listBeers();
		
		assertThat(beerDtos.size()).isEqualTo(0);
	}

	@Test
	void testGetBeerById_whenBeerIdIsValid_returnBeer() {
		
		Beer beer = beerRepository.findAll().get(0);
		
		BeerDto beerDto = beerController.getBeerById(beer.getId());
		
		assertThat(beerDto).isNotNull();
	}
	
	@Test
	void testGetBeerById_whenBeerIdIsInvalid_throwNotFoundException() {
		
		assertThrows(NotFoundException.class, () -> {
			beerController.getBeerById(UUID.randomUUID());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testCreateBeer_whenBeerIsValid_returnResponseEntity() {
		
		BeerDto beerDto = BeerDto.builder()
				.beerName("Test Beer")
				.build();
		
		ResponseEntity<Void> responseEntity = beerController.createBeer(beerDto);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
		
		String location = responseEntity.getHeaders().getLocation().getPath();
		
		PathPatternParser pathPatternParser = new PathPatternParser();
		
		PathMatchInfo pathMatchInfo = pathPatternParser.parse(BeerController.BEER_PATH_ID)
				.matchAndExtract(PathContainer.parsePath(location));
		
		UUID beerId = UUID.fromString(pathMatchInfo.getUriVariables().get("beerId"));
		
		Beer beer = beerRepository.findById(beerId).get();
		
		assertThat(beer).isNotNull();
	}

	@Rollback
	@Transactional
	@Test
	void testUpdateBeerById_whenBeerIdIsValid_returnResponseEntity() {
		
		Beer beer = beerRepository.findAll().get(0);
		
		BeerDto beerDto = beerMapper.beerToBeerDto(beer);
		
		beerDto.setId(null);
		beerDto.setVersion(null);
		beerDto.setCreatedDate(null);
		beerDto.setUpdateDate(null);
		final String beerName = "Test Beer Updated";
		beerDto.setBeerName(beerName);
		
		ResponseEntity<Void> responseEntity = beerController.updateBeerById(beer.getId(), beerDto);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Beer beerUpdated = beerRepository.findById(beer.getId()).get();
		
		assertThat(beerUpdated.getBeerName()).isEqualTo(beerName);
	}
	
	@Test
	void testUpdateBeerById_whenBeerIdNotFound_throwNotFoundException() {
		
		assertThrows(NotFoundException.class, () -> {
			beerController.updateBeerById(UUID.randomUUID(), BeerDto.builder().build());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testPatchBeerById() {
		
		Beer beer = beerRepository.findAll().get(0);
		
		BeerDto beerDto = beerMapper.beerToBeerDto(beer);
		
		beerDto.setId(null);
		beerDto.setVersion(null);
		beerDto.setCreatedDate(null);
		beerDto.setUpdateDate(null);
		final String beerName = "Test Beer Updated";
		beerDto.setBeerName(beerName);
		
		ResponseEntity<Void> responseEntity = beerController.patchBeerById(beer.getId(), beerDto);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Beer beerPatched = beerRepository.findById(beer.getId()).get();
		
		assertThat(beerPatched.getBeerName()).isEqualTo(beerName);
	}

	@Rollback
	@Transactional
	@Test
	void testDeleteBeerById_whenBeerIdFound_returnStatusNoContent() {
		
		Beer beer = beerRepository.findAll().get(0);
		
		ResponseEntity<Void> responseEntity = beerController.deleteBeerById(beer.getId());
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		assertThat(beerRepository.findById(beer.getId())).isEmpty();
	}
	
	@Test
	void testDeleteBeerById_whenBeerIdNotFound_throwNotFoundException() {
		
		assertThrows(NotFoundException.class, () -> {
			beerController.deleteBeerById(UUID.randomUUID());
		});
	}

}
