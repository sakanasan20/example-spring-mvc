package tw.niq.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerDto {

	private UUID id;
	
	private Integer version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
	@NotBlank
	@NotNull
	private String beerName;
	
	@NotNull
	private BeerStyle beerStyle;
	
	@NotBlank
	@NotNull
	private String upc;
	
	private Integer quantityOnHand;
	
	@NotNull
	private BigDecimal price;

}
