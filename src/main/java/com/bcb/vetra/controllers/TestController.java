package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.viewmodels.TestParameterResults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/owner")
public class TestController {
    private TestDao testDao;
    public TestController(TestDao testDao) {
        this.testDao = testDao;
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/tests")
    public List<TestParameterResults> getAll(int id, int patientId) {
        return testDao.getTestParameterResultsForPatient(patientId);
    }
}
