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
@RequestMapping("/patients")
public class PatientController {
    private PatientDao patientDao;
    public PatientController(PatientDao patientDao) {
        this.patientDao = patientDao;
    }
    @GetMapping
    public List<Patient> getAll() {
        return patientDao.getAllPatients();
    }
    @GetMapping("/{patientId}")
    public Patient get(@PathVariable int patientId) {
        return patientDao.getPatientById(patientId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return patientDao.create(patient);
    }
    @PutMapping("/{patientId}")
    public Patient update(@PathVariable int patientId, @RequestBody Patient patient) {
        patient.setPatientId(patientId);
        return patientDao.updatePatient(patient);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{patientId}")
    public void delete(@PathVariable int patientId) {
        patientDao.deletePatient(patientId);
    }
}
