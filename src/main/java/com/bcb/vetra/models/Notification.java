package com.bcb.vetra.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private int id;
    private String username;
    private int patientId;
    private int messageId;
    private int requestId;
    private int testId;
    private boolean isRead;
    private LocalDateTime timestamp;

    public Notification(String username, int patientId, int messageId, int requestId, int testId, boolean isRead) {
        this.username = username;
        this.patientId = patientId;
        this.messageId = messageId;
        this.requestId = requestId;
        this.testId = testId;
        this.isRead = isRead;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", patientId=" + patientId +
                ", messageId=" + messageId +
                ", requestId=" + requestId +
                ", testId=" + testId +
                ", isRead=" + isRead +
                ", timestamp=" + timestamp +
                '}';

    }
}
