package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.viewmodels.TestParameterResults;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TestDao {
    private final JdbcTemplate jdbcTemplate;
    public TestDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public Test getTestById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM test WHERE test_id = ?", this::mapToTest, id);
    }
    public List<Test> getTestsForPatient(int patientId) {
        return jdbcTemplate.query("SELECT * FROM test WHERE patient_id = ?", this::mapToTest, patientId);
    }
    //TODO: Implement this method
    public List<TestParameterResults> getTestParameterResultsForPatient(int patientId) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }
    public Test create(Test test) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO test (name, time_stamp, doctor_notes, patient_id, doctor_id) " +
                        "VALUES (?,?,?,?,?) " +
                        "RETURNING test_id;",
                Integer.class,
                test.getName(),
                test.getTimestamp(),
                test.getDoctorNotes(),
                test.getPatientID(),
                test.getDoctorID()
        );
        return getTestById(id);
    }
    public Test update(Test test) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE test SET name = ?, time_stamp = ?, doctor_notes = ?, patient_id = ?, doctor_id = ? " +
                        "WHERE test_id = ?;",
                test.getName(),
                test.getTimestamp(),
                test.getDoctorNotes(),
                test.getPatientID(),
                test.getDoctorID(),
                test.getId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getTestById(test.getId());
        }
    }
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM test WHERE test_id = ?", id) > 0;
    }
    public Test mapToTest(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Test(
                resultSet.getInt("test_id"),
                resultSet.getString("name"),
                resultSet.getTimestamp("time_stamp").toLocalDateTime(),
                resultSet.getString("doctor_notes"),
                resultSet.getInt("patient_id"),
                resultSet.getInt("doctor_id")
        );
    }
}
