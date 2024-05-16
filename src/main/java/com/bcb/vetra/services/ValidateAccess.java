package com.bcb.vetra.services;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.models.User;
import com.bcb.vetra.viewmodels.TestWithDetails;

public class ValidateAccess {
    private UserDao userDao;
    private PatientDao patientDao;
    private MessageDao messageDao;

    public ValidateAccess(UserDao userDao, PatientDao patientDao, MessageDao messageDao) {
        this.userDao = userDao;
        this.patientDao = patientDao;
        this.messageDao = messageDao;
    }
    public ValidateAccess(PatientDao patientDao, UserDao userDao) {
        this.patientDao = patientDao;
        this.userDao = userDao;
    }

    public boolean canAccessUser(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    public boolean canAccessPatient(int patientId, String username) {
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        return isOwnedBy || isDoctor;
    }

    public boolean canAccessTest(TestWithDetails test, String username) {
        int patientId = test.getPatientID();
        boolean isOwnedBy = patientDao.getPatientByIdAndOwner(patientId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("DOCTOR");
        return isOwnedBy || isDoctor;
    }

    public boolean canAccessMessage(int messageId, String username) {
        boolean isOwnedBy = messageDao.getMessageByIdAndUsername(messageId, username) != null;
        boolean isDoctor = userDao.getRoles(username).contains("ADMIN");
        return isOwnedBy || isDoctor;
    }
}
