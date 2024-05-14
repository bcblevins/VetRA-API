package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for refill request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private int requestId;
    @NotBlank(message = "Patient ID is required.")
    private int prescriptionId;
    private String status;
    private String requestDate;
}
