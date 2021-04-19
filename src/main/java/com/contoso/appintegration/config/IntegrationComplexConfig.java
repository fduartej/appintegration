package com.contoso.appintegration.config;

import org.springframework.context.annotation.Configuration;

import com.contoso.appintegration.model.Person;

import org.springframework.context.annotation.Bean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;

@Configuration
public class IntegrationComplexConfig {

	private static final String HTTP_END_POINT ="/persons";

    @Bean
	public IntegrationFlow httpGetFlow() {
		return IntegrationFlows.from(httpGetGate())
			.channel("httpGetChannel")
			.handle("personEndpoint", "get").get();
	}

	@Bean
	public IntegrationFlow httpGetAllFlow() {
		return IntegrationFlows.from(httpGetAllGate())
			.channel("httpGetAllChannel")
			.handle("personEndpoint", "getAll").get();
	}

	@Bean
	public IntegrationFlow httpPostPutFlow() {
		return IntegrationFlows.from(httpPostPutGate())
			.channel("routeRequest")
			.route("headers.http_requestMethod",
				 m -> m.prefix("http").suffix("Channel")
                 .channelMapping("PUT", "Put")
                 .channelMapping("POST", "Post")
		).get();
	}

	@Bean
	public IntegrationFlow httpPostFlow() {
		return IntegrationFlows.from("httpPostChannel")
			.handle("personEndpoint", "post")
			.get();
	}

	@Bean
	public IntegrationFlow httpPutFlow() {
		return IntegrationFlows.from("httpPutChannel")
		.handle("personEndpoint", "put")
		.get();
	}

	@Bean
	public IntegrationFlow httpDeleteFlow() {
		return IntegrationFlows.from(httpDeleteGate())
		.channel("httpDeleteChannel")
		.handle("personEndpoint", "delete")
		.get();
	}

	@Bean
	public MessagingGatewaySupport httpGetAllGate() {
		HttpRequestHandlingMessagingGateway handler = new HttpRequestHandlingMessagingGateway();
		handler.setRequestMapping(createMapping(new HttpMethod[]{HttpMethod.GET}, HTTP_END_POINT));
		handler.setHeaderMapper(headerMapper());

		return handler;
	}


	@Bean
	public MessagingGatewaySupport httpGetGate() {
		HttpRequestHandlingMessagingGateway handler = new HttpRequestHandlingMessagingGateway();
		handler.setRequestMapping(createMapping(new HttpMethod[]{HttpMethod.GET}, HTTP_END_POINT +"/{personId}"));
		handler.setPayloadExpression(parser().parseExpression("#pathVariables.personId"));
		handler.setHeaderMapper(headerMapper());

		return handler;
	}

	@Bean
	public MessagingGatewaySupport httpPostPutGate() {
		HttpRequestHandlingMessagingGateway handler = new HttpRequestHandlingMessagingGateway();
		handler.setRequestMapping(createMapping(new HttpMethod[]{HttpMethod.PUT, HttpMethod.POST}, HTTP_END_POINT, HTTP_END_POINT+"/{personId}"));
		handler.setStatusCodeExpression(parser().parseExpression("T(org.springframework.http.HttpStatus).NO_CONTENT"));
		handler.setRequestPayloadTypeClass(Person.class);
		handler.setHeaderMapper(headerMapper());

		return handler;
	}

	@Bean
	public MessagingGatewaySupport httpDeleteGate() {
		HttpRequestHandlingMessagingGateway handler = new HttpRequestHandlingMessagingGateway();
		handler.setRequestMapping(createMapping(new HttpMethod[]{HttpMethod.DELETE}, HTTP_END_POINT +"/{personId}"));
		handler.setStatusCodeExpression(parser().parseExpression("T(org.springframework.http.HttpStatus).NO_CONTENT"));
		handler.setPayloadExpression(parser().parseExpression("#pathVariables.personId"));
		handler.setHeaderMapper(headerMapper());

		return handler;
	}

	@Bean
	public ExpressionParser parser() {
		return new SpelExpressionParser();
	}

	@Bean
	public HeaderMapper<HttpHeaders> headerMapper() {
		return new DefaultHttpHeaderMapper();
	}

	private RequestMapping createMapping(HttpMethod[] method, String... path) {
		RequestMapping requestMapping = new RequestMapping();
		requestMapping.setMethods(method);
		requestMapping.setConsumes("application/json");
		requestMapping.setProduces("application/json");
		requestMapping.setPathPatterns(path);

		return requestMapping;
	}
}
