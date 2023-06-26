package tw.niq.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Beer {

	private UUID id;
	
	private Integer verison;
	
	private String beerName;
	
	private BeerStyle beerStyle;
	
	private String upc;
	
	private Integer quantityOnHand;
	
	private BigDecimal price;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;
	
}
