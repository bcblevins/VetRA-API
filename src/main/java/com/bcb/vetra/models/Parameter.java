package com.bcb.vetra.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class representing a single parameter for a Test.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    @NotBlank(message = "Parameter must have a name.")
    private String name;
    private String rangeLow;
    private String rangeHigh;
    private String unit;
    private String qualitativeNormal;
    private boolean isQualitative;

}
