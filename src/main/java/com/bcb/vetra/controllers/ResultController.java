package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.ResultDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Result;
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
 * <strong>Controller for results.</strong>
 * <br><br>
 * This class is responsible for handling all HTTP requests related to test results.
 */
@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
public class ResultController {

    private TestDao testDao;
    private ResultDao resultDao;
    private PatientDao patientDao;
    private UserDao userDao;
    private AccessControl accessControl;
    private VmsIntegration vmsIntegration;
    public ResultController(ResultDao resultDao, TestDao testDao, UserDao userDao, PatientDao patientDao) {
        this.resultDao = resultDao;
        this.testDao = testDao;
        this.accessControl = new AccessControl(patientDao, userDao, testDao);
        this.vmsIntegration = new MockVmsIntegration(testDao, resultDao);
    }

    /**
     * Gets all results for a test. Verifies permission by calling the access control service.
     *
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     * @param principal The currently logged in user.
     * @return A list of all results for the test.
     */
    @GetMapping("/patients/{patientId}/tests/{testId}/results")
    public List<Result> getAll(@PathVariable int patientId, @PathVariable int testId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient.");
        }
        vmsIntegration.updateDB();
        return resultDao.getResultsForTest(testId);
    }

    /**
     * Gets a result by its ID. Verifies permission by calling the access control service.
     *
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     * @param resultId The ID of the result.
     * @param principal The currently logged in user.
     * @return The result with the given ID.
     */
    @GetMapping("/patients/{patientId}/tests/{testId}/results/{resultId}")
    public Result get(@PathVariable int patientId, @PathVariable int testId, @PathVariable int resultId, Principal principal) {
        Result result = resultDao.getResultById(resultId);
        if (result.getTestID() != testId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Result does not belong to test.");
        }
        if (!accessControl.canAccessResult(result, testId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this result.");
        }
        vmsIntegration.updateDB();
        return result;
    }

    // No create or update methods as I plan to supply the test data from another API.

    /**
     * Deletes a result.
     *
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     * @param resultId The ID of the result.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/patients/{patientId}/tests/{testId}/results/{resultId}")
    public void delete(@PathVariable int patientId, @PathVariable int testId, @PathVariable int resultId) {
        testDao.deleteTestOfPatient(testId, patientId);
    }

}
