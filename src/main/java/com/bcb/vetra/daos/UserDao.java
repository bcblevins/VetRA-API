package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * <strong>Data Access Object for users.</strong>
 * <br><br>
 * This class is responsible for all database operations related to users.
 * <br><br>
 * Models: <i>User</i>
 */
@Component
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserDao(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
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

    public User createUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        String sql = "INSERT INTO \"user\" (username, password, first_name, last_name, email) VALUES (?,?,?,?,?) RETURNING username;";
        String username = jdbcTemplate.queryForObject(sql, String.class, user.getUsername(), hashedPassword, user.getFirstName(), user.getLastName());
        return getUserByUsername(username);
    }
    public User updateUser(User user) {
        String sql = "UPDATE \"user\" SET first_name = ?, last_name = ?, email = ? " +
                "WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getUserByUsername(user.getUsername());
        }
    }

    public User updatePassword(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        String sql = "UPDATE \"user\" SET password = ? WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, hashedPassword, user.getUsername());
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
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
                resultSet.getString("email")
        );
    }
    private String mapToRoles(ResultSet resultSet, int rowNumber) throws SQLException {
        return resultSet.getString("role");
    }
}
