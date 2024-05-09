package com.bcb.vetra.models;

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
    private String name;
    private LocalDateTime timestamp;
    private String doctorNotes;
    private int patientID;
    private int doctorID;
}
