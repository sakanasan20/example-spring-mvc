package tw.niq.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
	
	private UUID id;
	
	private Integer version;

	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
	private String name;

}
