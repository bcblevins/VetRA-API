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
public class PrescriptionController {
    private PrescriptionDao prescriptionDao;
    public PrescriptionController(PrescriptionDao prescriptionDao) {
        this.prescriptionDao = prescriptionDao;
    }
    @GetMapping("/patients/{patientId}/prescriptions")
    public List<PrescriptionWithMedication> getAll(@PathVariable int patientId) {
        return prescriptionDao.getPrescriptionsByPatientId(patientId);
    }
    @GetMapping("/patients/{patientId}/prescriptions/{prescriptionId}")
    public PrescriptionWithMedication get(@PathVariable int patientId, @PathVariable int prescriptionId) {
        return prescriptionDao.getPrescriptionWithMedicationById(prescriptionId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/prescriptions")
    public PrescriptionWithMedication create(@PathVariable int patientId, @RequestBody PrescriptionWithMedication prescription) {
        prescription.setPrescriptionId(patientId);
        return prescriptionDao.create(prescription);
    }
    @PutMapping("/patients/{patientId}/prescriptions/{prescriptionId}")
    public PrescriptionWithMedication update(@PathVariable int patientId, @PathVariable int prescriptionId, @RequestBody PrescriptionWithMedication prescription) {
        prescription.setPrescriptionId(prescriptionId);
        return prescriptionDao.update(prescription);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/patients/{patientId}/prescriptions/{prescriptionId}")
    public void delete(@PathVariable int patientId, @PathVariable int prescriptionId) {
        prescriptionDao.deletePrescriptionOfPet(prescriptionId, patientId);
    }
}
