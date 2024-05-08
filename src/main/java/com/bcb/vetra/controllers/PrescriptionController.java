package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PrescriptionDao;
import com.bcb.vetra.models.Prescription;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/owners")
public class PrescriptionController {
    private PrescriptionDao prescriptionDao;
    public PrescriptionController(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions", method = RequestMethod.GET)
    public List<Prescription> getAll(@PathVariable int id, @PathVariable int patientId) {
        return prescriptionDao.getPrescriptionsByPatientId(patientId);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.GET)
    public Prescription get(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId) {
        return prescriptionDao.getPrescriptionById(prescriptionId);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions", method = RequestMethod.POST)
    public Prescription create(@PathVariable int id, @PathVariable int patientId, Prescription prescription) {
        return prescriptionDao.create(prescription);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.PUT)
    public Prescription update(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId, Prescription prescription) {
        prescription.setPrescriptionId(prescriptionId);
        return prescriptionDao.update(prescription);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId) {
        prescriptionDao.delete(prescriptionId);
    }
}
