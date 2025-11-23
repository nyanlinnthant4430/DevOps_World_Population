package com.napier.devops.feature_policymaker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportCountryTest {

    @Test
    void testReportThrowsWhenConnectionIsNull() {
        ReportCountry r = new ReportCountry();

        assertThrows(NullPointerException.class, () -> {
            r.generateReport(null);   // con is null → expected NPE
        });
    }
}
