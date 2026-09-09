package com.evolutionnext.jqwik.arbitraries;


/**
 * Represents a patient ID that must follow the social security number format: {3}-{2}-{4}
 * Example: 123-45-6789
 */
public record PatientId(String id) {
    public PatientId {
        if (id == null || !id.matches("\\d{3}-\\d{2}-\\d{4}")) {
            throw new IllegalArgumentException("Patient ID must be in format: XXX-XX-XXXX where X is a digit");
        }
    }
}
