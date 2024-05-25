package com.bcb.vetra.services;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;

/**
 * <strong>Access Control Service</strong>
 * <br><br>
 * This class is responsible for providing more specific access control to resources based on user roles.
 */
public class AccessControl {
    private UserDao userDao;
    private PatientDao patientDao;
    private MessageDao messageDao;
    private TestDao testDao;

    public AccessControl(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    public AccessControl(PatientDao patientDao, UserDao userDao, MessageDao messageDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    public AccessControl(PatientDao patientDao, UserDao userDao, TestDao testDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.testDao = testDao;
    }


    public boolean canAccessUser(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    /**
     * Checks if a user can access a patient.
     *
     * @param patientId
     * @param username
     * @return boolean
     */
    public boolean canAccessPatient(int patientId, String username) {
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isOwnedBy || isDoctor || isAdmin;
    }

    /**
     * Checks if a user can access a test.
     *
     * @param test
     * @param username
     * @return boolean
     */
    public boolean canAccessTest(Test test, String username) {
        int patientId = test.getPatientID();
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isOwnedBy || isDoctor || isAdmin;
    }

    /**
     * Checks if a user can access a result.
     *
     * @param result
     * @param testId
     * @param username
     * @return boolean
     */
    public boolean canAccessResult(Result result, int testId, String username) {
        int patientId = testDao.getTestById(result.getTestID()).getPatientID();
        boolean isPatientOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isPatientOwnedBy || isDoctor || isAdmin;
    }

    /**
     * Checks if a user can access a message.
     *
     * @param messageId
     * @param username
     * @return boolean
     */
    public boolean canAccessMessage(int messageId, String username) {
        boolean isOwnedBy = messageDao.getMessageByIdAndUsername(messageId, username) != null;
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");

        return isOwnedBy || isAdmin;
    }

    public boolean isAdmin(String username) {
        return userDao.getRoles(username).contains("ADMIN");
    }

    public boolean isDoctorOrAdmin(String username) {
        return userDao.getRoles(username).contains("DOCTOR") || userDao.getRoles(username).contains("ADMIN");
    }
}
