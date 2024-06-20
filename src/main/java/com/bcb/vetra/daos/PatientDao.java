package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Patient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
     * Gets a list of ezyVet patient IDs.
     *
     * @return
     */
    public List<String> getEzyVetPatientIds() {
        return jdbcTemplate.queryForList("SELECT vms_id FROM \"patient_vms\" WHERE vms_name = 'ezyvet';", String.class);
    }

    /**
     * Checks if a patient exists by username and patient name.
     *
     * @param username
     * @param patientName
     * @return boolean
     */
    public boolean patientExists(String username, String patientName) {
        List<Patient> patients = getPatientsByUsername(username);
        for (Patient patient : patients) {
            if (patient.getFirstName().toLowerCase().equals(patientName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a patient by ID if they are associated with a specific username.
     *
     * @param patientId
     * @param username  owner id
     * @return Patient
     */
    public Patient getPatientByIdAndOwner(int patientId, String username) {
        Patient patient = null;
        try {
            patient = jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ? AND owner_username = ?;", this::mapToPatient, patientId, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        patient.setVmsIds(getEzyVetIdsByPatientId(patientId));
        return patient;
    }

    public Map<String, String> getEzyVetIdsByPatientId(int patientId) {
        List<String> Ids;
        Map<String, String> vmsIds = new HashMap<>();
        try {
            Ids = jdbcTemplate.queryForList("SELECT vms_id FROM patient_vms WHERE patient_id = ? AND vms_name = 'ezyvet';", String.class, patientId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        for (String id : Ids) {
            vmsIds.put("ezyvet", id);
        }
        return vmsIds;
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

    /**
     * Gets all patients.
     *
     * @return List of patients
     */
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
            patient.setPatientId(id);
            attributeVmsIdToPatient(patient.getPatientId(), patient.getVmsIds());
            return getPatientById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create patient.");
        }
    }

    /**
     * Associates a patient with a VMS ID.
     *
     * @param id
     * @param vmsIds
     * @return boolean
     */
    public boolean attributeVmsIdToPatient(int id, Map<String, String> vmsIds) {
        int count = 0;
        for (Map.Entry<String, String> entry : vmsIds.entrySet()) {
            String vmsId = entry.getValue();
            String vmsName = entry.getKey().toLowerCase();
            String sql = "INSERT INTO \"patient_vms\" (patient_id, vms_id, vms_name) VALUES (?, ?, ?)";
            count += jdbcTemplate.update(sql, id, vmsId, vmsName);
        }
        return count > 0;
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
