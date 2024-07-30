package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Model class for a message.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int messageId;
    @NotBlank(message = "Body is required.")
    private String body;
    private LocalDateTime timestamp;
    @NotBlank(message = "From username is required.")
    private String fromUsername;
    @NotBlank(message = "To username is required.")
    private String toUsername;
    private int testId;
    private int prescriptionId;
    private int patientId;
}
