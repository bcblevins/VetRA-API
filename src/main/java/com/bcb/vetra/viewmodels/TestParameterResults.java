package com.bcb.vetra.viewmodels;

import com.bcb.vetra.models.Parameter;
import com.bcb.vetra.models.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestParameterResults {
    private int id;
    private String name;
    private int patientID;
    private int doctorID;
    private List<Parameter> parameters;
    private List<Result> results;
    private String doctorNotes;
    private LocalDateTime timestamp;
}
