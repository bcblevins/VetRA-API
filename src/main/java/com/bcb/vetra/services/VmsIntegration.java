package com.bcb.vetra.services;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;

import java.util.List;
/**
 * <strong>Interface for the VMS integration service</strong>
 * This service is responsible for checking for new tests from the Veterinary Management System (VMS) and updating the database with them
 */
public interface VmsIntegration {

    /**
     * Checks for new tests from the VMS and updates the database with them
     * @return the number of tests added to the database
     */
    public int updateDB();
}
