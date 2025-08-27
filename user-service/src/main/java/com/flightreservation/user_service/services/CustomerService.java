package com.flightreservation.user_service.services;

import com.flightreservation.user_service.DTO.customer.CustomerCreateRequest;
import com.flightreservation.user_service.DTO.customer.CustomerResponse;
import com.flightreservation.user_service.DTO.customer.CustomerUpdateRequest;
import com.flightreservation.user_service.entities.Customer;
import com.flightreservation.user_service.exception.BusinessException;
import com.flightreservation.user_service.exception.ResourceNotFoundException;
import com.flightreservation.user_service.mapper.CustomerMapper;
import com.flightreservation.user_service.repositories.CustomerRepository;
import com.flightreservation.user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService{

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder; 
 //   private final VerificationService verificationService;
    

    public List<CustomerResponse> getAllCustomers() {
        log.info("Fetching all customers");
        List<CustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} customers", customers.size());
        return customers;
    }

    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id: " + id);
                });
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        log.info("Creating new customer with email: {}", request.getEmail());
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Email already in use: {}", request.getEmail());
            throw new BusinessException("Email already in use");
        }

        if (request.getName() == null || request.getName().isBlank() ||
            request.getSurname() == null || request.getSurname().isBlank()) {
            log.warn("Name or surname missing for email: {}", request.getEmail());
            throw new BusinessException("Name and surname must be provided");
        }

        Customer customer = customerMapper.toEntity(request);
        // Şifreyi hashle
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer saved = customerRepository.save(customer);
        log.info("Customer created with id: {}", saved.getId());
      //  verificationService.generateVerificationToken(saved);
       // verificationService.sendVerificationEmail(saved);
        return customerMapper.toResponse(saved);
    }

    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        log.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id: " + id);
                });

        if (request.getName() == null || request.getName().isBlank() ||
            request.getSurname() == null || request.getSurname().isBlank()) {
            log.warn("Name or surname missing for customer id: {}", id);
            throw new BusinessException("Name and surname must be provided");
        }

        customerMapper.updateCustomerFromRequest(request, customer);
        Customer updated = customerRepository.save(customer);
        log.info("Customer updated with id: {}", updated.getId());
        return customerMapper.toResponse(updated);
    }

    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer not found with id: " + id);
                });
        customerRepository.delete(customer);
        log.info("Customer deleted with id: {}", id);
    }
}
