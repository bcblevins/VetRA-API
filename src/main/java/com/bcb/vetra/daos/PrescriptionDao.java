package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.viewmodels.PrescriptionWithMedication;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <strong>Data Access Object for prescriptions.</strong>
 * <br><br>
 * This class is responsible for all database operations related to prescriptions.
 * <br><br>
 * Models: <i>PrescriptionWithMedication(view model)</i>
 */
@Component
public class PrescriptionDao {
    private final JdbcTemplate jdbcTemplate;

    public PrescriptionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Gets a prescription by ID.
     *
     * @param id
     * @return PrescriptionWithMedication
     */
    public PrescriptionWithMedication getPrescriptionById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT prescription.*, medication.unit " +
                            "FROM prescription " +
                            "JOIN medication ON medication.name = prescription.medication_name " +
                            "WHERE prescription.prescription_id = ?;",
                    this::mapToPrescriptionWithMedication,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets all prescriptions for a patient by ID.
     *
     * @param id
     * @return List of PrescriptionWithMedication
     */
    public List<PrescriptionWithMedication> getPrescriptionsByPatientId(int id) {
        return jdbcTemplate.query(
                "SELECT prescription.*, medication.unit " +
                        "FROM prescription " +
                        "JOIN medication ON medication.name = prescription.medication_name " +
                        "WHERE prescription.patient_id = ? " +
                        "ORDER BY prescription.medication_name;",
                this::mapToPrescriptionWithMedication,
                id
        );
    }

    /**
     * Gets all prescriptions.
     *
     * @return List of PrescriptionWithMedication
     */
    public List<PrescriptionWithMedication> getAllPrescriptions() {
        return jdbcTemplate.query(
                "SELECT prescription.*, medication.unit " +
                        "FROM prescription " +
                        "JOIN medication ON medication.name = prescription.medication_name " +
                        "ORDER BY prescription.medication_name;",
                this::mapToPrescriptionWithMedication);
    }

    /**
     * Creates a new prescription.
     *
     * @param prescription
     * @return PrescriptionWithMedication
     */
    public PrescriptionWithMedication create(PrescriptionWithMedication prescription) {
        insertMedicationIfDoesNotExist(prescription);
        try {
            Integer id = jdbcTemplate.queryForObject(
                    "INSERT INTO prescription (medication_name, quantity, instructions, is_active, patient_id, doctor_username, refills) " +
                            "VALUES (?,?,?,?,?,?,?) " +
                            "RETURNING prescription_id;",
                    Integer.class,
                    prescription.getName(),
                    prescription.getQuantity(),
                    prescription.getInstructions(),
                    prescription.isActive(),
                    prescription.getPatientId(),
                    prescription.getDoctorUsername(),
                    prescription.getRefills()
            );
            return getPrescriptionById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create prescription.");
        }
    }

    /**
     * Updates a prescription.
     *
     * @param prescription
     * @return PrescriptionWithMedication
     */
    public PrescriptionWithMedication update(PrescriptionWithMedication prescription) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE prescription SET medication_name = ?, quantity = ?, instructions = ?, is_active = ?, patient_id = ?, doctor_username = ?, refills = ? " +
                        "WHERE prescription_id = ?;",
                prescription.getName(),
                prescription.getQuantity(),
                prescription.getInstructions(),
                prescription.isActive(),
                prescription.getPatientId(),
                prescription.getDoctorUsername(),
                prescription.getRefills(),
                prescription.getPrescriptionId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPrescriptionById(prescription.getPrescriptionId());
        }
    }

    /**
     * Deletes a prescription by ID.
     *
     * @param id
     * @return boolean indicating success
     */
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM prescription WHERE prescription_id = ?", id) > 0;
    }

    /**
     * Deletes a prescription of a patient.
     *
     * @param id        id of prescription
     * @param patientId
     * @return boolean indicating success
     */
    public boolean deletePrescriptionOfPet(int id, int patientId) {
        return jdbcTemplate.update("DELETE FROM prescription WHERE prescription_id = ? AND patient_id = ?", id, patientId) > 0;
    }

    //----------------------
    // Helper methods
    //----------------------

    /**
     * Maps a row in the result set to a PrescriptionWithMedication object.
     *
     * @param resultSet
     * @param rowNumber
     * @return PrescriptionWithMedication
     * @throws SQLException
     */
    private PrescriptionWithMedication mapToPrescriptionWithMedication(ResultSet resultSet, int rowNumber) throws SQLException {
        return new PrescriptionWithMedication(
                resultSet.getInt("prescription_id"),
                resultSet.getString("medication_name"),
                resultSet.getInt("quantity"),
                resultSet.getString("unit"),
                resultSet.getString("instructions"),
                resultSet.getInt("refills"),
                resultSet.getBoolean("is_active"),
                resultSet.getInt("patient_id"),
                resultSet.getString("doctor_username")
        );
    }

    /**
     * Inserts a medication into medication table if it is not already present.
     *
     * @param medication
     * @return boolean indicating success
     */
    private boolean insertMedicationIfDoesNotExist(PrescriptionWithMedication medication) {
        try {
            boolean exists = 0 < jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM medication WHERE name = ?",
                    Integer.class,
                    medication.getName()
            );
            if (!exists) {
                jdbcTemplate.update(
                        "INSERT INTO medication (name, unit) VALUES (?, ?)",
                        medication.getName(),
                        medication.getUnit()
                );
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

}
