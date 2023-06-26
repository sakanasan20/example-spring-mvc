package tw.niq.example.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.model.Beer;
import tw.niq.example.service.BeerService;

@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

	private final BeerService beerService;
	
	public Beer getBeerById(UUID id) {
		
		Beer beer = beerService.getBeerById(id);
		
		log.debug(beer.toString());
		
		return beer;
	}
	
}
