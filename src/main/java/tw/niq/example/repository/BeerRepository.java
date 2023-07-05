package tw.niq.example.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.niq.example.entity.Beer;
import tw.niq.example.model.BeerStyle;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

	Collection<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
	
	Collection<Beer> findAllByBeerStyle(BeerStyle beerStyle);
	
	Collection<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
	
}
