package com.iloncar.testingspringboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
  @Id
  @SequenceGenerator(
      name = "person_id_seq",
      sequenceName = "person_id_seq",
      allocationSize = 1
  )
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
  private long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "year_of_birth", nullable = false)
  private int yearOfBirth;

}
