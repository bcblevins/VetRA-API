package com.bcb.vetra.services.vmsintegration;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;

import java.util.List;
/**
 * <strong>Interface for the VMS integration service</strong>
 * This service is responsible for checking for new tests and patients from the Veterinary Management System (VMS) and updating the database with them.
 */
public interface VmsIntegration {

    /**
     * Checks for new tests from the VMS and updates the database with them
     * @return codes: 1 - success, 0 - failure
     */
    public int updateDB();
}
