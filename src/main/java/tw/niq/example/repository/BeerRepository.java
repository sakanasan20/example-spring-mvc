package tw.niq.example.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.niq.example.entity.Beer;
import tw.niq.example.model.BeerStyle;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

	Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);
	
	Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);
	
	Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
	
}
