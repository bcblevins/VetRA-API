package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Patient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <strong>Data Access Object for patients.</strong>
 * <br><br>
 * This class is responsible for all database operations related to patients.
 * <br><br>
 * Models: <i>Patient</i>
 */
@Component
public class PatientDao {
    private final JdbcTemplate jdbcTemplate;

    public PatientDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Gets a patient by ID.
     *
     * @param id
     * @return Patient
     */
    public Patient getPatientById(int id) {
        Patient patient = null;
        try {
            patient = jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ?", this::mapToPatient, id);
        } catch (EmptyResultDataAccessException e) {
        }
        return patient;
    }

    /**
     * Gets a patient by ID if they are associated with a specific username.
     *
     * @param patientId
     * @param username  owner id
     * @return Patient
     */
    public Patient getPatientByIdAndOwner(int patientId, String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ? AND owner_username = ?;", this::mapToPatient, patientId, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets all patients owned by a user by username.
     *
     * @param username
     * @return List of patients
     */
    public List<Patient> getPatientsByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM patient WHERE owner_username = ? ORDER BY first_name;", this::mapToPatient, username);
    }

    public List<Patient> getAllPatients() {
        return jdbcTemplate.query("SELECT * FROM patient ORDER BY first_name", this::mapToPatient);
    }

    /**
     * Creates a new patient.
     *
     * @param patient
     * @return Patient
     */
    public Patient create(Patient patient) {
        try {
            Integer id = jdbcTemplate.queryForObject(
                    "INSERT INTO patient (first_name, birthday, species, sex, owner_username) " +
                            "VALUES (?,?,?,?,?) " +
                            "RETURNING patient_id;",
                    Integer.class,
                    patient.getFirstName(),
                    patient.getBirthday(),
                    patient.getSpecies(),
                    patient.getSex(),
                    patient.getOwnerUsername()
            );
            return getPatientById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create patient.");
        }
    }

    /**
     * Updates a patient.
     *
     * @param patient
     * @return Patient
     */
    public Patient updatePatient(Patient patient) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE patient SET first_name = ?, birthday = ?, species = ?, sex = ?, owner_username = ? " +
                        "WHERE patient_id = ?;",
                patient.getFirstName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerUsername(),
                patient.getPatientId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPatientById(patient.getPatientId());
        }
    }

    /**
     * Deletes a patient by ID.
     *
     * @param id
     * @return boolean
     */
    public boolean deletePatient(int id) {
        return jdbcTemplate.update("DELETE FROM patient WHERE patient_id = ?;", id) > 1;
    }

    /**
     * Maps a ResultSet to a Patient object.
     *
     * @param resultSet
     * @param rowNumber
     * @return Patient
     * @throws SQLException
     */
    private Patient mapToPatient(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Patient(
                resultSet.getInt("patient_id"),
                resultSet.getString("first_name"),
                resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("species"),
                resultSet.getString("sex"),
                resultSet.getString("owner_username")
        );
    }

}
