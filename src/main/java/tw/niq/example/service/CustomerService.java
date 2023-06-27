package tw.niq.example.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tw.niq.example.model.CustomerDto;

public interface CustomerService {

	List<CustomerDto> getAllCustomers();

	Optional<CustomerDto> getCustomerById(UUID id);

	CustomerDto createCustomer(CustomerDto customer);

	void updateCustomerById(UUID customerId, CustomerDto customer);

	void patchCustomerById(UUID customerId, CustomerDto customer);

	void deleteCustomerById(UUID customerId);

}
