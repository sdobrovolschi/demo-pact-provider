package com.example.pact.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/customers")
@RequiredArgsConstructor
final class CustomerResource {

    private final FindCustomerUseCase findCustomerUseCase;

    @GetMapping(path = "/{customerId}", produces = APPLICATION_JSON_VALUE)
    Optional<Customer> find(@PathVariable("customerId") String customerId) {
        return findCustomerUseCase.find(customerId);
    }
}
