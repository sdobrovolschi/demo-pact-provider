package com.example.pact.provider;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.pact.provider.ContractVerificationTest.RABBIT_HOLDER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.entry;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class CustomerCreatedVerification {

    private final RabbitOperations rabbit;

    public CustomerCreatedVerification() {
        rabbit = RABBIT_HOLDER.get();
    }

    @PactVerifyProvider("a customer created event is received")
    public MessageAndMetadata verifyCustomerCreated() {
        var message = await("event").atMost(5, SECONDS)
                .until(() -> rabbit.receive("messages.handling"), Objects::nonNull);

        var props = new HashMap<String, Object>();
        props.put("type", message.getMessageProperties().getType());
        props.put("contentType", message.getMessageProperties().getContentType());

        return new MessageAndMetadata(message.getBody(), props);
    }
}
