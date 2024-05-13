package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Model class for diagnostic tests. Meant to include only top level test information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    private int id;
    @NotBlank(message = "Test must have a name.")
    private String name;
    @NotBlank(message = "Test must have a timestamp.")
    private LocalDateTime timestamp;
    @NotBlank(message = "Test must have a patientId.")
    private int patientID;
    @NotBlank(message = "Test must have a doctorUsername.")
    private String doctorUsername;
}
