package com.bcb.vetra.services;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.daos.ResultDao;
import com.bcb.vetra.daos.TestDao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class MockVmsIntegration implements VmsIntegration {

    private Map<Test, List<Result>> testMap;
    private TestDao testDao;
    private ResultDao resultDao;

    public MockVmsIntegration(TestDao testDao, ResultDao resultDao) {
        testMap = new HashMap<>();
        this.testDao = testDao;
        this.resultDao = resultDao;
    }
    @Override
    public int updateDB() {
        int tests = pickTests();
        for (Map.Entry<Test, List<Result>> entry : testMap.entrySet()) {
            int testId = testDao.create(entry.getKey()).getId();
            for (Result result : entry.getValue()) {
                result.setTestID(testId);
                resultDao.create(result);
            }
        }
        return tests;
    }

    private int pickTests() {
        Random rand = new Random();
        int numTests = rand.nextInt(1, 20);
        if (numTests <= 5) {
            for (int i = 0; i < numTests; i++) {
                generateRandomTest(rand, i);
            }
            return numTests;
        }
        return 0;
    }

    private void generateRandomTest(Random rand, int testId) {
        int animalChoice = rand.nextInt(1, 3);
        int testChoice = rand.nextInt(1, 3);
        switch (testChoice) {
            case 1:
                generateCBC(rand, animalChoice, testId);
            case 2:
                generateFecal(rand, animalChoice, testId);
        }
    }

    private void generateCBC(Random rand, int animalChoice, int testId) {
        List<Result> results = new ArrayList<>();

        String wbc = String.valueOf(rand.nextInt(40, 150) / 10.0);
        String rbc = String.valueOf(rand.nextInt(450, 825) / 100.0);
        String hgb = String.valueOf(rand.nextInt(119, 189) / 10.0);
        String hct = String.valueOf(rand.nextInt(20, 75));
        String mcv = String.valueOf(rand.nextInt(60, 85));
        String plt = String.valueOf(rand.nextInt(190, 750));

        results.add(new Result(0, testId, wbc, "WBC", "4.0", "15.5", "10^3/uL"));
        results.add(new Result(0, testId, rbc, "RBC", "4.5", "8.25", "10^6/uL"));
        results.add(new Result(0, testId, hgb, "Hemoglobin", "11.9", "18.9", "g/dL"));
        results.add(new Result(0, testId, hct, "Hematocrit", "36", "60", "%"));
        results.add(new Result(0, testId, mcv, "MCV", "58", "79", "fL"));
        results.add(new Result(0, testId, plt, "Platelets", "170", "400", "10^3/uL"));

        testMap.put(new Test(0, "CBC", animalChoice, "cakelly4", LocalDateTime.now()), results);
    }

    private void generateFecal(Random rand, int animalChoice, int testId) {
        List<Result> results = new ArrayList<>();
        boolean hooks = rand.nextInt(0, 10) > 7;
        boolean rounds = rand.nextInt(0, 10) > 7;
        boolean whips = rand.nextInt(0, 10) > 7;
        boolean tapes = rand.nextInt(0, 10) > 7;

        results.add(new Result(0, testId, hooks ? "Positive" : "Negative", "Hookworms"));
        results.add(new Result(0, testId, rounds ? "Positive" : "Negative", "Roundworms"));
        results.add(new Result(0, testId, whips ? "Positive" : "Negative", "Whipworms"));
        results.add(new Result(0, testId, tapes ? "Positive" : "Negative", "Tapeworms"));

        testMap.put(new Test(0, "Fecal", animalChoice, "cakelly4", LocalDateTime.now()), results);
    }
}
