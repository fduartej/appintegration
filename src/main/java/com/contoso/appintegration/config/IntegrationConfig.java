package com.contoso.appintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.http.HttpMethod;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public IntegrationFlow httpFlow() {
        return IntegrationFlows.from(Http.inboundGateway("/foo")
        .requestMapping(m -> m.methods(HttpMethod.POST))
        .requestPayloadType(String.class))
        .channel("httpRequest")
        .get();
    }
}
