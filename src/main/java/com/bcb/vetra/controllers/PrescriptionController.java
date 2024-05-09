package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PrescriptionDao;
import com.bcb.vetra.models.Prescription;
import com.bcb.vetra.viewmodels.PrescriptionWithMedication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for the Prescription model. Also manipulates the medication table as needed for handling prescription requests.
 */
@RestController
@RequestMapping(path = "/owners")
public class PrescriptionController {
    private PrescriptionDao prescriptionDao;
    public PrescriptionController(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions", method = RequestMethod.GET)
    public List<PrescriptionWithMedication> getAll(@PathVariable int id, @PathVariable int patientId) {
        return prescriptionDao.getPrescriptionsByPatientId(patientId);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.GET)
    public PrescriptionWithMedication get(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId) {
        return prescriptionDao.getPrescriptionWithMedicationById(prescriptionId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions", method = RequestMethod.POST)
    public PrescriptionWithMedication create(@PathVariable int id, @PathVariable int patientId, @RequestBody PrescriptionWithMedication prescription) {
        return prescriptionDao.create(prescription);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.PUT)
    public PrescriptionWithMedication update(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId, @RequestBody PrescriptionWithMedication prescription) {
        prescription.setPrescriptionId(prescriptionId);
        return prescriptionDao.update(prescription);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}/patients/{patientId}/prescriptions/{prescriptionId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int patientId, @PathVariable int prescriptionId) {
        prescriptionDao.delete(prescriptionId);
    }
}
