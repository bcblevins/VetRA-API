package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "INSERT INTO \"user\" (username, password, first_name, last_name) VALUES (?,?,?,?)";
        jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
        return getUserByUsername(user.getUsername());
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
    private User mapToUser(ResultSet resultSet, int rowNumber) throws SQLException{
        return new User(
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
    }
}
