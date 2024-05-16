package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Message;
import com.bcb.vetra.models.Patient;
import com.bcb.vetra.services.ValidateAccess;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class MessageController {
    private MessageDao messageDao;
    private UserDao userDao;
    private PatientDao patientDao;
    private ValidateAccess validateAccess;

    public MessageController(MessageDao messageDao, PatientDao patientDao, UserDao userDao) {
        this.messageDao = messageDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.validateAccess = new ValidateAccess(userDao, patientDao, messageDao);
    }

    @GetMapping("/messages")
    public List<Message> getAll(Principal principal) {
        return messageDao.getMessagesByUsername(principal.getName());
    }

    @GetMapping("/messages/{messageId}")
    public Message get(@PathVariable int messageId, Principal principal) {
        return messageDao.getMessageByIdAndUsername(messageId, principal.getName());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allMessages")
    public List<Message> getAll() {
        return messageDao.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/allMessages/{messageId}")
    public Message getFromAll(@PathVariable int messageId) {
        return messageDao.getMessageById(messageId);
    }

    @GetMapping("/patients/{patientId}/messages")
    public List<Message> getMessagesByPatientId(@PathVariable int patientId) {
        return messageDao.getMessagesByPatientId(patientId);
    }

    @GetMapping("patients/{patientId}/tests/{testId}/messages")
    public List<Message> getMessagesByTestId(@PathVariable int patientId, @PathVariable int testId) {
        return messageDao.getMessagesByTestId(testId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/messages")
    public Message createForPatient(@RequestBody Message message, @PathVariable int patientId, Principal principal) {
        if (!validateAccess.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient");
        }
        message.setPatientId(patientId);
        message.setFromUsername(principal.getName());
        return messageDao.create(message);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/tests/{testId}/messages")
    public Message createForTest(@RequestBody Message message, @PathVariable int patientId, @PathVariable int testId, Principal principal) {
        if (!validateAccess.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient");
        }
        message.setPatientId(patientId);
        message.setTestId(testId);
        return messageDao.create(message);
    }

    @PreAuthorize("hasAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/messages/{messageId}")
    public Message update(@PathVariable int messageId, @RequestBody Message message) {
        if (!validateAccess.canAccessMessage(messageId, message.getFromUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this message");
        }
        message.setMessageId(messageId);
        return messageDao.update(message);
    }

    @PreAuthorize("hasAuthority('ADMIN'")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/messages/{messageId}")
    public void delete(@PathVariable int messageId) {
        messageDao.delete(messageId);
    }

}
