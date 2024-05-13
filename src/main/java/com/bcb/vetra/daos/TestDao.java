package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.viewmodels.ParameterWithResult;
import com.bcb.vetra.viewmodels.TestWithDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Test model.
 */
@Component
public class TestDao {
    private final JdbcTemplate jdbcTemplate;

    public TestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Test getTestById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM test WHERE test_id = ?", this::mapToTest, id);
    }

    public List<Test> getTestsForPatient(int patientId) {
        return jdbcTemplate.query("SELECT * FROM test WHERE patient_id = ?", this::mapToTest, patientId);
    }

    /**
     * Gets all tests for a patient with all parameters and results.
     * @param patientId
     * @return List of TestWithDetails
     */
    public List<TestWithDetails> getTestWithDetailsForPatient(int patientId) {
        List<TestWithDetails> testsForPatient = new ArrayList<>();
        String sql = "select test.*, parameter.name, result.result_id, result.result_value, parameter.range_low, parameter.range_high, parameter.unit, parameter.qualitative_normal, parameter.is_qualitative " +
                "from test " +
                "join result on result.test_id = test.test_id " +
                "join parameter on parameter.name = result.parameter_name " +
                "where patient_id = ? " +
                "order by test_id;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, patientId);

        int currentTestId;
        int previousTestId = -1;
        TestWithDetails currentTest = null;

        while (results.next()) {
            currentTestId = results.getInt("test_id");
            if (currentTestId != previousTestId) {
                if (currentTest != null) {
                    testsForPatient.add(currentTest);
                }
                currentTest = new TestWithDetails();
                currentTest.setTestId(currentTestId);
                currentTest.setName(results.getString("name"));
                currentTest.setPatientID(results.getInt("patient_id"));
                currentTest.setDoctorUsername(results.getString("doctor_username"));
                if (results.getTimestamp("time_stamp") != null) {
                    currentTest.setTimestamp(results.getTimestamp("time_stamp").toLocalDateTime());
                }
            }
            currentTest.addToResults(new ParameterWithResult(
                    results.getInt("result_id"),
                    results.getInt("test_id"),
                    results.getString("name"),
                    results.getString("result_value"),
                    results.getString("range_low"),
                    results.getString("range_high"),
                    results.getString("unit"),
                    results.getString("qualitative_normal"),
                    results.getBoolean("is_qualitative")
            ));
            previousTestId = currentTestId;
        }
        if (currentTest != null) {
            testsForPatient.add(currentTest);
        }
        return testsForPatient;
    }
    /**
     * Gets a test with all parameters and results by its id.
     * @param testId
     * @return TestWithDetails
     */

    public TestWithDetails getTestWithDetailsByTestId(int testId) {
        String sql = "select test.*, parameter.name, result.result_id, result.result_value, parameter.range_low, parameter.range_high, parameter.unit, parameter.qualitative_normal, parameter.is_qualitative\n" +
                "from test\n" +
                "join result on result.test_id = test.test_id\n" +
                "join parameter on parameter.name = result.parameter_name " +
                "where test.test_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, testId);

        int currentTestId;
        int previousTestId = -1;
        TestWithDetails currentTest = new TestWithDetails();

        while (results.next()) {
            currentTestId = results.getInt("test_id");
            if (currentTestId != previousTestId) {
                currentTest.setTestId(currentTestId);
                currentTest.setName(results.getString("name"));
                currentTest.setPatientID(results.getInt("patient_id"));
                currentTest.setDoctorUsername(results.getString("doctor_username"));
                currentTest.setTimestamp(results.getTimestamp("time_stamp").toLocalDateTime());
            }
            currentTest.addToResults(new ParameterWithResult(
                    results.getInt("result_id"),
                    results.getInt("test_id"),
                    results.getString("name"),
                    results.getString("result_value"),
                    results.getString("range_low"),
                    results.getString("range_high"),
                    results.getString("unit"),
                    results.getString("qualitative_normal"),
                    results.getBoolean("is_qualitative")
            ));
            previousTestId = currentTestId;
        }

        return currentTest;
    }



    public Test create(Test test) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO test (name, time_stamp, patient_id, doctor_username) " +
                        "VALUES (?,?,?,?) " +
                        "RETURNING test_id;",
                Integer.class,
                test.getName(),
                test.getTimestamp(),
                test.getPatientID(),
                test.getDoctorUsername()
        );
        return getTestById(id);
    }

    public Test update(Test test) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE test SET name = ?, time_stamp = ?, patient_id = ?, doctor_username = ? " +
                        "WHERE test_id = ?;",
                test.getName(),
                test.getTimestamp(),
                test.getPatientID(),
                test.getDoctorUsername(),
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

    /**
     * Deletes a test of a patient.
     * @param id id of test
     * @param patientId
     * @return boolean indicating success
     */
    public boolean deleteTestOfPatient(int id, int patientId) {
        return jdbcTemplate.update("DELETE FROM test WHERE test_id = ? AND patient_id = ?", id, patientId) > 0;
    }

    //----------------------
    // Helper methods
    //----------------------
    public Test mapToTest(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Test(
                resultSet.getInt("test_id"),
                resultSet.getString("name"),
                resultSet.getTimestamp("time_stamp").toLocalDateTime(),
                resultSet.getInt("patient_id"),
                resultSet.getString("doctor_username")
        );
    }

}
