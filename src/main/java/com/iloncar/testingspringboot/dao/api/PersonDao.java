package com.iloncar.testingspringboot.dao.api;

import com.iloncar.testingspringboot.domain.Person;
import com.iloncar.testingspringboot.exception.InternalServerErrorException;
import com.iloncar.testingspringboot.exception.ResourceNotFoundException;
import com.iloncar.testingspringboot.model.PersonModel;

public interface PersonDao {

  long insertPerson(PersonModel person) throws InternalServerErrorException;

  PersonModel getPersonById(long id) throws ResourceNotFoundException, InternalServerErrorException;

  void deletePersonById(long id) throws InternalServerErrorException;

  long insertPersonORM(Person person);

  Person getPersonByIdORM(long id) throws ResourceNotFoundException;

  void deletePersonByIdORM(long id);
}
