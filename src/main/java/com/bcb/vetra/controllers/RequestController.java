package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PrescriptionDao;
import com.bcb.vetra.models.Request;
import com.bcb.vetra.viewmodels.RequestWithPrescription;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bcb.vetra.daos.RequestDao;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class RequestController {
    private RequestDao requestDao;
    private PrescriptionDao prescriptionDao;

    public RequestController(RequestDao requestDao, PrescriptionDao prescriptionDao) {
        this.requestDao = requestDao;
        this.prescriptionDao = prescriptionDao;
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/requests")
    public List<RequestWithPrescription> getAll() {
        return requestDao.getAllRequestsWithPrescription();
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public List<Request> getAllForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId) {
        return requestDao.getRequestsByPrescriptionIdAndPatientId(prescriptionId, patientId);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/requests/{requestId}")
    public RequestWithPrescription get(@PathVariable int requestId) {
        return requestDao.getRequestWithPrescriptionById(requestId);
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests/{requestId}")
    public Request getForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId, @PathVariable int requestId) {
        return requestDao.getRequestByIdAndPatientId(requestId, patientId);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests")
    public Request create(@RequestBody Request request) {
        return requestDao.create(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public Request createForPrescription(@RequestBody Request request, @PathVariable int patientId, @PathVariable int prescriptionId) {
        if (prescriptionDao.getPrescriptionWithMedicationById(prescriptionId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prescription does not exist for patient");
        }
        request.setPrescriptionId(prescriptionId);
        return requestDao.create(request);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/requests/{requestId}")
    public Request update(@PathVariable int requestId, @RequestBody Request request) {
        request.setRequestId(requestId);
        return requestDao.update(request);
    }

    @PreAuthorize("hasAuthority('ADMIN'")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/requests/{requestId}")
    public void delete(@PathVariable int requestId) {
        requestDao.delete(requestId);
    }
}
