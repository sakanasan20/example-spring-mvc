package tw.niq.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.example.config.SpringSecurityConfig;
import tw.niq.example.model.BeerDto;
import tw.niq.example.service.BeerService;
import tw.niq.example.service.BeerServiceMap;

@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	BeerService beerService;
	
	BeerServiceMap beerServiceImpl = new BeerServiceMap();
	
	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;
	
	@Captor
	ArgumentCaptor<BeerDto> beerArgumentCaptor;
	
	public static final String USERNAME = "admin";
	
	public static final String PASSWORD = "adminpass";

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListBeers() throws Exception {
		
		given(beerService.listBeers(any(), any() , any(), any(), any()))
			.willReturn(beerServiceImpl.listBeers(null, null, false, null, null));
		
		mockMvc.perform(get(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.content.length()", is(3)));
	}
	
	@Test
	void testGetBeerById() throws Exception {
		
		BeerDto testBeer = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(testBeer));
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
	}
	
	@Test
	void testGetBeerById_WhenBeerIdNotFound() throws Exception {
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testCreateBeer() throws Exception {
	
		BeerDto testBeerCreated = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		BeerDto testBeerToCreate = BeerDto.builder()
				.beerName(testBeerCreated.getBeerName())
				.beerStyle(testBeerCreated.getBeerStyle())
				.price(testBeerCreated.getPrice())
				.quantityOnHand(testBeerCreated.getQuantityOnHand())
				.upc(testBeerCreated.getUpc())
				.build();

		String testBeerToCreateJson = objectMapper.writeValueAsString(testBeerToCreate);
		
		given(beerService.createBeer(any(BeerDto.class))).willReturn(testBeerCreated);

		mockMvc.perform(post(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerToCreateJson)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isCreated())
			.andExpect(header().exists(HttpHeaders.LOCATION));
	}
	
	@Test
	void testCreateBeer_whenBeerIsNotValid_returnBadRequest() throws Exception {
		
		BeerDto beerDto = BeerDto.builder().build();
		
		String beerDtoJson = objectMapper.writeValueAsString(beerDto);
		
		given(beerService.createBeer(any(BeerDto.class))).willReturn(beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get());
		
		MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(beerDtoJson)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()", is(6)))
			.andReturn();
		
		System.out.println(mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	void testUpdateBeerById() throws Exception {
	
		BeerDto testBeer = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		String testBeerJson = objectMapper.writeValueAsString(testBeer);
		
		given(beerService.updateBeerById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(testBeer));

		mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerJson)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isNoContent());
		
		verify(beerService).updateBeerById(any(UUID.class), any(BeerDto.class));
	}
	
	@Test
	void testUpdateBeerById_whenBeerIsNotValid_returnBadRequest() throws Exception {
	
		BeerDto testBeer = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		testBeer.setBeerName("012345678901234567890123456789012345678901234567890123456789");
		
		String testBeerJson = objectMapper.writeValueAsString(testBeer);
		
		given(beerService.updateBeerById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(testBeer));

		MvcResult mvcResult = mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerJson)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.length()", is(1)))
			.andReturn();
		
		System.out.println("testUpdateBeerById_whenBeerIsNotValid_returnBadRequest:" + mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	void testPatchBeerById() throws Exception {
	
		BeerDto testBeer = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		Map<String, Object> testBeerToPatch = new HashMap<>();
		
		testBeerToPatch.put("beerName", "New Name");
		
		String testBeerToPatchJson = objectMapper.writeValueAsString(testBeerToPatch);

		mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerToPatchJson)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isNoContent());
		
		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		
		assertThat(testBeerToPatch.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
	}
	
	@Test
	void testDeleteBeerById() throws Exception {
		
		BeerDto testBeer = beerServiceImpl.listBeers(null, null, null, 1, 25).stream().findFirst().get();
		
		given(beerService.deleteBeerById(any(UUID.class))).willReturn(true);
		
		mockMvc.perform(delete(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.with(httpBasic(USERNAME, PASSWORD)))
			.andExpect(status().isNoContent());
	
		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
