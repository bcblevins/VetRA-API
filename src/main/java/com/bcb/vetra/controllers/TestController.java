package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.ResultDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.services.MockVmsIntegration;
import com.bcb.vetra.services.AccessControl;
import com.bcb.vetra.services.VmsIntegration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * <strong>Controller for tests.</strong>
 * <br><br>
 * This class is responsible for handling all HTTP requests related to tests.
 */
@PreAuthorize("isAuthenticated()")

@RestController
public class TestController {
    private TestDao testDao;
    private ResultDao resultDao;
    private PatientDao patientDao;
    private UserDao userDao;
    private AccessControl accessControl;
    private VmsIntegration vmsIntegration;
    public TestController(TestDao testDao, UserDao userDao, PatientDao patientDao, ResultDao resultDao) {
        this.testDao = testDao;
        this.resultDao = resultDao;
        this.accessControl = new AccessControl(patientDao, userDao);
        this.vmsIntegration = new MockVmsIntegration(testDao, resultDao);
    }

    @GetMapping("/patients/{patientId}/tests")
    public List<Test> getAll(@PathVariable int patientId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have access to this patient.");
        }
        vmsIntegration.updateDB();
        return testDao.getTestsForPatient(patientId);
    }
    @GetMapping("/patients/{patientId}/tests/{testId}")
    public Test get(@PathVariable int patientId, @PathVariable int testId, Principal principal) {
        Test test = testDao.getTestById(testId);
        if (!accessControl.canAccessTest(test, principal.getName())) {
            return null;
        }
        vmsIntegration.updateDB();
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
