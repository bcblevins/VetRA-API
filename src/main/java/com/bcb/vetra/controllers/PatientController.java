package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.models.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for the Patient model.
 */
@RestController
public class PatientController {
    private PatientDao patientDao;
    public PatientController(PatientDao patientDao) {
        this.patientDao = patientDao;
    }
    @RequestMapping(path = "/patients", method = RequestMethod.GET)
    public List<Patient> getAll() {
        return patientDao.getAllPatients();
    }
    @RequestMapping(path = "patients/{patientId}", method = RequestMethod.GET)
    public Patient get(@PathVariable int patientId) {
        return patientDao.getPatientById(patientId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{username}/patients", method = RequestMethod.POST)
    public Patient create(@PathVariable String username, @RequestBody Patient patient) {
        return patientDao.create(patient);
    }
    @RequestMapping(path = "/{username}/patients/{patientId}", method = RequestMethod.PUT)
    public Patient update(@PathVariable String username, @PathVariable int patientId, @RequestBody Patient patient) {
        patient.setPatientId(patientId);
        return patientDao.updatePatientOfOwner(patient, username);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{username}/patients/{patientId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String username, @PathVariable int patientId) {
        patientDao.deletePatient(patientId);
    }
}
