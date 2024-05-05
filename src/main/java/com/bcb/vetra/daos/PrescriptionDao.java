package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.models.Prescription;
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
    public Prescription getPrescriptionById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM prescription WHERE prescription_id = ?", this::mapToPrescription, id);
    }
    public List<Prescription> getPrescriptionsByPatientId(int id) {
        return jdbcTemplate.query("SELECT * FROM prescription WHERE patient_id = ?", this::mapToPrescription, id);
    }
    public List<Prescription> getAllPrescriptions() {
        return jdbcTemplate.query("SELECT * FROM prescription", this::mapToPrescription);
    }
    public Prescription create(Prescription prescription) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO prescription (name, quantity, instructions, is_active, patient_id, doctor_id) " +
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
        return getPrescriptionById(id);
    }
    public Prescription update(Prescription prescription) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE prescription SET name = ?, quantity = ?, instructions = ?, is_active = ?, patient_id = ?, doctor_id = ? " +
                        "WHERE prescription_id = ?;",
                prescription.getName(),
                prescription.getQuantity(),
                prescription.getInstructions(),
                prescription.isActive(),
                prescription.getPatientId(),
                prescription.getDoctorId(),
                prescription.getId()
        );
        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one.");
        } else {
            return getPrescriptionById(prescription.getId());
        }
    }
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM prescription WHERE prescription_id = ?", id) > 0;
    }
    private Prescription mapToPrescription(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Prescription(
                resultSet.getInt("prescription_id"),
                resultSet.getString("name"),
                resultSet.getString("quantity"),
                resultSet.getString("instructions"),
                resultSet.getBoolean("is_active"),
                resultSet.getInt("patient_id"),
                resultSet.getInt("doctor_id")
                );
    }}
