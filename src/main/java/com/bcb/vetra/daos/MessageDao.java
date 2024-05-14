package com.bcb.vetra.daos;

import com.bcb.vetra.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//TODO: Add joins to SQL queries to get test_id and patient_id from test and patient tables
@Component
public class MessageDao {
    private JdbcTemplate jdbcTemplate;
    public MessageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<Message> getMessagesByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM message WHERE to_username = ? OR from_username = ?;", this::mapToMessage, username, username);
    }

    public Message getMessageById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM message WHERE message_id = ?;", this::mapToMessage, id);
    }

    public List<Message> getAll() {
        return jdbcTemplate.query("SELECT * FROM message;", this::mapToMessage);
    }

    public List<Message> getMessagesByPatientId(int id) {
        return jdbcTemplate.query("SELECT * FROM message WHERE patient_id = ?;", this::mapToMessage, id);
    }

    public List<Message> getMessagesByTestId(int id) {
        return jdbcTemplate.query("SELECT * FROM message WHERE test_id = ?;", this::mapToMessage, id);
    }

    public Message create(Message message) {
        Integer id = jdbcTemplate.queryForObject(
            "INSERT INTO message (subject, body, from_username, to_username, test_id, patient_id) " +
            "VALUES (?,?,?,?,?,?) " +
            "RETURNING message_id;",
            Integer.class,
            message.getSubject(),
            message.getBody(),
            message.getFromUsername(),
            message.getToUsername(),
            message.getTestId(),
            message.getPatientId()
        );
        return getMessageById(id);
    }

    public Message update(Message message) {
        int rowsAffected = jdbcTemplate.update(
            "UPDATE message SET subject = ?, body = ?, from_username = ?, to_username = ?, test_id = ?, patient_id = ? " +
            "WHERE message_id = ?;",
            message.getSubject(),
            message.getBody(),
            message.getFromUsername(),
            message.getToUsername(),
            message.getTestId(),
            message.getPatientId(),
            message.getMessageId()
        );
        return getMessageById(message.getMessageId());
    }

    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM message WHERE message_id = ?;", id) > 0;
    }

    private Message mapToMessage(ResultSet rs, int rowNum) throws SQLException {
        return new Message(
            rs.getInt("message_id"),
            rs.getString("subject"),
            rs.getString("body"),
            rs.getString("from_username"),
            rs.getString("to_username"),
            rs.getInt("test_id"),
            rs.getInt("patient_id")
        );
    }

}
