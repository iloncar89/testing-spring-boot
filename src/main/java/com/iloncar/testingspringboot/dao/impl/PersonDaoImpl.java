package com.iloncar.testingspringboot.dao.impl;

import com.iloncar.testingspringboot.dao.api.PersonDao;
import com.iloncar.testingspringboot.domain.Person;
import com.iloncar.testingspringboot.exception.InternalServerErrorException;
import com.iloncar.testingspringboot.exception.ResourceNotFoundException;
import com.iloncar.testingspringboot.model.PersonModel;
import com.iloncar.testingspringboot.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class PersonDaoImpl extends JdbcDaoSupport implements PersonDao {
  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private PersonRepository personRepository;

  public PersonDaoImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, PersonRepository personRepository) {
    this.dataSource = dataSource;
    this.jdbcTemplate = jdbcTemplate;
    this.personRepository = personRepository;
  }

  @PostConstruct
  private void initialize(){
    setDataSource(dataSource);
  }

  @Override public long insertPerson(final PersonModel personModel) throws InternalServerErrorException {
    try(Connection connection = dataSource.getConnection()) {
      final String insertPersonSql = "INSERT INTO person (first_name, last_name, year_of_birth) VALUES(?,?,?) returning id";
      PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSql, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, personModel.getFirstName());
      preparedStatement.setString(2, personModel.getLastName());
      preparedStatement.setInt(3, personModel.getYearOfBirth());

      int affectedRows = preparedStatement.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Creating user failed, no rows affected.");
      }

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          return generatedKeys.getLong(1);
        }
      }
    } catch (SQLException e) {
      throw new InternalServerErrorException(e.getMessage());
    }
    throw new InternalServerErrorException("Could not get DB connection");
  }
  @Override public PersonModel getPersonById(final long id)
      throws ResourceNotFoundException, InternalServerErrorException {
    try(Connection connection = dataSource.getConnection()) {
      final String getPersonByIdSql = "SELECT * FROM person WHERE id=?;";
      PreparedStatement preparedStatement = connection.prepareStatement(getPersonByIdSql);
      preparedStatement.setLong(1, id);
      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()){
        return new PersonModel(rs.getLong("id"),rs.getString("first_name"), rs.getString("last_name"), rs.getInt("year_of_birth"));
      }
    } catch (SQLException e){
      throw new InternalServerErrorException(e.getMessage());
    }
    throw new ResourceNotFoundException("Could not found resource");
  }
  @Override public void deletePersonById(final long id) throws InternalServerErrorException {
    try(Connection connection = dataSource.getConnection()) {
      String deletePersonByIdSql = "Delete from person where id = ?;";
      PreparedStatement preparedStatement = connection.prepareStatement(deletePersonByIdSql);
      preparedStatement.setLong(1, id);
      int result = preparedStatement.executeUpdate();
      if (result <= 0) {
        throw new InternalServerErrorException("Could not delete from database");
      }
    } catch (SQLException e){
      throw new InternalServerErrorException("Could not delete from database");
    }
  }

  @Override public long insertPersonORM(Person person) {
    return personRepository.save(person).getId();
  }

  @Override public Person getPersonByIdORM(final long id) throws ResourceNotFoundException {
    return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + id));
  }

  @Override public void deletePersonByIdORM(final long id) {
    personRepository.deleteById(id);
  }
}
