package com.evolutionnext.jqwik.arbitraries;

import net.datafaker.Faker;
import net.jqwik.api.*;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;

/**
 * This class demonstrates the frequency distribution of data generation
 * using jqwik's property-based testing framework. It specifically shows
 * how to create and test data with different occurrence probabilities.
 */
public class FrequencyTest {


    /**
     * Creates an Arbitrary that generates blood types with realistic frequency distribution.
     * Distribution follows approximate real-world percentages:
     * A+ (30%), A- (6%), B+ (8%), B- (2%), O+ (38%), O- (7%), AB+ (7%), AB- (2%)
     *
     * @return Arbitrary that generates BloodType values with specified frequencies
     */
    Arbitrary<BloodType> bloodTypeArbitrary() {
        return Arbitraries.frequency(
            Tuple.of(30, BloodType.A_POSITIVE),  // ~30%
            Tuple.of(6, BloodType.A_NEGATIVE),   // ~6%
            Tuple.of(8, BloodType.B_POSITIVE),   // ~8%
            Tuple.of(2, BloodType.B_NEGATIVE),   // ~2%
            Tuple.of(38, BloodType.O_POSITIVE),  // ~38%
            Tuple.of(7, BloodType.O_NEGATIVE),   // ~7%
            Tuple.of(7, BloodType.AB_POSITIVE),  // ~7%
            Tuple.of(2, BloodType.AB_NEGATIVE)   // ~2%
        );
    }

    /**
     * Creates an Arbitrary that generates patient IDs in the format "XXX-XX-XXXX".
     * The format consists of three number groups:
     * - First group: 3 digits (001-999)
     * - Second group: 2 digits (01-99)
     * - Third group: 4 digits (0001-9999)
     *
     * @return Arbitrary that generates formatted PatientId values
     */
    Arbitrary<PatientId> patientIdArbitrary() {
        var firstSet = Arbitraries.integers().between(1, 999);
        var secondSet = Arbitraries.integers().between(1, 99);
        var thirdSet = Arbitraries.integers().between(1, 9999);
        return Combinators.combine(firstSet, secondSet, thirdSet).as((i, j, k) ->
            new PatientId(String.format("%03d-%02d-%04d", i, j, k))
        );
    }


    /**
     * Provides an Arbitrary that generates complete Patient objects.
     * Combines patient ID, first name, last name, and blood type to create
     * realistic patient data. Uses Faker to generate random names.
     *
     * @return Arbitrary that generates Patient objects with all required fields
     */
    @Provide("patientArbitrary")
    Arbitrary<Patient> patientArbitrary() {
        Faker faker = new Faker();
        return Combinators.combine(
            patientIdArbitrary(),
            Arbitraries.create(() -> faker.name().firstName()),
            Arbitraries.create(() -> faker.name().lastName()),
            bloodTypeArbitrary()
        ).as(Patient::new);
    }

    /**
     * Tests the frequency distribution of generated blood types.
     * Collects statistics on blood type distribution and displays them
     * in a histogram format to verify the expected frequencies.
     *
     * @param patient Generated patient data to analyze
     */
    @Property(tries = 1000)
    @StatisticsReport(format = Histogram.class)
    void testFrequency(@ForAll("patientArbitrary") Patient patient) {
        System.out.printf("Patient: %s%n", patient);
        Statistics.collect(patient.bloodType());
    }
}
