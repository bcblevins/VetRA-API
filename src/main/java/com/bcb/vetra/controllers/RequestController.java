package com.bcb.vetra.controllers;

import com.bcb.vetra.models.Request;
import com.bcb.vetra.viewmodels.RequestWithPrescription;
import org.springframework.web.bind.annotation.*;

import com.bcb.vetra.daos.RequestDao;

import java.util.List;

@RestController
public class RequestController {
    private RequestDao requestDao;

    public RequestController(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @GetMapping("/requests")
    public List<RequestWithPrescription> getAll() {
        return requestDao.getAllRequestsWithPrescription();
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public List<Request> getAllForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId) {
        return requestDao.getRequestsByPrescriptionId(prescriptionId);
    }

    @GetMapping("/requests/{requestId}")
    public RequestWithPrescription get(@PathVariable int requestId) {
        return requestDao.getRequestWithPrescriptionById(requestId);
    }

    @GetMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests/{requestId}")
    public Request getForPrescription(@PathVariable int patientId, @PathVariable int prescriptionId, @PathVariable int requestId) {
        return requestDao.getRequestById(requestId);
    }

    @PostMapping("/requests")
    public Request create(@RequestBody Request request) {
        return requestDao.create(request);
    }

    @PostMapping("patients/{patientId}/prescriptions/{prescriptionId}/requests")
    public Request createForPrescription(@RequestBody Request request, @PathVariable int patientId, @PathVariable int prescriptionId) {
        request.setPrescriptionId(prescriptionId);
        return requestDao.create(request);
    }

    @PutMapping("/requests/{requestId}")
    public Request update(@PathVariable int requestId, @RequestBody Request request) {
        request.setRequestId(requestId);
        return requestDao.update(request);
    }

    @DeleteMapping("/requests/{requestId}")
    public void delete(@PathVariable int requestId) {
        requestDao.delete(requestId);
    }
}
