package tw.niq.example.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.example.exception.NotFoundException;
import tw.niq.example.model.CustomerDto;
import tw.niq.example.service.CustomerService;

@RequiredArgsConstructor
@RestController
public class CustomerController {

	public static final String CUSTOMER_PATH = "/api/v1/customer";

	public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

	private final CustomerService customerService;

	@GetMapping(CUSTOMER_PATH)
	public List<CustomerDto> listAllCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping(CUSTOMER_PATH_ID)
	public CustomerDto getCustomerById(@PathVariable("customerId") UUID id) {
		return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
	}

	@PostMapping(CUSTOMER_PATH)
	public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto customer) {

		CustomerDto savedCustomer = customerService.createCustomer(customer);

		HttpHeaders headers = new HttpHeaders();

		headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId().toString());

		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@PutMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<Void> updateCustomerByID(@PathVariable("customerId") UUID customerId,
			@RequestBody CustomerDto customer) {

		customerService.updateCustomerById(customerId, customer);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<Void> patchCustomerById(@PathVariable("customerId") UUID customerId,
			@RequestBody CustomerDto customer) {

		customerService.patchCustomerById(customerId, customer);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID customerId) {

		customerService.deleteCustomerById(customerId);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
