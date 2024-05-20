package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.services.ValidateAccess;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * Controller class for the Test model.
 */
@PreAuthorize("isAuthenticated()")

@RestController
public class TestController {
    private TestDao testDao;
    private PatientDao patientDao;
    private UserDao userDao;
    private ValidateAccess validateAccess;
    public TestController(TestDao testDao, UserDao userDao, PatientDao patientDao) {
        this.testDao = testDao;
        this.validateAccess = new ValidateAccess(patientDao, userDao);
    }

    @GetMapping("/patients/{patientId}/tests")
    public List<Test> getAll(@PathVariable int patientId, Principal principal) {
        if (!validateAccess.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have access to this patient.");
        }
        return testDao.getTestsForPatient(patientId);
    }
    @GetMapping("/patients/{patientId}/tests/{testId}")
    public Test get(@PathVariable int patientId, @PathVariable int testId, Principal principal) {
        Test test = testDao.getTestById(testId);
        if (!validateAccess.canAccessTest(test, principal.getName())) {
            return null;
        }
        return test;
    }

    // No create or update methods as I plan to supply the test data from another API.

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/patients/{patientId}/tests/{testId}")
    public void delete(@PathVariable int patientId, @PathVariable int testId) {
        testDao.deleteTestOfPatient(testId, patientId);
    }

}
