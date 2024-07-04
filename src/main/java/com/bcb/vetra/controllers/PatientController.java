package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.services.AccessControl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * <strong>Controller for patients.</strong>
 * <br><br>
 * This class is responsible for handling all HTTP requests related to patients.
 */

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/patients")
@CrossOrigin
public class PatientController {
    private PatientDao patientDao;
    private UserDao userDao;
    private AccessControl accessControl;
    public PatientController(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.accessControl = new AccessControl(patientDao, userDao);
    }

    /**
     * Gets all patients for the currently logged in user.
     *
     * @param principal The currently logged in user.
     * @return A list of all patients for the currently logged in user.
     */
    @GetMapping
    public List<Patient> getAllByUsername(Principal principal) {
        return patientDao.getPatientsByUsername(principal.getName());
    }

    /**
     * Gets all patients.
     *
     * @return A list of all patients.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @GetMapping("/all")
    public List<Patient> getAll() {
        return patientDao.getAllPatients();
    }

    /**
     * Gets a patient by its ID. Verifies permission by calling the access control service. If a user owns the patient, or if they are an admin or doctor, they can access the patient.
     *
     * @param patientId The ID of the patient.
     * @param principal The currently logged in user.
     * @return The patient with the given ID.
     */
    @GetMapping("/{patientId}")
    public Patient get(@PathVariable int patientId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient.");
        }

        return patientDao.getPatientById(patientId);
    }

    /**
     * Creates a new patient.
     *
     * @param patient The patient to create.
     * @return The created patient.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Patient create(@Valid @RequestBody Patient patient) {
        return patientDao.create(patient);
    }

    /**
     * Updates a patient.
     *
     * @param patientId The ID of the patient to update.
     * @param patient The updated patient.
     * @return The updated patient.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/{patientId}")
    public Patient update(@PathVariable int patientId, @Valid @RequestBody Patient patient) {
        patient.setPatientId(patientId);
        return patientDao.updatePatient(patient);
    }

    /**
     * Deletes a patient.
     *
     * @param patientId The ID of the patient to delete.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{patientId}")
    public void delete(@PathVariable int patientId) {
        patientDao.deletePatient(patientId);
    }
}
