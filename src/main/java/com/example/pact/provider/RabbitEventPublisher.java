package com.example.pact.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitMessageOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitEventPublisher {

    private final RabbitMessageOperations rabbit;

    void publish(Event event) {
        rabbit.convertAndSend("messages", "", event);
    }
}
