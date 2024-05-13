package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for the Patient model.
 */
@Component
public class PatientDao {
    private final JdbcTemplate jdbcTemplate;
    public PatientDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public Patient getPatientById(int id){
        return jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ?", this::mapToPatient, id);
    }
    /**
     * Gets a patient by ID from specific owner.
     * @param patientId
     * @param username owner id
     * @return Patient
     */
    public Patient getPatientByIdAndOwner(int patientId, String username){
        return jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ? AND username = ?;", this::mapToPatient, patientId, username);
    }
    public List<Patient> getPatientsByOwnerId(String username) {
        return jdbcTemplate.query("SELECT * FROM patient WHERE username = ? ORDER BY first_name;", this::mapToPatient, username);
    }
    public List<Patient> getAllPatients() {
        return jdbcTemplate.query("SELECT * FROM patient ORDER BY first_name", this::mapToPatient);
    }
    public Patient create(Patient patient) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO patient (first_name, birthday, species, sex, owner_username) " +
                        "VALUES (?,?,?,?,?,?,?) " +
                        "RETURNING patient_id;",
                Integer.class,
                patient.getFirstName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerUsername()
                );
        return getPatientById(id);
    }
    public Patient update(Patient patient) {
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

    public Patient updatePatientOfOwner(Patient patient, String username) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE patient SET first_name = ?, birthday = ?, species = ?, sex = ?, owner_username = ? " +
                        "WHERE patient_id = ? AND username = ?;",
                patient.getFirstName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerUsername(),
                patient.getPatientId(),
                username
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPatientById(patient.getPatientId());
        }
    }
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM patient WHERE patient_id = ?;", id) > 1;
    }
    public boolean deletePatient(int id) {
        return jdbcTemplate.update("DELETE FROM patient WHERE patient_id = ?", id) > 1;
    }
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
