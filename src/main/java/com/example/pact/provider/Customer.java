package com.example.pact.provider;

import java.util.Objects;

public final class Customer {

    private final CustomerId customerId;
    private final FullName fullName;

    public Customer(CustomerId customerId, FullName fullName) {
        this.customerId = customerId;
        this.fullName = fullName;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public FullName getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerId);
    }
}
