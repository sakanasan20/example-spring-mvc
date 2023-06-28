package tw.niq.example.mapper;

import org.mapstruct.Mapper;

import tw.niq.example.entity.Beer;
import tw.niq.example.model.BeerDto;

@Mapper
public interface BeerMapper {

	Beer beerDtoToBeer(BeerDto beerDto);
	
	BeerDto beerToBeerDto(Beer beer);
	
}
