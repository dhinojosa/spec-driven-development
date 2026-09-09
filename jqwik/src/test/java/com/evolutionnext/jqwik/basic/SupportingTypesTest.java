package com.evolutionnext.jqwik.basic;

import com.evolutionnext.jqwik.models.Planet;
import net.jqwik.api.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

public class SupportingTypesTest {

    @Property(tries = 5)
    void integersAreGenerated(@ForAll int i) {
        System.out.printf("int: %d%n", i);
    }

    @Property(tries = 5)
    void longsAreGenerated(@ForAll long l) {
        System.out.printf("long: %d%n", l);
    }

    @Property(tries = 5)
    void shortsAreGenerated(@ForAll short s) {
        System.out.printf("short: %d%n", s);
    }

    @Property(tries = 5)
    void bytesAreGenerated(@ForAll byte b) {
        System.out.printf("byte: %d%n", b);
    }

    @Property(tries = 5)
    void floatsAreGenerated(@ForAll float f) {
        System.out.printf("float: %f%n", f);
    }

    @Property(tries = 5)
    void doublesAreGenerated(@ForAll double d) {
        System.out.printf("double: %f%n", d);
    }

    @Property(tries = 5)
    void booleansAreGenerated(@ForAll boolean b) {
        System.out.printf("boolean: %b%n", b);
    }

    @Property(tries = 5)
    void charsAreGenerated(@ForAll char c) {
        System.out.printf("char: %c (U+%04X)%n", c, (int) c);
    }

    @Property(tries = 5)
    void stringsAreGenerated(@ForAll String s) {
        System.out.printf("string: %s%n", s);
    }

    @Property(tries = 5)
    void unicodeStringsAreGenerated(@ForAll String s) {
        if (s.codePoints().anyMatch(cp -> cp > 127)) {
            System.out.printf("unicode string: %s%n", s);
        }
    }

    @Property(tries = 5)
    void bigIntegersAreGenerated(@ForAll BigInteger bi) {
        System.out.printf("BigInteger: %s%n", bi);
    }

    @Property(tries = 5)
    void bigDecimalsAreGenerated(@ForAll BigDecimal bd) {
        System.out.printf("BigDecimal: %s%n", bd);
    }

    @Property(tries = 5)
    void localDatesAreGenerated(@ForAll LocalDate date) {
        System.out.printf("LocalDate: %s%n", date);
    }

    @Property(tries = 5)
    void localDateTimesAreGenerated(@ForAll LocalDateTime dateTime) {
        System.out.printf("LocalDateTime: %s%n", dateTime);
    }

    @Property(tries = 5)
    void instantsAreGenerated(@ForAll Instant instant) {
        System.out.printf("Instant: %s%n", instant);
    }

    @Property(tries = 5)
    void durationsAreGenerated(@ForAll Duration duration) {
        System.out.printf("Duration: %s%n", duration);
    }

    @Property(tries = 5)
    void collectionsAreGenerated(@ForAll List<Integer> list) {
        System.out.printf("List: %s%n", list);
    }

    @Property(tries = 5)
    void setsAreGenerated(@ForAll Set<String> set) {
        System.out.printf("Set: %s%n", set);
    }

    @Property(tries = 5)
    void mapsAreGenerated(@ForAll Map<Integer, String> map) {
        System.out.printf("Map: %s%n", map);
    }

    @Property(tries = 5)
    void optionalsAreGenerated(@ForAll Optional<String> opt) {
        System.out.printf("Optional: %s%n", opt);
    }

    @Property(tries = 5)
    void primitiveArraysAreGenerated(@ForAll int[] arr) {
        System.out.printf("Array: %s%n", Arrays.toString(arr));
    }

    @Property(tries = 5)
    void objectArraysAreGenerated(@ForAll String[] arr) {
        System.out.printf("Array: %s%n", Arrays.toString(arr));
    }

    @Property(tries = 5)
    void enumsAreGenerated(@ForAll Planet planet) {
        System.out.printf("Planet: %-8s → Gravity: %.2f%n", planet.name(), planet.surfaceGravity());
    }
}
