package com.bcb.vetra.services.vmsintegration;

import com.bcb.vetra.models.Result;
import com.bcb.vetra.models.Test;
import com.bcb.vetra.daos.ResultDao;
import com.bcb.vetra.daos.TestDao;
import com.bcb.vetra.services.vmsintegration.VmsIntegration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Mock VMS integration to be replaced by an external API.
 */
@Component
public class MockVmsIntegration implements VmsIntegration {

    private Map<Test, List<Result>> testMap;
    private TestDao testDao;
    private ResultDao resultDao;
    private boolean isDisabled = true;

    public MockVmsIntegration(TestDao testDao, ResultDao resultDao) {
        testMap = new HashMap<>();
        this.testDao = testDao;
        this.resultDao = resultDao;
    }

    /**
     * Updates the database with new tests. Implemented from VmsIntegration.
     *
     * @return int
     */
    @Override
    public int updateDB() {
        if (isDisabled) {
            return 0;
        }
        int tests = pickTests();
        for (Map.Entry<Test, List<Result>> entry : testMap.entrySet()) {
            int testId = testDao.create(entry.getKey()).getId();
            for (Result result : entry.getValue()) {
                result.setTestID(testId);
                resultDao.create(result);
            }
        }
        if (tests > 0) {
            System.out.println("Added " + tests + " tests to the database.");
            this.isDisabled = true;
        } else {
            System.out.println("No new tests to add.");
        }

        return tests > 0 ? 1 : 0;
    }

    /**
     * Randomly selects a number of tests to generate.
     */
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

    /**
     * Generates a random test.
     */
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

    /**
     * Generates a CBC test.
     */
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

    /**
     * Generates a fecal test.
     */
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
