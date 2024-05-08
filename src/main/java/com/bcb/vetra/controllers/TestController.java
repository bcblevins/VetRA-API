package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.viewmodels.TestWithDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//TODO: This class don't work :(

@RestController
@RequestMapping(path = "/owners")
public class TestController {
    private TestDao testDao;
    public TestController(TestDao testDao) {
        this.testDao = testDao;
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/tests", method = RequestMethod.GET)
    public List<TestWithDetails> getAll(@PathVariable int id, @PathVariable int patientId) {
        return testDao.getTestWithDetailsForPatient(patientId);
    }
    @RequestMapping(path = "/{id}/patients/{patientId}/tests/{testId}", method = RequestMethod.GET)
    public TestWithDetails get(@PathVariable int id, @PathVariable int patientId, @PathVariable int testId) {
        return testDao.getTestWithDetailsByTestId(testId);
    }

    // No create or update methods yet, as I plan to supply the test data from another API.

    @RequestMapping(path = "/{id}/patients/{patientId}/tests/{testId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id, @PathVariable int patientId, @PathVariable int testId) {
        testDao.delete(testId);
    }

}
