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
     * @param id owner id
     * @return Patient
     */
    public Patient getPatientByIdAndOwner(int patientId, int id){
        return jdbcTemplate.queryForObject("SELECT * FROM patient WHERE patient_id = ? AND owner_id = ?;", this::mapToPatient, patientId, id);
    }
    public List<Patient> getPatientsByOwnerId(int id) {
        return jdbcTemplate.query("SELECT * FROM patient WHERE owner_id = ? ORDER BY first_name;", this::mapToPatient, id);
    }
    public List<Patient> getAllPatients() {
        return jdbcTemplate.query("SELECT * FROM patient ORDER BY first_name", this::mapToPatient);
    }
    public Patient create(Patient patient) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO patient (chart_number, first_name, last_name, birthday, species, sex, owner_id) " +
                        "VALUES (?,?,?,?,?,?,?) " +
                        "RETURNING patient_id;",
                Integer.class,
                patient.getChartNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerID()
                );
        return getPatientById(id);
    }
    public Patient update(Patient patient) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE patient SET chart_number = ?, first_name = ?, last_name = ?, birthday = ?, species = ?, sex = ?, owner_id = ? " +
                        "WHERE patient_id = ?;",
                patient.getChartNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerID(),
                patient.getPatientId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPatientById(patient.getPatientId());
        }
    }

    public Patient updatePatientOfOwner(Patient patient, int ownerId) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE patient SET chart_number = ?, first_name = ?, last_name = ?, birthday = ?, species = ?, sex = ?, owner_id = ? " +
                        "WHERE patient_id = ? AND owner_id = ?;",
                patient.getChartNumber(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthday(),
                patient.getSpecies(),
                patient.getSex(),
                patient.getOwnerID(),
                patient.getPatientId(),
                ownerId
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
    public boolean deletePatientOfOwner(int id, int ownerId) {
        return jdbcTemplate.update("DELETE FROM patient WHERE patient_id = ? AND owner_id =;", id, ownerId) > 1;
    }
    private Patient mapToPatient(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Patient(
                resultSet.getInt("patient_id"),
                resultSet.getString("chart_number"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("species"),
                resultSet.getString("sex"),
                resultSet.getInt("owner_id")
        );
    }

}
