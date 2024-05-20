package com.bcb.vetra.services;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.models.User;

public class ValidateAccess {
    private UserDao userDao;
    private PatientDao patientDao;
    private MessageDao messageDao;
    private TestDao testDao;

    public ValidateAccess(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    public ValidateAccess(PatientDao patientDao, UserDao userDao, MessageDao messageDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.messageDao = messageDao;
    }

    public ValidateAccess(PatientDao patientDao, UserDao userDao, TestDao testDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.testDao = testDao;
    }


    public boolean canAccessUser(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    public boolean canAccessPatient(int patientId, String username) {
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isOwnedBy || isDoctor || isAdmin;
    }

    public boolean canAccessTest(Test test, String username) {
        int patientId = test.getPatientID();
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isOwnedBy || isDoctor || isAdmin;
    }
    public boolean canAccessResult(Result result, int testId, String username) {
        int patientId = testDao.getTestById(result.getTestID()).getPatientID();
        boolean isPatientOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        boolean isAdmin = userDao.getRoles(username).contains("ADMIN");
        return isPatientOwnedBy || isDoctor || isAdmin;
    }



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
