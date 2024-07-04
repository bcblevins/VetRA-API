package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.*;
import com.bcb.vetra.models.*;
import com.bcb.vetra.services.AccessControl;
import com.bcb.vetra.services.MessageNotification;
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

    public DemoController(MessageDao messageDao, PatientDao patientDao, UserDao userDao, PrescriptionDao prescriptionDao, TestDao testDao, ResultDao resultDao) {
        this.messageDao = messageDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.prescriptionDao = prescriptionDao;
        this.testDao = testDao;
        this.resultDao = resultDao;
    }

    @GetMapping(path = "/login/{username}")
    public User getUser(@PathVariable String username) {
        return userDao.getUserByUsername(username);
    }


    @GetMapping("{username}/patients")
    public List<Patient> getAllByUsername(@PathVariable String username) {
        return patientDao.getPatientsByUsername(username);
    }

    @GetMapping("{patientId}/messages")
    public List<Message> getMessagesByPatientId(@PathVariable int patientId) {
        return messageDao.getMessagesByPatientId(patientId);
    }

    @GetMapping("{patientId}/prescriptions")
    public List<PrescriptionWithMedication> getAllPrescriptions(@PathVariable int patientId) {
        return prescriptionDao.getPrescriptionsByPatientId(patientId);
    }

    @GetMapping("{patientId}/tests")
    public List<Test> getAllTests(@PathVariable int patientId) {
        return testDao.getTestsForPatient(patientId);
    }

    @GetMapping("{testId}/results")
    public List<Result> getAll(@PathVariable int testId) {
        return resultDao.getResultsForTest(testId);
    }
}

