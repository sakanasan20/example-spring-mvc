package tw.niq.example.controller;

import java.util.Collection;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.model.Beer;
import tw.niq.example.service.BeerService;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
	
	public static final String BEER_PATH = "/api/v1/beers";
	
	public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

	private final BeerService beerService;
	
	@GetMapping(BEER_PATH)
	public Collection<Beer> listBeers() {
		
		Collection<Beer> beers = beerService.listBeers();
		
		log.debug(beers.toString());
		
		return beers;
	}
	
	@GetMapping(BEER_PATH_ID)
	public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
		
		Beer beer = beerService.getBeerById(beerId);
		
		log.debug(beer.toString());
		
		return beer;
	}
	
	@PostMapping(BEER_PATH)
	public ResponseEntity<Void> createBeer(@RequestBody Beer beer) {
		
		Beer beerCreated = beerService.createBeer(beer);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.add(HttpHeaders.LOCATION, "/api/v1/beers/" + beerCreated.getId().toString());
		
		return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
	}
	
	@PutMapping(BEER_PATH_ID)
	public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		
		beerService.updateBeerById(beerId, beer);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@PatchMapping(BEER_PATH_ID)
	public ResponseEntity<Void> patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		
		beerService.patchBeerById(beerId, beer);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping(BEER_PATH_ID)
	public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID beerId) {
		
		beerService.deleteBeerById(beerId);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
