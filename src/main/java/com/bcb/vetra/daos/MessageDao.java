package com.bcb.vetra.daos;

import com.bcb.vetra.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <strong>Data Access Object for messages.</strong>
 * <br><br>
 * This class is responsible for all database operations related to messages. Includes methods that filter results by username, patient ID, and test ID.
 * <br><br>
 * Models: <i>Message</i>
 */
@Component
public class MessageDao {
    private JdbcTemplate jdbcTemplate;

    public MessageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets all messages sent to or from a specific username.
     * @param username
     * @return List of messages
     */
    public List<Message> getMessagesByUsername(String username) {
        return jdbcTemplate.query("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                        "FROM message  " +
                        "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                        "LEFT JOIN message_patient ON message_patient.message_id = message.message_id " +
                        "WHERE to_username = ? OR from_username = ?;",
                this::mapToMessage, username, username);
    }

    public Message getMessageById(int id) {
        return jdbcTemplate.queryForObject("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                        "FROM message  " +
                        "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                        "LEFT JOIN message_patient ON message_patient.message_id = message.message_id " +
                        "WHERE message.message_id = ?;",
                this::mapToMessage, id);
    }
    /**
     * Gets a message by ID if it has been sent to or from a username.
     * @param id
     * @param username
     * @return Message
     */
    public Message getMessageByIdAndUsername(int id, String username) {
        return jdbcTemplate.queryForObject("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                        "FROM message  " +
                        "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                        "LEFT JOIN message_patient ON message_patient.message_id = message.message_id " +
                        "WHERE message.message_id = ? AND (to_username = ? OR from_username = ?);",
                this::mapToMessage, id, username, username);
    }

    public List<Message> getAll() {
        return jdbcTemplate.query("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                        "FROM message  " +
                        "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                        "LEFT JOIN message_patient ON message_patient.message_id = message.message_id",
                this::mapToMessage);
    }

    public List<Message> getMessagesByPatientId(int id) {
        return jdbcTemplate.query("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                        "FROM message  " +
                        "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                        "LEFT JOIN message_patient ON message_patient.message_id = message.message_id " +
                        "WHERE message_patient.patient_id = ?;",
                this::mapToMessage, id);
    }

    /**
     * Gets all messages associated with a specific testId.
     * @param testId
     * @return List of messages
     */
    public List<Message> getMessagesByTestId(int testId) {
        return jdbcTemplate.query("SELECT message.*, message_test.test_id, message_patient.patient_id  " +
                "FROM message  " +
                "LEFT JOIN message_test ON message_test.message_id = message.message_id " +
                "LEFT JOIN message_patient ON message_patient.message_id = message.message_id " +
                "WHERE message_test.test_id = ?;", this::mapToMessage, testId);
    }

    public Message create(Message message) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO message (subject, body, from_username, to_username) " +
                        "VALUES (?,?,?,?) " +
                        "RETURNING message_id;",
                Integer.class,
                message.getSubject(),
                message.getBody(),
                message.getFromUsername(),
                message.getToUsername()
        );
        if (message.getTestId() != 0) {
            jdbcTemplate.update("INSERT INTO message_test (message_id, test_id) VALUES (?,?);", id, message.getTestId());
        }
        if (message.getPatientId() != 0) {
            jdbcTemplate.update("INSERT INTO message_patient (message_id, patient_id) VALUES (?,?);", id, message.getPatientId());
        }
        return getMessageById(id);
    }

    public Message update(Message message) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE message SET subject = ?, body = ?, from_username = ?, to_username = ? " +
                        "WHERE message_id = ?;",
                message.getSubject(),
                message.getBody(),
                message.getFromUsername(),
                message.getToUsername(),
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
