package tw.niq.example.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
public class BeerOrder {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@Version
	private Long version;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime lastModifiedDate;
	
	private String customerRef;
	
	@ManyToOne
	private Customer customer;
	
	@Builder.Default
	@OneToMany(mappedBy = "beerOrder")
	private Set<BeerOrderLine> beerOrderLines = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.PERSIST)
	private BeerOrderShipment beerOrderShipment;
	
	public Boolean isNew() {
		return this.id == null;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
		customer.getBeerOrders().add(this);
	}
	
	public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
		this.beerOrderShipment = beerOrderShipment;
		beerOrderShipment.setBeerOrder(this);
	}

	public BeerOrder(UUID id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate,
			String customerRef, Customer customer, Set<BeerOrderLine> beerOrderLines, BeerOrderShipment beerOrderShipment) {
		this.id = id;
		this.version = version;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.customerRef = customerRef;
		this.setCustomer(customer);
		this.beerOrderLines = beerOrderLines;
		this.setBeerOrderShipment(beerOrderShipment);
	}
	
}
