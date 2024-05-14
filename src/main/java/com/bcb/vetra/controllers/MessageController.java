package com.bcb.vetra.controllers;

import com.bcb.vetra.daos.MessageDao;
import com.bcb.vetra.models.Message;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class MessageController {
    private MessageDao messageDao;

    public MessageController(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @GetMapping("/messages")
    // public List<Message> getAll(Principal principal)
    public List<Message> getAll() {
        return messageDao.getAll();
    }

    @GetMapping("/messages/{messageId}")
    public Message get(@PathVariable int messageId) {
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

    @PostMapping("/messages")
    // public Message create(Principal principal, @RequestBody Message message)
    public Message create(@RequestBody Message message) {
        return messageDao.create(message);
    }

    @PostMapping("/patients/{patientId}/messages")
    public Message createForPatient(Principal principal, @RequestBody Message message, @PathVariable int patientId) {
        message.setFromUsername(principal.getName());
        message.setPatientId(patientId);
        return messageDao.create(message);
    }

    @PostMapping("/patients/{patientId}/tests/{testId}/messages")
    public Message createForTest(Principal principal, @RequestBody Message message, @PathVariable int patientId, @PathVariable int testId) {
        message.setFromUsername(principal.getName());
        message.setPatientId(patientId);
        message.setTestId(testId);
        return messageDao.create(message);
    }

    @PutMapping("/messages/{messageId}")
    public Message update(@RequestBody Message message) {
        return messageDao.update(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public void delete(@PathVariable int messageId) {
        messageDao.delete(messageId);
    }

}
