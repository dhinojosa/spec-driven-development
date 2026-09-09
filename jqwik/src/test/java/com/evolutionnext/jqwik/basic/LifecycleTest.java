package com.evolutionnext.jqwik.basic;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.lifecycle.*;

/**
 * This test class demonstrates the various lifecycle annotations available in JQwik framework.
 * It shows the execution order of different lifecycle hooks:
 * - @BeforeContainer/@AfterContainer: Run once before/after all properties
 * - @BeforeProperty/@AfterProperty: Run before/after each property
 * - @BeforeTry/@AfterTry: Run before/after each try of a property
 * The test includes a sample property that executes multiple times to showcase these lifecycle methods.
 */
public class LifecycleTest {

    /**
     * Static method that runs exactly once before any property of a container class will be executed,
     * even before the first instance of this class will be created.
     */
    @BeforeContainer
    static void beforeContainer() {
        System.out.println("before container");
    }

    /**
     * Static method that runs exactly once after all properties of a container class have run.
     */
    @AfterContainer
    static void afterContainer() {
        System.out.println("after container");
    }

    /**
     * Method that runs once before each property or example.
     *
     * @BeforeExample is an alias with the same functionality.
     */
    @BeforeProperty
    void beforeProperty() {
        System.out.println("before property");
    }

    /**
     * Method that runs once after each property or example.
     *
     * @AfterExample is an alias with the same functionality.
     */
    @AfterProperty
    void afterProperty() {
        System.out.println("after property");
    }

    /**
     * Method that runs once before each try, i.e. execution of a property or example method.
     */
    @BeforeTry
    void beforeTry() {
        System.out.println("before try");
    }

    /**
     * Method that runs once after each try, i.e. execution of a property or example method.
     */
    @AfterTry
    void afterTry() {
        System.out.println("after try");
    }

    @Property(tries = 3)
    void property(@ForAll @IntRange(min = -5, max = 5) int anInt) {
        System.out.println("property: " + anInt);
    }
}
