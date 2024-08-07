package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Notification;
import com.bcb.vetra.models.Patient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class NotificationDao {
    private final JdbcTemplate jdbcTemplate;

    public NotificationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Notification getNotificationById(int id) {
        Notification notification = null;
        try {
            notification = jdbcTemplate.queryForObject("SELECT * FROM notification WHERE notification_id = ?", this::mapToNotification, id);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        }
        return notification;
    }

    public List<Notification> getNotificationsByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM notification WHERE username = ?", this::mapToNotification, username);
    }

    public Notification create(Notification notification) {
        System.out.println(notification.toString());
        try {
            Integer id = jdbcTemplate.queryForObject(
                            "INSERT INTO notification (username, patient_id, message_id, request_id, test_id, is_read) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING notification_id;",
                    Integer.class,
                    notification.getUsername(),
                    notification.getPatientId() == 0 ? null : notification.getPatientId(),
                    notification.getMessageId() == 0 ? null : notification.getMessageId(),
                    notification.getRequestId() == 0 ? null : notification.getRequestId(),
                    notification.getTestId() == 0 ? null : notification.getTestId(),
                    notification.isRead()
            );
            return getNotificationById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create notification.");
        }
    }

    public Notification update(Notification notification) {
        try {
            jdbcTemplate.update("UPDATE notification SET username = ?, patient_id = ?, message_id = ?, request_id = ?, test_id = ?, is_read = ?, timestamp = ? WHERE notification_id = ?",
                    notification.getUsername(),
                    notification.getPatientId(),
                    notification.getMessageId(),
                    notification.getRequestId(),
                    notification.getTestId(),
                    notification.isRead(),
                    notification.getTimestamp(),
                    notification.getId()
            );
            return getNotificationById(notification.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to update notification.");
        }
    }

    public boolean markAsRead(int id, String username) {
        int rowsAffected = jdbcTemplate.update("UPDATE notification SET is_read = true WHERE notification_id = ? AND username = ?", id, username);
        return rowsAffected > 0;
    }

    public boolean delete(int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM notification WHERE notification_id = ?", id);
        return rowsAffected > 0;
    }

    private Notification mapToNotification(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Notification(
                resultSet.getInt("notification_id"),
                resultSet.getString("username"),
                resultSet.getInt("patient_id"),
                resultSet.getInt("message_id"),
                resultSet.getInt("request_id"),
                resultSet.getInt("test_id"),
                resultSet.getBoolean("is_read"),
                resultSet.getTimestamp("timestamp").toLocalDateTime()
        );
    }}
