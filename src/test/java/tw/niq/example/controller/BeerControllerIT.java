package tw.niq.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.example.entity.Beer;
import tw.niq.example.exception.NotFoundException;
import tw.niq.example.mapper.BeerMapper;
import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerStyle;
import tw.niq.example.repository.BeerRepository;

@SpringBootTest
class BeerControllerIT {
	
	@Autowired
	BeerController beerController;
	
	@Autowired
	BeerRepository beerRepository;
	
	@Autowired
	BeerMapper beerMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void testListBeers_whenBeersIsNotEmpty_returnBeers() {
		
		Page<BeerDto> beerDtos = beerController.listBeers(null, null, null, 1, 25);
		
		assertThat(beerDtos.getContent().size()).isGreaterThan(0);
	}
	
	@Rollback
	@Transactional
	@Test
	void testListBeers_whenBeersIsEmpty_returnEmptyList() {
		
		beerRepository.deleteAll();
		
		Page<BeerDto> beerDtos = beerController.listBeers(null, null, null, 1, 25);
		
		assertThat(beerDtos.getContent().size()).isEqualTo(0);
	}
	
	@Test
	void testListBeersByName() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerName", "IPA")
				.queryParam("pageSize", "400")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(336)));
	}
	
	@Test
	void testListBeersByStyle_withNoAuth() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("pageSize", "600"))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testListBeersByStyle() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("pageSize", "600")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(548)));
	}
	
	@Test
	void testListBeersByNameAndStyle() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("pageSize", "400")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)));
	}
	
	@Test
	void testListBeersByNameAndStyleAndShowInventoryFalse() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "false")
				.queryParam("pageSize", "400")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
	}
	
	@Test
	void testListBeersByNameAndStyleAndShowInventoryTrue() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "true")
				.queryParam("pageSize", "400")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(310)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
	}
	
	@Test
	void testListBeersByNameAndStyleAndShowInventoryTruePage2() throws Exception {
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.queryParam("beerName", "IPA")
				.queryParam("beerStyle", BeerStyle.IPA.name())
				.queryParam("showInventory", "true")
				.queryParam("pageNumber", "2")
				.queryParam("pageSize", "50")
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()", is(50)))
			.andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
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
	void testPatchBeerById_withBadBeerName() throws Exception {
		
		Beer beer = beerRepository.findAll().get(0);
		
		BeerDto beerDto = beerMapper.beerToBeerDto(beer);
		
		beerDto.setId(null);
		beerDto.setVersion(null);
		beerDto.setCreatedDate(null);
		beerDto.setUpdateDate(null);
		final String beerName = "012345678901234567890123456789012345678901234567890";
		beerDto.setBeerName(beerName);
		
		String beerDtoJson = objectMapper.writeValueAsString(beerDto);

		MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8))
				.content(beerDtoJson)
				.with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD)))
			.andExpect(status().isBadRequest())
			.andReturn();
		
		System.out.println(mvcResult.getResponse().getContentAsString());
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
