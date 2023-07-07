package tw.niq.example.client;

import java.util.UUID;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import tw.niq.example.model.BeerDto;
import tw.niq.example.model.BeerDtoPageImpl;
import tw.niq.example.model.BeerStyle;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

	private final RestTemplateBuilder restTemplateBuilder;

	private final String GET_BEER_PATH = "/api/v1/beers";
	private final String GET_BEER_BY_ID_PATH = "/api/v1/beers/{beerId}";
	
	@Override
	public Page<BeerDto> listBeers() {
		return listBeers(null, null, null, null, null);
	}

	@Override
	public Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
			Integer pageSize) {

		RestTemplate restTemplate = restTemplateBuilder.build();

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

		if (beerName != null) {
			uriComponentsBuilder.queryParam("beerName", beerName);
		}

		ResponseEntity<BeerDtoPageImpl> responseEntity = restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
				BeerDtoPageImpl.class);

		return responseEntity.getBody();
	}

	@Override
	public BeerDto getBeerById(UUID beerId) {
		
		RestTemplate restTemplate = restTemplateBuilder.build();
		
		return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDto.class, beerId);
	}

}
