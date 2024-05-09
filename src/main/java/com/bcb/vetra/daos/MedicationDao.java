package com.bcb.vetra.daos;

import com.bcb.vetra.viewmodels.PrescriptionWithMedication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class MedicationDao {
    private final JdbcTemplate jdbcTemplate;

    public MedicationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean insertMedicationIfDoesntExist(PrescriptionWithMedication medication) {
        boolean exists = 0 < jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM medication WHERE name = ?",
                Integer.class,
                medication.getName()
        );
        if (!exists) {
            jdbcTemplate.update(
                    "INSERT INTO medication (name, unit) VALUES (?, ?)",
                    medication.getName(),
                    medication.getUnit()
            );
            return true;
        }
        return false;
    }
}
