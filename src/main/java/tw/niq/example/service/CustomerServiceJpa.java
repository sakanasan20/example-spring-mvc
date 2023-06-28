package tw.niq.example.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.niq.example.mapper.CustomerMapper;
import tw.niq.example.model.CustomerDto;
import tw.niq.example.repository.CustomerRepository;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {
	
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	@Override
	public List<CustomerDto> getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CustomerDto> getCustomerById(UUID id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public CustomerDto createCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCustomerById(UUID customerId, CustomerDto customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void patchCustomerById(UUID customerId, CustomerDto customer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCustomerById(UUID customerId) {
		// TODO Auto-generated method stub

	}

}
