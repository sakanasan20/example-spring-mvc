package tw.niq.example.service;

import java.util.UUID;

import tw.niq.example.model.Beer;

public interface BeerService {

	Beer getBeerById(UUID id);
	
}
