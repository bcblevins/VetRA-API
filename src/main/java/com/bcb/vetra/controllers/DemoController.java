package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.*;
import com.bcb.vetra.models.*;
import com.bcb.vetra.services.AccessControl;
import com.bcb.vetra.services.MessageNotification;
import com.bcb.vetra.services.vmsintegration.MockVmsIntegration;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import com.bcb.vetra.viewmodels.PrescriptionWithMedication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class DemoController {

    private MessageDao messageDao;
    private UserDao userDao;
    private PatientDao patientDao;
    private PrescriptionDao prescriptionDao;
    private TestDao testDao;
    private ResultDao resultDao;
    private VmsIntegration vmsIntegration;

    public DemoController(MessageDao messageDao, PatientDao patientDao, UserDao userDao, PrescriptionDao prescriptionDao, TestDao testDao, ResultDao resultDao) {
        this.messageDao = messageDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.prescriptionDao = prescriptionDao;
        this.testDao = testDao;
        this.resultDao = resultDao;
        this.vmsIntegration = new MockVmsIntegration(testDao, resultDao);
    }

    @GetMapping(path = "/login/{username}")
    public User getUser(@PathVariable String username) {
        User user = userDao.getUserByUsername(username);
        return user;
    }


    @GetMapping("{username}/patients")
    public List<Patient> getAllByUsername(@PathVariable String username) {
        List<Patient> patients = patientDao.getPatientsByUsername(username);
        return patients;
    }

    @GetMapping("{patientId}/messages")
    public List<Message> getMessagesByPatientId(@PathVariable int patientId) {
        List<Message> messages = messageDao.getMessagesByPatientId(patientId);
        return messages;
    }

    @GetMapping("{patientId}/prescriptions")
    public List<PrescriptionWithMedication> getAllPrescriptions(@PathVariable int patientId) {
        List<PrescriptionWithMedication> prescriptions = prescriptionDao.getPrescriptionsByPatientId(patientId);
        return prescriptions;
    }

    @GetMapping("{patientId}/tests")
    public List<Test> getAllTests(@PathVariable int patientId) {
        vmsIntegration.updateDB();
        List<Test> tests = testDao.getTestsForPatient(patientId);
        return tests;
    }

    @GetMapping("{testId}/results")
    public List<Result> getAll(@PathVariable int testId) {
        List<Result> results = resultDao.getResultsForTest(testId);
        return results;
    }
}

