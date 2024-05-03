package com.bcb.vetra.models;
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
    private int testID;
    private String parameterName;
    private String resultValue;
}
