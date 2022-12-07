package com.example.pact.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}

	@Configuration
	static class Config {

		@Bean
		Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
			return builder -> builder
					.mixIn(CustomerId.class, MixIns.CustomerIdMixIn.class)
					.mixIn(Name.class, MixIns.FullNameMixIn.class);
		}
	}
}
