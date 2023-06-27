package tw.niq.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.example.model.BeerDto;
import tw.niq.example.service.BeerService;
import tw.niq.example.service.BeerServiceImpl;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	BeerService beerService;
	
	BeerServiceImpl beerServiceimpl = new BeerServiceImpl();
	
	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;
	
	@Captor
	ArgumentCaptor<BeerDto> beerArgumentCaptor;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListBeers() throws Exception {
		
		Collection<BeerDto> testBeers = beerServiceimpl.listBeers();
		
		given(beerService.listBeers()).willReturn(testBeers);
		
		mockMvc.perform(get(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(testBeers.size())));
	}
	
	@Test
	void testGetBeerById() throws Exception {
		
		BeerDto testBeer = beerServiceimpl.listBeers().stream().findFirst().get();
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(testBeer));
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
	}
	
	@Test
	void testGetBeerById_WhenBeerIdNotFound() throws Exception {
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void testCreateBeer() throws Exception {
	
		BeerDto testBeerCreated = beerServiceimpl.listBeers().stream().findFirst().get();
		
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
				.content(testBeerToCreateJson))
			.andExpect(status().isCreated())
			.andExpect(header().exists(HttpHeaders.LOCATION));
	}
	
	@Test
	void testUpdateBeerById() throws Exception {
	
		BeerDto testBeer = beerServiceimpl.listBeers().stream().findFirst().get();
		
		String testBeerJson = objectMapper.writeValueAsString(testBeer);

		mockMvc.perform(put(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerJson))
			.andExpect(status().isNoContent());
		
		verify(beerService).updateBeerById(any(UUID.class), any(BeerDto.class));
	}
	
	@Test
	void testPatchBeerById() throws Exception {
	
		BeerDto testBeer = beerServiceimpl.listBeers().stream().findFirst().get();
		
		Map<String, Object> testBeerToPatch = new HashMap<>();
		
		testBeerToPatch.put("beerName", "New Name");
		
		String testBeerToPatchJson = objectMapper.writeValueAsString(testBeerToPatch);

		mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(testBeerToPatchJson))
			.andExpect(status().isNoContent());
		
		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		
		assertThat(testBeerToPatch.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
	}
	
	@Test
	void testDeleteBeerById() throws Exception {
		
		BeerDto testBeer = beerServiceimpl.listBeers().stream().findFirst().get();
		
		mockMvc.perform(delete(BeerController.BEER_PATH_ID, testBeer.getId()).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	
		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
