package com.evolutionnext.jqwik.arbitraries;

import net.datafaker.Faker;
import net.jqwik.api.*;

public class ArbitraryTest {


    @Provide("justHelloWorld")
    Arbitrary<String> justHelloWorld() {
        return Arbitraries.just("Hello World");
    }

    @Property
    void justProperty(@ForAll("justHelloWorld") String helloWorld) {
        System.out.println(helloWorld);
    }


    public record Employee(Integer employeeId,
                           String firstName,
                           String lastName) {
    }

    @Provide
    Arbitrary<Employee> employeeArbitraryRaw() {
        return Combinators
            .combine(Arbitraries.integers(),
                Arbitraries.strings(),
                Arbitraries.strings())
            .as((employeeId, firstName, lastName) ->
                new Employee(employeeId, firstName, lastName));
    }

    @Property
    public void testEmployeeArbitraryRaw(@ForAll("employeeArbitraryRaw") Employee employee) {
        System.out.printf("Employee: %s%n", employee);
    }

    @Provide
    Arbitrary<Employee> employeeArbitraryPositiveId() {
        return Combinators
            .combine(Arbitraries.integers().greaterOrEqual(0),
                Arbitraries.strings().alpha(),
                Arbitraries.strings().alpha())
            .as(Employee::new);
    }

    Arbitrary<Employee> employeeArbitraryAlternateFlatMap() {
        return Arbitraries.integers().greaterOrEqual(0)
            .flatMap(i ->
            Arbitraries.strings().alpha()
                .flatMap(fn ->
                Arbitraries.strings().alpha()
                    .map(ln -> new Employee(i, fn, ln))
            ));
    }

    @Property
    public void testEmployeeArbitraryPositiveId(@ForAll("employeeArbitraryPositiveId") Employee employee) {
        System.out.printf("Employee: %s%n", employee);
    }

    @Provide
    Arbitrary<Employee> employeeArbitraryPositiveIdAndAlphaNames() {
        return Combinators
            .combine(Arbitraries.integers().greaterOrEqual(1),
                Arbitraries.strings().alpha(),
                Arbitraries.strings().alpha())
            .as(Employee::new);
    }

    @Property
    public void testEmployeeArbitraryPositiveIdAndAlphaNames(@ForAll("employeeArbitraryPositiveIdAndAlphaNames") Employee employee) {
        System.out.printf("Employee: %s%n", employee);
    }

    @Provide
    Arbitrary<Employee> employeeArbitraryPositiveIdAndFakerNames() {
        Faker faker = new Faker();
        return Combinators.combine(
            Arbitraries.integers().greaterOrEqual(0),
            Arbitraries.create(() -> faker.name().firstName()),
            Arbitraries.create(() -> faker.name().lastName())
        ).as(Employee::new);
    }

    @Property
    public void testEmployeeArbitraryPositiveIdAndFakerNames(
        @ForAll("employeeArbitraryPositiveIdAndFakerNames") Employee employee) {
        System.out.printf("Employee: %s%n", employee);
    }


    public record StoreEmployee(Integer employeeId, String firstName,
                                String lastName, Department department,
                                Title title) {
    }

    public record Department(String name) {
    }

    public record Title(String title) {
    }

    Arbitrary<Department> departmentArbitrary() {
        return Arbitraries.of("Appliances", "Accounting").map(Department::new);
    }

    Arbitrary<Title> appliancesTitleArbitrary() {
        return Arbitraries.of("Mechanic", "Service", "Installation").map(Title::new);
    }

    Arbitrary<Title> accountingTitleArbitrary() {
        return Arbitraries.of("Auditing", "Receivable", "Payable", "Payroll").map(Title::new);
    }

    Arbitrary<Title> titlesFromDepartment(Department department) {
        return department.name().equals("Appliances") ?
            appliancesTitleArbitrary() :
            accountingTitleArbitrary();
    }

    Arbitrary<Tuple.Tuple2<Department, Title>> departmentTitleArbitrary() {
        return departmentArbitrary()
            .flatMap(department -> titlesFromDepartment(department)
                .map(title -> Tuple.of(department, title)));
    }

    @Provide
    Arbitrary<StoreEmployee> storeEmployeeArbitrary() {
        return departmentTitleArbitrary().flatMap(t2 ->
            Combinators.combine(
                Arbitraries.integers().greaterOrEqual(0),
                Arbitraries.create(() -> new Faker().name().firstName()),
                Arbitraries.create(() -> new Faker().name().lastName())
            ).as((id, firstName, lastName) ->
                new StoreEmployee(id, firstName, lastName, t2.get1(), t2.get2())
            )
        );
    }

    @Property(tries = 20)
    void testDepartmentRoleArbitrary(@ForAll("storeEmployeeArbitrary") StoreEmployee storeEmployee) {
        System.out.printf("Store Employee: %s%n", storeEmployee);
    }

    public Arbitrary<StoreEmployee> createStoreEmployeeNamed(String name) {
        return storeEmployeeArbitrary().map(s ->
            new StoreEmployee(s.employeeId(), name, s.lastName(), s.department(), s.title()));
    }

    @Provide
    public Arbitrary<StoreEmployee> createStoreEmployeeWithPercentageNamedSteve() {
        return createStoreEmployeeNamed("Steve");
    }

    @Property(tries = 20)
    void testStoreEmployeeNamedSteve(@ForAll("createStoreEmployeeWithPercentageNamedSteve") StoreEmployee storeEmployee) {
        System.out.printf("Store Employee: %s%n", storeEmployee);
    }

    @Provide
    public Arbitrary<Employee> createEmployeePoorly() {
        // BAD: Sampling values outside of the Arbitrary pipeline,
        // I can really only create one because I got a sample

        Integer id = Arbitraries.integers().sample();
        String firstName = Arbitraries.strings().alpha().sample();
        String lastName = Arbitraries.strings().alpha().sample();

        // Wrapping constant values in a "constant" Arbitrary
        return Arbitraries.just(new Employee(id, firstName, lastName));
    }

    /**
     * By using `sample()` in createStudentPoorly(), we break the functional nature of property testing
     * and the seed value is not respected since sampling happens outside the Arbitrary pipeline
     */
    @Property(seed = "-2699408261846660881")
    public void testEmployeePoorly(@ForAll("createEmployeePoorly") Employee employee) {
        System.out.printf("Employee: %s%n", employee);
    }

    @Provide
    public Arbitrary<String> eldenRing() {
        return Arbitraries.randomValue(random -> {
            Faker faker = new Faker(random);
            return faker.eldenRing().npc();
        });
    }

    @Property
    public void testEldenRingNpc(@ForAll("eldenRing") String en) {
        System.out.printf("Elden Ring: %s\n", en);
    }
}
