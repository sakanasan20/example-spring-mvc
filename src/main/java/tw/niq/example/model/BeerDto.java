package tw.niq.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerDto {

	private UUID id;
	
	private Integer version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
	private String beerName;
	
	private BeerStyle beerStyle;
	
	private String upc;
	
	private Integer quantityOnHand;
	
	private BigDecimal price;

}
