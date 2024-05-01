package com.bcb.vetra.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Model class for patient record.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private int id;
    private String chartNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String species;
    private String sex;
    private int ownerID;

}
