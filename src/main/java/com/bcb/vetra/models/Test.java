package com.bcb.vetra.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Model class for diagnostic tests
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    private String name;
    private List<TestParameter> parameters;
    private String doctorNotes;
    private LocalDateTime timestamp;

}
