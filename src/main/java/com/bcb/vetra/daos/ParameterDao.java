package com.bcb.vetra.daos;

import com.bcb.vetra.models.Parameter;
import com.bcb.vetra.models.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public class ParameterDao {
    private final JdbcTemplate jdbcTemplate;
    public ParameterDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public Parameter getParameterByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM parameter WHERE name = ?", this::mapToParameter, name);
    }
    public List<Parameter> getParametersForTest(Test test) {
        return jdbcTemplate.query("SELECT * FROM parameter WHERE test_id = ?", this::mapToParameter, test.getId());
    }
    public Parameter create(Parameter parameter) {
        String name = jdbcTemplate.queryForObject(
                "INSERT INTO parameter (name, range_low, range_high, unit, qualitative_normal, is_qualitative) " +
                        "VALUES (?,?,?,?,?,?)" +
                        "RETURNING name;",
                String.class,
                parameter.getName(),
                parameter.getRangeHigh(),
                parameter.getRangeLow(),
                parameter.getUnit(),
                parameter.isQualitative()
        );
        return getParameterByName(name);
    }
    public boolean update(Parameter parameter) {
        String sql = "UPDATE parameter " +
                "SET name = ?, range_low = ?, range_high = ?, unit = ?, qualitative_normal = ?, is_qualitative = ? " +
                "WHERE name = ?";
        int rowsAffected = jdbcTemplate.update(sql, parameter.getName(), parameter.getRangeLow(), parameter.getRangeHigh(), parameter.getUnit(), parameter.getQualitativeNormal(), parameter.isQualitative());
        return rowsAffected == 0;
    }
    public boolean delete(Parameter parameter) {
        String sql = "DELETE FROM parameter WHERE name = ?";
        return jdbcTemplate.update(sql, parameter.getName()) > 0;
    }
    private Parameter mapToParameter(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Parameter(
                resultSet.getString("name"),
                resultSet.getString("range_low"),
                resultSet.getString("range_high"),
                resultSet.getString("unit"),
                resultSet.getString("qualitative_normal"),
                resultSet.getBoolean("is_qualitative")
        );
    }
}
