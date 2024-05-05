package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/owners")
public class PatientController {
    private PatientDao patientDao;
    public PatientController(PatientDao patientDao) {
        this.patientDao = patientDao;
    }
    
}
