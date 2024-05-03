package com.bcb.vetra.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model class representing a single result for a Test, including parameter information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    private String name;
    private String rangeLow;
    private String rangeHigh;
    private String unit;
    private String qualitativeNormal;
    private boolean isQualitative;
}
