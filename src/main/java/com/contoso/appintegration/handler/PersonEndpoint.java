package com.contoso.appintegration.handler;

import org.springframework.stereotype.Component;

import com.contoso.appintegration.model.Person;
import com.contoso.appintegration.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;


@Component
public class PersonEndpoint {
	private static final String STATUSCODE_HEADER = "http_statusCode";
	
	@Autowired
	private PersonService service;
	
	public Message<?> getAll(Message<String> msg) {
		return MessageBuilder.withPayload(service.getAllPerson())
				.copyHeadersIfAbsent(msg.getHeaders())
				.setHeader(STATUSCODE_HEADER, HttpStatus.OK)
				.build();
	}

	public Message<?> get(Message<String> msg) {
		long id = Long.valueOf(msg.getPayload());
		Person person = service.getPerson(id);
		
		if (person == null) {
			return MessageBuilder.fromMessage(msg)
					.copyHeadersIfAbsent(msg.getHeaders())
					.setHeader(STATUSCODE_HEADER, HttpStatus.NOT_FOUND)
					.build(); 
		}
		
		return MessageBuilder.withPayload(person)
				.copyHeadersIfAbsent(msg.getHeaders())
				.setHeader(STATUSCODE_HEADER, HttpStatus.OK)
				.build();
	}
	
	public void put(Message<Person> msg) {
		service.updatePerson(msg.getPayload());
	}
	
	public void post(Message<Person> msg) {
		service.insertPerson(msg.getPayload());
	}
	
	public void delete(Message<String> msg) {
		long id = Long.valueOf(msg.getPayload());
		service.deletePerson(id);
	}
}