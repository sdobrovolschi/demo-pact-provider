package com.example.pact.provider;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.v4.V4InteractionType;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.TestTarget;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.VerificationReports;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.NamedThreadLocal;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Nullable;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/application-test.properties")
@Provider("provider")
@PactBroker(enablePendingPacts = "true", providerTags = "master")
@IgnoreNoPactsToVerify
@VerificationReports(value = {"console", "json"}, reportDir = "target/pact/reports")
class ContractVerificationTest {

    @LocalServerPort
    int port;

    public static final ThreadLocal<RabbitOperations> RABBIT_HOLDER = new NamedThreadLocal<>("Rabbit");

    @BeforeEach
    void before(@Nullable PactVerificationContext context,
                @Autowired(required = false) List<StateHandler> stateHandlers,
                @Autowired RabbitOperations rabbit) {

        if (context != null && stateHandlers != null) {
            context.withStateChangeHandlers(stateHandlers.toArray())
                    .setTarget(testTarget(context.getInteraction()));
        }

        RABBIT_HOLDER.set(rabbit);
    }

    TestTarget testTarget(Interaction interaction) {
        if (interaction.asV4Interaction().isInteractionType(V4InteractionType.AsynchronousMessages)) {
            return new MessageTestTarget(List.of("com.example.pact.provider"));
        }

        if (interaction.asV4Interaction().isInteractionType(V4InteractionType.SynchronousHTTP)) {
            return new HttpTestTarget("localhost", port, "/");
        }
        return null;
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void verification(@Nullable PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }
}
