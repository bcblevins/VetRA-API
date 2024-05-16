package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.viewmodels.TestWithDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for the TestWithDetails view model.
 */
@PreAuthorize("isAuthenticated()")

@RestController
public class TestWithDetailsController {
    private TestDao testDao;
    public TestWithDetailsController(TestDao testDao) {
        this.testDao = testDao;
    }
    @GetMapping("/patients/{patientId}/tests")
    public List<TestWithDetails> getAll(@PathVariable int patientId) {
        return testDao.getTestWithDetailsForPatient(patientId);
    }
    @GetMapping("/patients/{patientId}/tests/{testId}")
    public TestWithDetails get(@PathVariable int patientId, @PathVariable int testId) {
        return testDao.getTestWithDetailsByTestId(testId);
    }

    // No create or update methods yet, as I plan to supply the test data from another API.

    @DeleteMapping("/patients/{patientId}/tests/{testId}")
    public void delete(@PathVariable int patientId, @PathVariable int testId) {
        testDao.deleteTestOfPatient(testId, patientId);
    }

}
