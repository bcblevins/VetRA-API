package com.bcb.vetra.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View Model class for a single parameter with its result. Will be used to display the results of a test.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParameterWithResult {
    private int resultID;
    private int testID;
    private String name;
    private String resultValue;
    private String rangeLow;
    private String rangeHigh;
    private String unit;
    private String qualitativeNormal;
    private boolean isQualitative;
}
