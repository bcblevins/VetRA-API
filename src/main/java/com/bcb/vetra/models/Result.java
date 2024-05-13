package com.bcb.vetra.models;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class for a single result from a test.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int resultID;
    @NotBlank(message = "Result must have a Test ID.")
    private int testID;
    @NotBlank(message = "Result must have a parameter name.")
    private String parameterName;
    @NotBlank(message = "Result must have a result value.")
    private String resultValue;
}
