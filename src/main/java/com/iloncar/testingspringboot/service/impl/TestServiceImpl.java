package com.iloncar.testingspringboot.service.impl;

import com.iloncar.testingspringboot.dao.api.PersonDao;
import com.iloncar.testingspringboot.exception.InternalServerErrorException;
import com.iloncar.testingspringboot.exception.ResourceNotFoundException;
import com.iloncar.testingspringboot.domain.Person;
import com.iloncar.testingspringboot.model.PersonModel;
import com.iloncar.testingspringboot.repository.PersonRepository;
import com.iloncar.testingspringboot.service.api.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
  PersonDao personDao;
  PersonRepository personRepository;

  public TestServiceImpl(PersonDao personDao, PersonRepository personRepository) {
    this.personDao = personDao;
    this.personRepository = personRepository;
  }
  @Override
  public long calculateFibonacci(long n) {
    if (n <= 1) return n;
    return calculateFibonacci(n - 1) + calculateFibonacci(n - 2);
    }

  @Override
  public PersonModel createGetDeletePersonTestCase(PersonModel person)
      throws InternalServerErrorException, ResourceNotFoundException {
    long i = personDao.insertPerson(person);
    person = personDao.getPersonById(i);
    personDao.deletePersonById(person.getId());
    return person;
  }

  @Override public Person createGetDeletePersonORMTestCase(Person person) throws ResourceNotFoundException {
    Long personId = personDao.insertPersonORM(person);
    person = personDao.getPersonByIdORM(personId);
    personDao.deletePersonByIdORM(personId);
    return person;
  }
}
