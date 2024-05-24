package com.bcb.vetra.daos;

import com.bcb.vetra.exception.DaoException;
import com.bcb.vetra.models.Request;
import com.bcb.vetra.viewmodels.RequestWithPrescription;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <strong>Data Access Object for requests.</strong>
 * <br><br>
 * This class is responsible for all database operations related to requests.
 * <br><br>
 * Models: <i>Request, RequestWithPrescription(view model)</i>
 */
@Component
public class RequestDao {
    private JdbcTemplate jdbcTemplate;

    public RequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Request getRequestById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM request WHERE request_id = ?", this::mapToRequest, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Gets a request by ID if it exists for a patient.
     *
     * @param id
     * @param patientId
     * @return Request
     */
    public Request getRequestByIdAndPatientId(int id, int patientId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * " +
                            "FROM request r " +
                            "JOIN prescription p ON p.prescription_id = r.prescription_id " +
                            "WHERE r.request_id = ? AND p.patient_id = ?",
                    this::mapToRequest,
                    id,
                    patientId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Request> getAllRequests() {
        return jdbcTemplate.query("SELECT * FROM request ORDER BY request_date", this::mapToRequest);
    }

    /**
     * Gets all requests with prescription details.
     *
     * @return List of RequestWithPrescription
     */
    public List<RequestWithPrescription> getAllRequestsWithPrescription() {
        return jdbcTemplate.query("SELECT request.*, prescription.* " +
                        "FROM request " +
                        "JOIN prescription ON request.prescription_id = prescription.prescription_id " +
                        "ORDER BY request.request_date;",
                this::mapToRequestWithPrescription);
    }

    /**
     * Gets a request with prescription details by ID.
     *
     * @param id
     * @return RequestWithPrescription
     */
    public RequestWithPrescription getRequestWithPrescriptionById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT request.*, prescription.* " +
                            "FROM request " +
                            "JOIN prescription ON request.prescription_id = prescription.prescription_id " +
                            "WHERE request_id = ?;",
                    this::mapToRequestWithPrescription,
                    id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Request> getRequestsByPatientId(int patientId) {
        return jdbcTemplate.query("SELECT * FROM request WHERE prescription_id = ? ORDER BY request_date", this::mapToRequest, patientId);
    }

    public List<Request> getRequestsByPrescriptionId(int prescriptionId) {
        return jdbcTemplate.query("SELECT * FROM request WHERE prescription_id = ? ORDER BY request_date", this::mapToRequest, prescriptionId);
    }

    /**
     * Gets all requests for a prescription by ID and patient by ID.
     *
     * @param prescriptionId
     * @param patientId
     * @return List of Request
     */
    public List<Request> getRequestsByPrescriptionIdAndPatientId(int prescriptionId, int patientId) {
        return jdbcTemplate.query(
                "SELECT r.*, p.patient_id " +
                        "FROM request r " +
                        "JOIN prescription p ON p.prescription_id = r.prescription_id " +
                        "WHERE r.prescription_id = ? AND p.patient_id = ? ORDER BY r.request_date", this::mapToRequest, prescriptionId, patientId);
    }

    /**
     * @param status pending, approved, or denied
     * @return
     */
    public List<Request> getRequestsByStatus(String status) {
        status.toUpperCase();
        return jdbcTemplate.query("SELECT * FROM request WHERE status = ? ORDER BY request_date", this::mapToRequest, status);
    }

    public Request create(Request request) {
        request.setStatus(request.getStatus().toUpperCase());
        try {
            Integer id = jdbcTemplate.queryForObject(
                    "INSERT INTO request (prescription_id, status) " +
                            "VALUES (?,?) " +
                            "RETURNING request_id;",
                    Integer.class,
                    request.getPrescriptionId(),
                    request.getStatus()
            );
            return getRequestById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("Failed to create request.");
        }
    }

    public Request update(Request request) {
        request.setStatus(request.getStatus().toUpperCase());
        int rowsAffected = jdbcTemplate.update(
                "UPDATE request SET prescription_id = ?, status = ? " +
                        "WHERE request_id = ?;",
                request.getPrescriptionId(),
                request.getStatus(),
                request.getRequestId()
        );
        return getRequestById(request.getRequestId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM request WHERE request_id = ?", id);
    }

    private Request mapToRequest(ResultSet rs, int rowNum) throws SQLException {
        return new Request(
                rs.getInt("request_id"),
                rs.getInt("prescription_id"),
                rs.getString("status"),
                rs.getTimestamp("request_date").toLocalDateTime()
        );

    }

    private RequestWithPrescription mapToRequestWithPrescription(ResultSet rs, int rowNum) throws SQLException {
        return new RequestWithPrescription(
                rs.getInt("request_id"),
                rs.getInt("prescription_id"),
                rs.getInt("patient_id"),
                rs.getString("status"),
                rs.getString("request_date"),
                rs.getString("medication_name"),
                rs.getDouble("quantity"),
                rs.getString("instructions"),
                rs.getInt("refills"),
                rs.getBoolean("is_active"),
                rs.getString("doctor_username")
        );
    }
}
