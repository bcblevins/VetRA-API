package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Prescription;
import com.bcb.vetra.viewmodels.PrescriptionWithMedication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class PrescriptionDao {
    private final JdbcTemplate jdbcTemplate;
    public PrescriptionDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public PrescriptionWithMedication getPrescriptionWithMedicationById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT prescription.*, medication.unit " +
                        "FROM prescription " +
                        "JOIN medication ON medication.name = prescription.medication_name " +
                        "WHERE prescription.prescription_id = ?;",
                this::mapToPrescriptionWithMedication,
                id
        );
    }

    public List<PrescriptionWithMedication> getPrescriptionsByPatientId(int id) {
        return jdbcTemplate.query(
                "SELECT prescription.*, medication.unit " +
                        "FROM prescription " +
                        "JOIN medication ON medication.name = prescription.medication_name " +
                        "WHERE prescription.patient_id = ?",
                this::mapToPrescriptionWithMedication,
                id
        );
    }
    public List<PrescriptionWithMedication> getAllPrescriptions() {
        return jdbcTemplate.query(
                "SELECT prescription.*, medication.unit " +
                        "FROM prescription " +
                        "JOIN medication ON medication.name = prescription.medication_name",
                this::mapToPrescriptionWithMedication);
    }
    public PrescriptionWithMedication create(PrescriptionWithMedication prescription) {
        insertMedicationIfDoesNotExist(prescription);
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO prescription (medication_name, quantity, instructions, is_active, patient_id, doctor_id) " +
                        "VALUES (?,?,?,?,?,?) " +
                        "RETURNING prescription_id;",
                Integer.class,
                prescription.getName(),
                prescription.getQuantity(),
                prescription.getInstructions(),
                prescription.isActive(),
                prescription.getPatientId(),
                prescription.getDoctorId()
        );
        return getPrescriptionWithMedicationById(id);
    }
    public PrescriptionWithMedication update(PrescriptionWithMedication prescription) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE prescription SET medication_name = ?, quantity = ?, instructions = ?, is_active = ?, patient_id = ?, doctor_id = ? " +
                        "WHERE prescription_id = ?;",
                prescription.getName(),
                prescription.getQuantity(),
                prescription.getInstructions(),
                prescription.isActive(),
                prescription.getPatientId(),
                prescription.getDoctorId(),
                prescription.getPrescriptionId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPrescriptionWithMedicationById(prescription.getPrescriptionId());
        }
    }
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM prescription WHERE prescription_id = ?", id) > 0;
    }
    //----------------------
    // Helper methods
    //----------------------
    private PrescriptionWithMedication mapToPrescriptionWithMedication(ResultSet resultSet, int rowNumber) throws SQLException {
        return new PrescriptionWithMedication(
                resultSet.getInt("prescription_id"),
                resultSet.getString("medication_name"),
                resultSet.getInt("quantity"),
                resultSet.getString("unit"),
                resultSet.getString("instructions"),
                resultSet.getBoolean("is_active"),
                resultSet.getInt("patient_id"),
                resultSet.getInt("doctor_id")
                );
    }

    private Prescription mapToPrescription(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Prescription(
                resultSet.getInt("prescription_id"),
                resultSet.getString("name"),
                resultSet.getInt("quantity"),
                resultSet.getString("instructions"),
                resultSet.getBoolean("is_active"),
                resultSet.getInt("patient_id"),
                resultSet.getInt("doctor_id")
        );
    }

    public boolean insertMedicationIfDoesNotExist(PrescriptionWithMedication medication) {
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
    }

}
