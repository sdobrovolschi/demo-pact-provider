package com.example.pact.provider;

public record CustomerCreated(String customerId, String name) implements Event {
}
