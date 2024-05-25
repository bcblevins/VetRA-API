package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.PrescriptionDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Request;
import com.bcb.vetra.services.AccessControl;
import com.bcb.vetra.viewmodels.RequestWithPrescription;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bcb.vetra.daos.RequestDao;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * <strong>Controller for requests.</strong>
 * <br><br>
 * This class is responsible for handling all HTTP requests related to requests.
 */
@PreAuthorize("isAuthenticated()")
@RestController
public class RequestController {
    private RequestDao requestDao;
    private PrescriptionDao prescriptionDao;
    private PatientDao patientDao;
    private UserDao userDao;
    private AccessControl accessControl;

    public RequestController(RequestDao requestDao, PrescriptionDao prescriptionDao, PatientDao patientDao, UserDao userDao) {
        this.requestDao = requestDao;
        this.prescriptionDao = prescriptionDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.accessControl = new AccessControl(patientDao, userDao);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/requests")
    public List<RequestWithPrescription> getAll() {
        return requestDao.getAllRequestsWithPrescription();
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public List<Request> getAllForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId, Principal principal ) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient.");
        }

        return requestDao.getRequestsByPrescriptionIdAndPatientId(prescriptionId, patientId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/requests/{requestId}")
    public RequestWithPrescription get(@PathVariable int requestId) {
        return requestDao.getRequestWithPrescriptionById(requestId);
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests/{requestId}")
    public Request getForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId, @PathVariable int requestId) {
        return requestDao.getRequestByIdAndPatientId(requestId, patientId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests")
    public Request create(@Valid @RequestBody Request request) {
        return requestDao.create(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public Request createForPrescription(@RequestBody Request request, @PathVariable int patientId, @PathVariable int prescriptionId) {
        if (prescriptionDao.getPrescriptionById(prescriptionId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prescription does not exist for patient");
        }
        request.setPrescriptionId(prescriptionId);
        request.setStatus("PENDING");
        return requestDao.create(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/requests/{requestId}")
    public Request update(@PathVariable int requestId, @Valid @RequestBody Request request) {
        request.setRequestId(requestId);
        return requestDao.update(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/requests/{requestId}")
    public void delete(@PathVariable int requestId) {
        requestDao.delete(requestId);
    }
}
