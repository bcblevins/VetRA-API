package com.bcb.vetra.services;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;

import java.util.List;

public interface VmsIntegration {
    public List<Test> getTest();
    public List<Result> getResults();
}
