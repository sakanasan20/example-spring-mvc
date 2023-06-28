package tw.niq.example.mapper;

import org.mapstruct.Mapper;

import tw.niq.example.entity.Customer;
import tw.niq.example.model.CustomerDto;

@Mapper
public interface CustomerMapper {
	
	Customer customerDtoTocustomer(CustomerDto customerDto);
	
	CustomerDto customerTocustomerDto(Customer customer);

}
