package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Data Access Object for the User model.
 */
@Component
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM \"user\" ORDER BY username;", this::mapToUser);
    }

    public User getUserByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM \"user\" WHERE username = ?", this::mapToUser, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User createPerson(User user) {
        String sql = "INSERT INTO \"user\" (username, password, first_name, last_name) VALUES (?,?,?,?) RETURNING username;";
        String username = jdbcTemplate.queryForObject(sql, String.class, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
        return getUserByUsername(username);
    }
    public User updatePerson(User user) {
        String sql = "UPDATE \"user\" SET first_name = ?, last_name = ? " +
                "WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getUsername());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, excpected at least one.");
        } else {
            return getUserByUsername(user.getUsername());
        }
    }

    public User updatePassword(User user) {
        String sql = "UPDATE \"user\" SET password = ? WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getPassword(), user.getUsername());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, excpected at least one.");
        } else {
            return getUserByUsername(user.getUsername());
        }
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM \"user\" WHERE username = ? ";
        return jdbcTemplate.update(sql, username) > 0;
    }

    //------------------
    // Role methods
    //------------------
    public List<String> getRoles(String username) {
        return jdbcTemplate.query("SELECT role FROM \"role\" WHERE username = ?;", this::mapToRoles, username);
    }
    public User addRole(String username, String role) {
        String sql = "INSERT INTO \"role\" (username, role) VALUES (?,?)";
        jdbcTemplate.update(sql, username, role);
        return getUserByUsername(username);
    }
    public void deleteRole(String username, String role) {
        String sql = "DELETE FROM \"role\" WHERE username = ? AND role = ?";
        jdbcTemplate.update(sql, username, role);
    }

    //------------------
    // Helper methods
    //------------------
    private User mapToUser(ResultSet resultSet, int rowNumber) throws SQLException{
        String username = resultSet.getString("username");
        return new User(
                username,
                resultSet.getString("password"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                getRoles(username)
        );
    }
    private String mapToRoles(ResultSet resultSet, int rowNumber) throws SQLException {
        return resultSet.getString("role");
    }
}
