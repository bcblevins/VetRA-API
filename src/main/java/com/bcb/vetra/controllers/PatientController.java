package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.models.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/owners")
public class PatientController {
    private PatientDao patientDao;
    public PatientController(PatientDao patientDao) {
        this.patientDao = patientDao;
    }
    @RequestMapping(path = "/{id}/patients", method = RequestMethod.GET)
    public List<Patient> getAll(@PathVariable int id) {
        return patientDao.getPatientsByOwnerId(id);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}", method = RequestMethod.GET)
    public Patient get(@PathVariable int id, @PathVariable int patientId) {
        return patientDao.getPatientById(patientId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{id}/patients", method = RequestMethod.POST)
    public Patient create(@PathVariable int id, @RequestBody Patient patient) {
        return patientDao.create(patient);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}", method = RequestMethod.PUT)
    public Patient update(@PathVariable int id, @PathVariable int patientId, @RequestBody Patient patient) {
        return patientDao.update(patient);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}/patients/{patientId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int patientId) {
        patientDao.delete(patientId);
    }
}
