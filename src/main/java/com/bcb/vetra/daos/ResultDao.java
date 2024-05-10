package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Result;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for the Result model.
 */
@Component
public class ResultDao {
    private final JdbcTemplate jdbcTemplate;
    public ResultDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public Result getResultById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM result WHERE result_id = ?", this::mapToResult, id);
    }
    public List<Result> getResultsForTest(int testId) {
        return jdbcTemplate.query("SELECT * FROM result WHERE test_id = ? ORDER BY result_id", this::mapToResult, testId);
    }
    public Result create(Result result) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO result (test_id, parameter_name, result_value) " +
                        "VALUES (?,?,?) " +
                        "RETURNING result_id;",
                Integer.class,
                result.getTestID(),
                result.getParameterName(),
                result.getResultValue()
        );
        return getResultById(id);
    }
    public Result update(Result result) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE result SET test_id = ?, parameter_name = ?, result_value = ? " +
                        "WHERE result_id = ?;",
                result.getTestID(),
                result.getParameterName(),
                result.getResultValue(),
                result.getResultID()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getResultById(result.getResultID());
        }
    }
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM result WHERE result_id = ?", id) > 0;
    }
    private Result mapToResult(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Result(
                resultSet.getInt("result_id"),
                resultSet.getInt("test_id"),
                resultSet.getString("parameter_name"),
                resultSet.getString("result_value")
        );
    }
}
