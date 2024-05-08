package com.bcb.vetra.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
