package tw.niq.example.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withAccepted;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.example.config.RestTemplateBuilderConfig;
import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerDtoPageImpl;
import tw.niq.example.model.BeerStyle;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)	// use custom RestTemplateBuilder configuration
class BeerClientImplMockTest {
	
	static final String URL = "http://localhost:8888";

	// don't auto-wired, customize in setUp()
	BeerClient beerClient;
	
	// don't auto-wired, customize in setUp()
    MockRestServiceServer mockRestServiceServer;
    
    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;	// auto-wired RestTemplateBuilder with custom configuration
	
	@Autowired
    ObjectMapper objectMapper;
	
	@Mock
	RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());
	
	BeerDto beerDto;
	
	String beerDtoJson;
	
	@BeforeEach
	void setUp() throws Exception {
		
		// create customize RestTemplate
		RestTemplate restTemplate =  restTemplateBuilderConfigured.build();
		
		// bind MockRestServiceServer with customize RestTemplate
		mockRestServiceServer = mockRestServiceServer.bindTo(restTemplate).build();
		
		when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
		
		// create BeerClient with mocked RestTemplateBuilder
		beerClient = new BeerClientImpl(mockRestTemplateBuilder);
		
		beerDto = getBeetDto();
		
		beerDtoJson = objectMapper.writeValueAsString(beerDto);
	}

	@Test
	void testListBeers() throws JsonProcessingException {
		
		String beerDtosPageJson = objectMapper.writeValueAsString(getPage());
		
		mockRestServiceServer.expect(method(HttpMethod.GET))
			.andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
			.andRespond(withSuccess(beerDtosPageJson, MediaType.APPLICATION_JSON));
		
		Page<BeerDto> beersPage = beerClient.listBeers();
		
		assertThat(beersPage.getContent().size()).isGreaterThan(0);
	}
	
	@Test
	void testListBeersWithQueryParam() throws JsonProcessingException {
		
		String beerDtosPageJson = objectMapper.writeValueAsString(getPage());
		
		URI uri = UriComponentsBuilder.fromHttpUrl(URL + BeerClientImpl.GET_BEER_PATH)
				.queryParam("beerName", "ALE")
				.build().toUri();
		
		mockRestServiceServer.expect(method(HttpMethod.GET))
			.andExpect(requestTo(uri))
			.andExpect(queryParam("beerName", "ALE"))
			.andRespond(withSuccess(beerDtosPageJson, MediaType.APPLICATION_JSON));
		
		Page<BeerDto> beersPage = beerClient.listBeers("ALE", null, null, null, null);
		
		assertThat(beersPage.getContent().size()).isEqualTo(1);
	}
	
	@Test
	void testGetBeerById() {
		
		mockGetOperation();
		
		BeerDto beerGot = beerClient.getBeerById(beerDto.getId());
		
		assertThat(beerGot.getId()).isEqualTo(beerDto.getId());
	}
	
	@Test
	void testCreateBeer() {
		
		URI uri = UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH)
				.build(beerDto.getId());
		
		mockRestServiceServer.expect(method(HttpMethod.POST))
			.andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
			.andRespond(withAccepted().location(uri));
		
		mockGetOperation();
		
		BeerDto beerCreated = beerClient.createBeer(beerDto);
		
		assertThat(beerCreated.getId()).isEqualTo(beerDto.getId());
	}
	
	@Test
	void testUpdateBeer() {
		
		mockRestServiceServer.expect(method(HttpMethod.PUT))
			.andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
			.andRespond(withNoContent());
		
		mockGetOperation();
		
		BeerDto beerUpdated = beerClient.updateBeer(beerDto);
		
		assertThat(beerUpdated.getId()).isEqualTo(beerDto.getId());
	}
	
	@Test
	void testDeleteBeer() {
		
		mockRestServiceServer.expect(method(HttpMethod.DELETE))
			.andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
			.andRespond(withNoContent());

		beerClient.deleteBeer(beerDto.getId());
		
		mockRestServiceServer.verify();
	}
	
	@Test
	void testDeleteBeerNotFound() {
		
		mockRestServiceServer.expect(method(HttpMethod.DELETE))
			.andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
			.andRespond(withResourceNotFound());

		assertThrows(HttpClientErrorException.class, () -> {
			beerClient.deleteBeer(beerDto.getId());
		});
		
		mockRestServiceServer.verify();
	}
	
	BeerDto getBeetDto() {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
	}
	
	BeerDtoPageImpl getPage() {
		return new BeerDtoPageImpl(Arrays.asList(beerDto), 1, 25, 1);
	}
	
	void mockGetOperation() {
		mockRestServiceServer.expect(method(HttpMethod.GET))
			.andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
			.andRespond(withSuccess(beerDtoJson, MediaType.APPLICATION_JSON));
	}

}
