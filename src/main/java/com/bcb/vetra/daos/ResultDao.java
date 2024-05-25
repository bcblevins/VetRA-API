package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Result;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <strong>Data Access Object for results.</strong>
 * <br><br>
 * This class is responsible for all database operations related to results.
 * <br><br>
 * Models: <i>Result</i>
 */
@Component
public class ResultDao {
    private final JdbcTemplate jdbcTemplate;

    public ResultDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Gets a result by ID.
     *
     * @param id
     * @return Result
     */
    public Result getResultById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM result WHERE result_id = ?", this::mapToResult, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets all results for a test by ID.
     *
     * @param testId
     * @return List of Result
     */
    public List<Result> getResultsForTest(int testId) {
        return jdbcTemplate.query("SELECT * FROM result WHERE test_id = ? ORDER BY result_id", this::mapToResult, testId);
    }

    /**
     * Creates a new result.
     *
     * @param result
     * @return Result
     */
    public Result create(Result result) {
        try {
            Integer id = jdbcTemplate.queryForObject(
                    "INSERT INTO result (test_id, result_value, parameter_name, range_low, range_high, unit) " +
                            "VALUES (?,?,?,?,?,?) " +
                            "RETURNING result_id;",
                    Integer.class,
                    result.getTestID(),
                    result.getResultValue(),
                    result.getParameterName(),
                    result.getRangeLow(),
                    result.getRangeHigh(),
                    result.getUnit()
            );
            return getResultById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create result.");
        }
    }

    /**
     * Updates a result.
     *
     * @param result
     * @return Result
     */
    public Result update(Result result) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE result SET test_id = ?, result_value = ?, parameter_name = ?, range_low = ?, range_high = ?, unit = ?" +
                        "WHERE result_id = ?;",
                result.getTestID(),
                result.getResultValue(),
                result.getParameterName(),
                result.getRangeLow(),
                result.getRangeHigh(),
                result.getUnit(),
                result.getResultID()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getResultById(result.getResultID());
        }
    }

    /**
     * Deletes a result by ID.
     *
     * @param id
     */
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM result WHERE result_id = ?", id) > 0;
    }

    /**
     * Maps a ResultSet to a Result object.
     *
     * @param resultSet
     * @param rowNumber
     * @return Result
     * @throws SQLException
     */
    private Result mapToResult(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Result(
                resultSet.getInt("result_id"),
                resultSet.getInt("test_id"),
                resultSet.getString("result_value"),
                resultSet.getString("parameter_name"),
                resultSet.getString("range_low"),
                resultSet.getString("range_high"),
                resultSet.getString("unit")
        );
    }
}
