package tw.niq.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
	@Size(max = 50)
	private String beerName;
	
	@NotNull
	private BeerStyle beerStyle;
	
	@NotBlank
	@NotNull
	@Size(max = 255)
	private String upc;
	
	private Integer quantityOnHand;
	
	@NotNull
	private BigDecimal price;

}
