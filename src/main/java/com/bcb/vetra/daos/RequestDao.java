package com.bcb.vetra.daos;

import com.bcb.vetra.models.Request;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RequestDao {
    private JdbcTemplate jdbcTemplate;

    public RequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Request getRequestById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM request WHERE request_id = ?", this::mapToRequest, id);
    }

    public List<Request> getAllRequests() {
        return jdbcTemplate.query("SELECT * FROM request ORDER BY request_date", this::mapToRequest);
    }

    public List<Request> getRequestsByPatientId(int patientId) {
        return jdbcTemplate.query("SELECT * FROM request WHERE prescription_id = ? ORDER BY request_date", this::mapToRequest, patientId);
    }

    public List<Request> getRequestsByPrescriptionId(int prescriptionId) {
        return jdbcTemplate.query("SELECT * FROM request WHERE prescription_id = ? ORDER BY request_date", this::mapToRequest, prescriptionId);
    }

    /**
     *
     * @param status pending, approved, or denied
     * @return
     */
    public List<Request> getRequestsByStatus(String status) {
        status.toUpperCase();
        return jdbcTemplate.query("SELECT * FROM request WHERE status = ? ORDER BY request_date", this::mapToRequest, status);
    }

    public Request create(Request request) {
        Integer id = jdbcTemplate.queryForObject(
                "INSERT INTO request (prescription_id, status, request_date) " +
                        "VALUES (?,?,?) " +
                        "RETURNING request_id;",
                Integer.class,
                request.getPrescriptionId(),
                request.getStatus(),
                request.getRequestDate()
        );
        return getRequestById(id);
    }

    public Request update(Request request) {
        int rowsAffected = jdbcTemplate.update(
                "UPDATE request SET prescription_id = ?, status = ?, request_date = ? " +
                        "WHERE request_id = ?;",
                request.getPrescriptionId(),
                request.getStatus(),
                request.getRequestDate(),
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
                rs.getString("request_date")
        );

    }
}
