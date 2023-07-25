package com.iloncar.testingspringboot.service.api;

import com.iloncar.testingspringboot.exception.InternalServerErrorException;
import com.iloncar.testingspringboot.exception.ResourceNotFoundException;
import com.iloncar.testingspringboot.domain.Person;
import com.iloncar.testingspringboot.model.PersonModel;

public interface TestService {
  long calculateFibonacci(long n);

  PersonModel createGetDeletePersonTestCase(PersonModel person)
      throws InternalServerErrorException, ResourceNotFoundException;

  Person createGetDeletePersonORMTestCase(Person person) throws ResourceNotFoundException;
}
