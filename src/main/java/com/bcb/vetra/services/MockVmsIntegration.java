package com.bcb.vetra.services;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;

import java.util.List;

public class MockVmsIntegration implements VmsIntegration{
    private List<Test> tests;
    private List<Result> results;
    @Override
    public List<Test> getTest() {
        return tests;
    }

    @Override
    public List<Result> getResults() {
        return results;
    }

    public void randomTestSelector() {
    }
}
