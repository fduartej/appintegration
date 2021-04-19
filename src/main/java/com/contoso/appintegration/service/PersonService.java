package com.contoso.appintegration.service;

import org.springframework.stereotype.Service;
import java.util.Map;

import com.contoso.appintegration.model.Person;

import java.util.HashMap;


@Service
public class PersonService {
    
    private final Map<Long, Person> persons = new HashMap<Long, Person>();
	
	public PersonService() {
		persons.put(1l, new Person(1, "John", 25));
		persons.put(2l, new Person(2, "Steve", 19));
		persons.put(3l, new Person(3, "Mike", 38));
		persons.put(4l, new Person(4, "Julia", 41));
	}

	public Map<Long, Person> getAllPerson() {
		return persons;
	}

	public Person getPerson(long id) {
		return persons.get(id);
	}

	public void updatePerson(Person person) {
		if (persons.get(person.getId()) != null) {
			persons.put(person.getId(), person);
		}
	}

	public void insertPerson(Person person) {
		if (persons.get(person.getId()) == null) {
			persons.put(person.getId(), person);
		}
	}

	public void deletePerson(long id) {
		persons.remove(id);
	}
}
