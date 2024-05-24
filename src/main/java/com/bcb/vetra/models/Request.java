package com.bcb.vetra.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Model class for refill request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private int requestId;
    @Min(value = 1, message = "Prescription ID is required.")
    private int prescriptionId;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") // ensures that using SQL timestamp format in PUT/POST requests doesn't cause deserialization issues
    private LocalDateTime requestDate;
}
