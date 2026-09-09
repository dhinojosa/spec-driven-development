package com.evolutionnext.jqwik.arbitraries;


public record Patient (PatientId patientId, String firstName, String lastName, BloodType bloodType) {
}
