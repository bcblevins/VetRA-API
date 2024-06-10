package com.bcb.vetra.models;

import com.bcb.vetra.services.deserializers.EpochToLocalDateDeserializer;
import com.bcb.vetra.services.deserializers.SexIdToSexDeserializer;
import com.bcb.vetra.services.deserializers.SpeciesIdToSpeciesDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {
    private int patientId;
    @JsonAlias({"name", "firstName"})
    @NotBlank(message = "First name is required.")
    private String firstName;
    @JsonAlias({"date_of_birth", "birthday"})
    @JsonDeserialize(using = EpochToLocalDateDeserializer.class)
    private LocalDate birthday;
    @JsonAlias({"species", "species_id"})
    @JsonDeserialize(using = SpeciesIdToSpeciesDeserializer.class)
    private String species;
    @JsonAlias({"sex_id", "sex"})
    @JsonDeserialize(using = SexIdToSexDeserializer.class)
    private String sex;
    @NotBlank(message = "Owner username is required.")
    @JsonAlias({"contact_id", "ownerUsername"})
    private String ownerUsername;
}
