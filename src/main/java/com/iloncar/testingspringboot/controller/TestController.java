package com.iloncar.testingspringboot.controller;

import com.iloncar.testingspringboot.domain.Person;
import com.iloncar.testingspringboot.model.PersonModel;
import com.iloncar.testingspringboot.payload.CalculateFibonacciResponse;
import com.iloncar.testingspringboot.payload.GreetingRequest;
import com.iloncar.testingspringboot.payload.GreetingResponse;
import com.iloncar.testingspringboot.payload.PersonRequest;
import com.iloncar.testingspringboot.payload.PersonResponse;
import com.iloncar.testingspringboot.service.api.TestService;
import com.iloncar.testingspringboot.utils.AppConstants;
import com.iloncar.testingspringboot.utils.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  private TestService testService;
  private Mapper mapper;

  public TestController(TestService testService, Mapper mapper) {
    this.testService = testService;
    this.mapper = mapper;
  }

  @GetMapping("/case1")
  public ResponseEntity<GreetingResponse> hello() {
    return  ResponseEntity.ok(new GreetingResponse(AppConstants.HELLO));
  }

  @PostMapping("/case2")
  ResponseEntity<GreetingResponse> greeting(@RequestBody GreetingRequest request) {
    return ResponseEntity.ok(new GreetingResponse(AppConstants.HELLO + " " + request.getName()));
  }

  @GetMapping("/case3/{number}")
  ResponseEntity<CalculateFibonacciResponse> calculateFibonacci(@PathVariable(name = "number") long number){
    try{
      return ResponseEntity.ok(new CalculateFibonacciResponse(testService.calculateFibonacci(number)));
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @PostMapping("/case4")
  ResponseEntity<PersonResponse> createGetDeletePersonTestCase(@RequestBody PersonRequest personRequest) {
    try {
      PersonModel personModel = mapper.mapPersonRequestToPersonModel(personRequest);
      personModel = testService.createGetDeletePersonTestCase(personModel);
      PersonResponse personResponse = mapper.mapPersonModelToPersonResponse(personModel);
      return ResponseEntity.ok(personResponse);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/case5")
  ResponseEntity<PersonResponse> createGetDeletePersonORMTestCase(@RequestBody PersonRequest personRequest) {
    try {
      Person person = mapper.mapPersonRequestToPerson(personRequest);
      person = testService.createGetDeletePersonORMTestCase(person);
      PersonResponse personResponse = mapper.mapPersonToPersonResponse(person);
      return ResponseEntity.ok(personResponse);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
