package tw.niq.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tw.niq.example.model.BeerStyle;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Beer {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@Version
	private Integer version;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime updateDate;
	
	@NotBlank
	@NotNull
	@Size(max = 50)
	@Column(length = 50)
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
	
	@OneToMany(mappedBy = "beer")
	private Set<BeerOrderLine> beerOrderLines;
	
	@ManyToMany
	@JoinTable(name = "beer_category",
		joinColumns = @JoinColumn(name = "beer_id"), 
		inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories;
	
	public void addCategory(Category category) {
		this.categories.add(category);
		category.getBeers().add(this);
	}
	
	public void removeCategory(Category category) {
		this.categories.remove(category);
		category.getBeers().remove(this);
	}

}
