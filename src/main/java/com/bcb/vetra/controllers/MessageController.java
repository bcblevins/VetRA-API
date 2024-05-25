package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.daos.PatientDao;
import com.bcb.vetra.daos.UserDao;
import com.bcb.vetra.models.Message;
import com.bcb.vetra.services.MessageNotification;
import com.bcb.vetra.services.AccessControl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * <strong>Controller for messages.</strong>
 * <br><br>
 * This class is responsible for handling all HTTP requests related to messages.
 */

@PreAuthorize("isAuthenticated()")
@RestController
public class MessageController {
    private MessageDao messageDao;
    private UserDao userDao;
    private PatientDao patientDao;
    private AccessControl accessControl;
    private MessageNotification messageNotification;

    public MessageController(MessageDao messageDao, PatientDao patientDao, UserDao userDao) {
        this.messageDao = messageDao;
        this.patientDao = patientDao;
        this.userDao = userDao;
        this.accessControl = new AccessControl(patientDao, userDao, messageDao);
        this.messageNotification = new MessageNotification();
    }

    /**
     * Gets all message sent to or from user.
     *
     * @param principal The currently logged in user.
     * @return A list of all messages for the currently logged in user.
     */
    @GetMapping("/messages")
    public List<Message> getAll(Principal principal) {
        return messageDao.getMessagesByUsername(principal.getName());
    }

    /**
     * Gets a message by its ID.
     *
     * @param messageId The ID of the message.
     * @param principal The currently logged in user.
     * @return The message with the given ID.
     */
    @GetMapping("/messages/{messageId}")
    public Message get(@PathVariable int messageId, Principal principal) {
        return messageDao.getMessageByIdAndUsername(messageId, principal.getName());
    }

    /**
     * Gets all messages
     * @return A list of all messages.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/messages/all")
    public List<Message> getAll() {
        return messageDao.getAll();
    }

    /**
     * Gets a message by its ID.
     *
     * @param messageId The ID of the message.
     * @return The message with the given ID.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/messages/all/{messageId}")
    public Message getFromAll(@PathVariable int messageId) {
        return messageDao.getMessageById(messageId);
    }

    /**
     * Gets all messages about a patient.
     * @param patientId
     * @return
     */
    @GetMapping("/patients/{patientId}/messages")
    public List<Message> getMessagesByPatientId(@PathVariable int patientId) {
        return messageDao.getMessagesByPatientId(patientId);
    }

    /**
     * Gets all messages about a test.
     * @param patientId
     * @param testId
     * @return
     */
    @GetMapping("patients/{patientId}/tests/{testId}/messages")
    public List<Message> getMessagesByTestId(@PathVariable int patientId, @PathVariable int testId) {
        return messageDao.getMessagesByTestId(testId);
    }

    /**
     * Creates a new message attributed to a specific patient.
     *
     * @param message The message to create.
     * @param principal The currently logged in user.
     * @return The created message.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/messages")
    public Message createForPatient(@Valid @RequestBody Message message, @PathVariable int patientId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient");
        }
        message.setPatientId(patientId);
        message.setFromUsername(principal.getName());

        //Send email notification
        String toEmail = userDao.getUserByUsername(message.getToUsername()).getEmail();
        messageNotification.sendEmail(toEmail, message, false);

        return messageDao.create(message);
    }

    /**
     * Creates a new message attributed to a specific test.
     *
     * @param message The message to create.
     * @param patientId The ID of the patient.
     * @param testId The ID of the test.
     * @param principal The currently logged in user.
     * @return The created message.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/tests/{testId}/messages")
    public Message createForTest(@Valid @RequestBody Message message, @PathVariable int patientId, @PathVariable int testId, Principal principal) {
        if (!accessControl.canAccessPatient(patientId, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this patient");
        }
        message.setPatientId(patientId);
        message.setTestId(testId);
        message.setFromUsername(principal.getName());

        //Send email notification
        String toEmail = userDao.getUserByUsername(message.getToUsername()).getEmail();
        messageNotification.sendEmail(toEmail, message, false);

        return messageDao.create(message);
    }

    /**
     * Updates a message.
     * @param messageId
     * @param message
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR')")
    @PutMapping("/messages/all/{messageId}")
    public Message update(@PathVariable int messageId, @Valid @RequestBody Message message) {
        if (!accessControl.canAccessMessage(messageId, message.getFromUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this message");
        }
        message.setMessageId(messageId);
        return messageDao.update(message);
    }

    /**
     * Deletes a message.
     * @param messageId
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/messages/all/{messageId}")
    public void delete(@PathVariable int messageId) {
        messageDao.delete(messageId);
    }

}
