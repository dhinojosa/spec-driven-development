package com.evolutionnext.jqwik.basic;

import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Disabled;
import net.jqwik.api.constraints.AlphaChars;
import org.assertj.core.api.Assertions;

public class BasicTest {

    /**
     * This test demonstrates a straightforward TDD-style test using JQwik's @Example annotation.
     * Unlike property-based tests, this is a single concrete example with fixed inputs and expected outputs,
     * similar to traditional unit tests.
     */
    @Example
    public void testExample() {
        Integer answer = 42;
        Assertions.assertThat(answer).isEqualTo(42);
    }

    /**
     * This property test demonstrates a failing case where the sum of two integers
     * is not always greater than both of its parts. This is particularly evident
     * when dealing with:
     * 1. Negative numbers: (-2) + (-3) = -5, which is less than both -2 and -3
     * 2. Integer overflow: When adding large numbers, the sum might overflow and become negative
     * The test is disabled because it intentionally proves this mathematical property false.
     */
    @Property
    @Disabled("This test is temporarily disabled")
    public void testAddition(@ForAll int i, @ForAll int j) {
        System.out.printf("i: %d, j: %d%n", i, j);
        Assertions.assertThat(add(i, j))
            .isGreaterThanOrEqualTo(i).isGreaterThanOrEqualTo(j);
    }

    private static int add(int i, int j) {
        return i + j;
    }

    @Property
    public void testStrings(@ForAll @AlphaChars String string) {
        System.out.printf("String: %s%n", string);
    }
}












