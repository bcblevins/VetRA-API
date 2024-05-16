package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.models.User;
import com.bcb.vetra.services.ValidateAccess;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * Controller class for the Patient model.
 */

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/patients")
public class PatientController {
    private PatientDao patientDao;
    private UserDao userDao;
    private ValidateAccess validateAccess;
    public PatientController(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.validateAccess = new ValidateAccess(patientDao, userDao);
    }
    @GetMapping
    public List<Patient> getAllByUsername(Principal principal) {
        return patientDao.getPatientsByUsername(principal.getName());
    }
    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/all")
    public List<Patient> getAll() {
        return patientDao.getAllPatients();
    }


    @GetMapping("/{patientId}")
    public Patient get(@PathVariable int patientId, Principal principal) {
        if (!validateAccess.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient.");
        }
        return patientDao.getPatientById(patientId);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return patientDao.create(patient);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/{patientId}")
    public Patient update(@PathVariable int patientId, @RequestBody Patient patient) {
        patient.setPatientId(patientId);
        return patientDao.updatePatient(patient);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{patientId}")
    public void delete(@PathVariable int patientId) {
        patientDao.deletePatient(patientId);
    }
}
