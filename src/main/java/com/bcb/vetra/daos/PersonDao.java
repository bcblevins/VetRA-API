package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Person;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * Data Access Object for the Person model.
 */
@Component
public class PersonDao {
    private final JdbcTemplate jdbcTemplate;

    public PersonDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<Person> getPersons() {
        return jdbcTemplate.query("SELECT * FROM person", this::mapToPerson);
    }
    public List<Person> getOwners() {
        return jdbcTemplate.query("SELECT * FROM person WHERE is_doctor = false;", this::mapToPerson);
    }
    public Person getPersonById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM person WHERE person_id = ?", this::mapToPerson, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Person createPerson(Person person) {
        String sql = "INSERT INTO person (first_name, last_name, is_doctor) VALUES (?,?,?) RETURNING person_id;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, person.getFirstName(), person.getLastName(), person.isDoctor());
        return getPersonById(id);
    }
    public Person updatePerson(Person person) {
        String sql = "UPDATE person SET first_name = ?, last_name = ?, is_doctor= ? " +
                "WHERE person_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(), person.isDoctor(), person.getId());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, excpected at least one.");
        } else {
            return getPersonById(person.getId());
        }
    }
    public boolean deletePerson(int id) {
        String sql = "DELETE FROM person WHERE person_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
    private Person mapToPerson(ResultSet resultSet, int rowNumber) throws SQLException{
        return new Person(
                resultSet.getInt("person_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getBoolean("is_doctor")
        );
    }
}
