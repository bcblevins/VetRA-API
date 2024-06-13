package com.bcb.vetra.daos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MetaDao {

    private JdbcTemplate jdbcTemplate;
    public MetaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LocalDateTime getTimeForAction(String action) {
        action = action.toLowerCase();
        String timestamp = jdbcTemplate.queryForObject("SELECT performed_at FROM \"meta\" WHERE action = ?;", String.class, action);
        timestamp = timestamp.replace(' ', 'T');
        return LocalDateTime.parse(timestamp);
    }

    public void setTimeForAction(String action, LocalDateTime time) {
        action = action.toLowerCase();
        jdbcTemplate.update("UPDATE \"meta\" SET performed_at = ? WHERE action = ?;", time.toString(), action);
    }

    public void createAction(String action) {
        action = action.toLowerCase();
        jdbcTemplate.update("INSERT INTO \"meta\" (action) VALUES (?);", action);
    }

}
