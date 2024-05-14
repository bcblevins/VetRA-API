package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a message.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int messageId;
    private String subject;
    @NotBlank(message = "Body is required.")
    private String body;
    @NotBlank(message = "From username is required.")
    private String fromUsername;
    @NotBlank(message = "To username is required.")
    private String toUsername;
    private int testId;
    @NotBlank(message = "Patient ID is required.")
    private int patientId;
}
