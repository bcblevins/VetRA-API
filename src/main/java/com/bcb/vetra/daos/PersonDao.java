package com.bcb.vetra.daos;

import com.bcb.vetra.models.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDao {
    private final JdbcTemplate jdbcTemplate;

    public PersonDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public Person getPersonById(int id) {
        jdbcTemplate.queryForRowSet(
                "SELECT * FROM person WHERE person_id = ?", id);
    }
    private Person mapToPerson(SqlRowSet rowSet){
        return new Person(
                rowSet.getInt("person_id"),
                rowSet.getString("first_name"),
                rowSet.getString("last_name"),
                rowSet.getBoolean("is_doctor")
        );
    }
}
