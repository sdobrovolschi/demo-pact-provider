package com.example.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.VerificationReports;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
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

    @BeforeEach
    void before(@Nullable PactVerificationContext context, @LocalServerPort int port,
                @Autowired(required = false) List<StateHandler> stateHandlers) {

        if (context != null && stateHandlers != null) {
            context.withStateChangeHandlers(stateHandlers.toArray())
                    .setTarget(new HttpTestTarget("localhost", port, "/"));
        }
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void verification(@Nullable PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }
}
