package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.*;
import com.bcb.vetra.models.Notification;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.services.vmsintegration.MockVmsIntegration;
import com.bcb.vetra.services.AccessControl;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
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
@CrossOrigin
public class TestController {
    private TestDao testDao;
    private ResultDao resultDao;
    private PatientDao patientDao;
    private UserDao userDao;
    private AccessControl accessControl;
    private VmsIntegration vmsIntegration;
    public TestController(TestDao testDao, UserDao userDao, PatientDao patientDao, ResultDao resultDao, NotificationDao notificationDao) {
        this.testDao = testDao;
        this.resultDao = resultDao;
        this.accessControl = new AccessControl(patientDao, userDao);
        this.vmsIntegration = new MockVmsIntegration(testDao, resultDao);
    }

    /**
     * Gets all tests for a patient. Verifies permission by calling the access control service.
     *
     * @param patientId The ID of the patient.
     * @param principal The currently logged-in user.
     * @return A list of all tests for the patient.
     */
    @GetMapping("/patients/{patientId}/tests")
    public List<Test> getAll(@PathVariable int patientId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not have access to this patient.");
        }
        vmsIntegration.updateDB();
        return testDao.getTestsForPatient(patientId);
    }

    /**
     * Gets a test by its ID. Verifies permission by calling the access control service.
     *
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     * @param principal The currently logged in user.
     * @return The test with the given ID.
     */
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

    /**
     * Deletes a test.
     *
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/patients/{patientId}/tests/{testId}")
    public void delete(@PathVariable int patientId, @PathVariable int testId) {
        testDao.deleteTestOfPatient(testId, patientId);
    }

}
