package com.evolutionnext.architecture;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.spi.ToolProvider;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HexagonalArchitectureTest {
    private static final String FEATURES = "com.evolutionnext.features.";
    private static final String DOMAIN = ".domain.";
    private static final String DOMAIN_MODEL = ".domain.model.";
    private static final String DOMAIN_EVENTS = ".domain.events.";
    private static final String DOMAIN_SERVICE = ".domain.service.";
    private static final String APPLICATION = ".application.";
    private static final String APPLICATION_SERVICE = ".application.service.";
    private static final String PORT = ".port.";
    private static final String PORT_IN = ".port.in.";
    private static final String PORT_OUT = ".port.out.";
    private static final String INFRASTRUCTURE = ".infrastructure.";
    private static final String ADAPTER_IN = ".infrastructure.adapter.in.";
    private static final String ADAPTER_OUT = ".infrastructure.adapter.out.";

    /*
     * ArchUnit 1.4.1 is the latest release, and it supports class-file import
     * through Java 25. This project compiles Java 26 class files, so these tests
     * enforce the same architecture rules with jdeps plus reflection until
     * ArchUnit can import Java 26 bytecode directly.
     */

    @Test
    void hexagonal_dependencies_point_inward() {
        var violations = new ArrayList<String>();
        for (var dependency : classDependencies()) {
            if (inFeatureLayer(dependency.target(), INFRASTRUCTURE)) {
                if (inFeatureLayer(dependency.origin(), DOMAIN)
                    || inFeatureLayer(dependency.origin(), APPLICATION)
                    || inFeatureLayer(dependency.origin(), PORT)) {
                    violations.add(dependency.origin() + " depends on infrastructure type " + dependency.target());
                }
            }
            if (inFeatureLayer(dependency.origin(), DOMAIN)
                && inFeaturePackage(dependency.target())
                && !inFeatureLayer(dependency.target(), DOMAIN)) {
                violations.add(dependency.origin() + " depends on outer feature type " + dependency.target());
            }
            if (inFeatureLayer(dependency.origin(), APPLICATION)
                && (inFeatureLayer(dependency.target(), INFRASTRUCTURE) || dependency.target().startsWith("com.evolutionnext.http."))) {
                violations.add(dependency.origin() + " depends on adapter or HTTP type " + dependency.target());
            }
            if (inFeatureLayer(dependency.origin(), PORT)
                && inFeatureLayer(dependency.target(), INFRASTRUCTURE)) {
                violations.add(dependency.origin() + " depends on infrastructure type " + dependency.target());
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void feature_adapters_live_in_adapter_packages() {
        var violations = new ArrayList<String>();
        for (var type : applicationClasses()) {
            var name = type.getName();
            if (name.endsWith("Handler") && inFeaturePackage(name) && !inFeatureLayer(name, ADAPTER_IN)) {
                violations.add(name + " must live under infrastructure.adapter.in");
            }
            if (name.endsWith("Repository") && inFeatureLayer(name, INFRASTRUCTURE) && !inFeatureLayer(name, ADAPTER_OUT)) {
                violations.add(name + " must live under infrastructure.adapter.out");
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void domain_types_follow_ddd_package_rules() {
        var violations = new ArrayList<String>();
        for (var type : applicationClasses()) {
            var name = type.getName();
            if (!inFeatureLayer(name, DOMAIN)) {
                continue;
            }
            if (isDomainEvent(type)) {
                if (!inFeatureLayer(name, DOMAIN_EVENTS)) {
                    violations.add(name + " must live under domain.events");
                }
                if (!type.isInterface() && !hasApprovedEventName(type)) {
                    violations.add(name + " must end with an approved past-tense event verb");
                }
            } else if (name.endsWith("DomainService")) {
                if (!inFeatureLayer(name, DOMAIN_SERVICE)) {
                    violations.add(name + " must live under domain.service");
                }
                for (var field : type.getDeclaredFields()) {
                    if (!(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))) {
                        violations.add(name + " must be stateless but has field " + field.getName());
                    }
                }
            } else if (!type.isInterface() && !inFeatureLayer(name, DOMAIN_MODEL)) {
                violations.add(name + " must live under domain.model");
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void domain_model_does_not_use_infrastructure_or_test_frameworks() {
        var violations = new ArrayList<String>();
        var disallowedPackages = List.of(
            "java.sql.",
            "java.net.http.",
            "com.sun.net.httpserver.",
            "org.testcontainers.",
            "io.cucumber.",
            "io.restassured.",
            "net.jqwik."
        );
        for (var dependency : classDependencies()) {
            if (!inFeatureLayer(dependency.origin(), DOMAIN_MODEL)) {
                continue;
            }
            if (disallowedPackages.stream().anyMatch(dependency.target()::startsWith)) {
                violations.add(dependency.origin() + " depends on infrastructure or test framework type " + dependency.target());
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void port_names_and_packages_follow_the_standard() {
        var violations = new ArrayList<String>();
        for (var type : applicationClasses()) {
            var name = type.getName();
            if (name.endsWith("CommandPort") || name.endsWith("QueryPort")) {
                if (!inFeatureLayer(name, PORT_IN)) {
                    violations.add(name + " must live under port.in");
                }
                if (!type.getSimpleName().matches("[A-Z][A-Za-z0-9]+[A-Z][A-Za-z0-9]+(Command|Query)Port")) {
                    violations.add(name + " must follow <Role><Aggregate><Command|Query>Port");
                }
            }
            if (type.isInterface() && name.endsWith("Repository") && inFeatureLayer(name, PORT) && !inFeatureLayer(name, PORT_OUT)) {
                violations.add(name + " must live under port.out");
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void application_services_keep_command_and_query_ports_separate() {
        var violations = new ArrayList<String>();
        for (var type : applicationClasses()) {
            var name = type.getName();
            if (!name.endsWith("ApplicationService")) {
                continue;
            }
            if (inFeatureLayer(name, APPLICATION) && !inFeatureLayer(name, APPLICATION_SERVICE)) {
                violations.add(name + " must live under application.service");
            }
            var implementsCommand = Stream.of(type.getInterfaces())
                .anyMatch(port -> port.getSimpleName().endsWith("CommandPort"));
            var implementsQuery = Stream.of(type.getInterfaces())
                .anyMatch(port -> port.getSimpleName().endsWith("QueryPort"));
            if (name.endsWith("CommandApplicationService") && !implementsCommand) {
                violations.add(name + " must implement a command port");
            }
            if (name.endsWith("CommandApplicationService") && implementsQuery) {
                violations.add(name + " must not implement a query port");
            }
            if (name.endsWith("QueryApplicationService") && !implementsQuery) {
                violations.add(name + " must implement a query port");
            }
            if (name.endsWith("QueryApplicationService") && implementsCommand) {
                violations.add(name + " must not implement a command port");
            }
            if (implementsCommand && implementsQuery) {
                violations.add(name + " must not implement both command and query ports");
            }
        }
        assertThat(violations).isEmpty();
    }

    @Test
    void repository_adapters_implement_repository_ports_and_have_tests() {
        var violations = new ArrayList<String>();
        for (var type : applicationClasses()) {
            var name = type.getName();
            if (name.endsWith("Repository") && inFeatureLayer(name, ADAPTER_OUT)) {
                var implementsRepositoryPort = Stream.of(type.getInterfaces())
                    .anyMatch(port -> port.getSimpleName().endsWith("Repository") && inFeatureLayer(port.getName(), PORT_OUT));
                if (!implementsRepositoryPort) {
                    violations.add(name + " must implement a port.out repository");
                }
                var testPath = Path.of("src/test/java", name.replace('.', '/') + "PropertyTest.java");
                if (!Files.exists(testPath)) {
                    testPath = Path.of("full-application/src/test/java", name.replace('.', '/') + "PropertyTest.java");
                }
                if (!Files.exists(testPath)) {
                    testPath = Path.of("src/test/java", name.replace('.', '/') + "Test.java");
                }
                if (!Files.exists(testPath)) {
                    testPath = Path.of("full-application/src/test/java", name.replace('.', '/') + "Test.java");
                }
                if (!Files.exists(testPath)) {
                    violations.add(name + " must have a corresponding property or integration test");
                }
            }
        }
        assertThat(violations).isEmpty();
    }

    private static List<Class<?>> applicationClasses() {
        try (var paths = Files.walk(targetClasses())) {
            return paths
                .filter(path -> path.toString().endsWith(".class"))
                .filter(path -> path.toString().contains("/com/evolutionnext/"))
                .map(HexagonalArchitectureTest::className)
                .<Class<?>>map(HexagonalArchitectureTest::loadClass)
                .toList();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to discover application classes", exception);
        }
    }

    private static List<ClassDependency> classDependencies() {
        var jdeps = ToolProvider.findFirst("jdeps")
            .orElseThrow(() -> new IllegalStateException("jdeps is required to enforce architecture dependencies"));
        var output = new ByteArrayOutputStream();
        var error = new ByteArrayOutputStream();
        var exitCode = jdeps.run(new PrintStream(output), new PrintStream(error),
            "-verbose:class",
            "-filter:none",
            "-recursive",
            targetClasses().toString());
        if (exitCode != 0) {
            throw new IllegalStateException("jdeps failed: " + error.toString(StandardCharsets.UTF_8));
        }
        return output.toString(StandardCharsets.UTF_8).lines()
            .map(String::trim)
            .filter(line -> line.startsWith("com.evolutionnext."))
            .filter(line -> line.contains(" -> "))
            .map(HexagonalArchitectureTest::dependency)
            .toList();
    }

    private static ClassDependency dependency(String line) {
        var parts = line.split("\\s+->\\s+");
        var origin = parts[0].trim();
        var target = parts[1].trim().split("\\s+")[0];
        return new ClassDependency(origin, target);
    }

    private static Path targetClasses() {
        var targetClasses = Path.of("target/classes");
        if (!Files.exists(targetClasses)) {
            targetClasses = Path.of("full-application/target/classes");
        }
        return targetClasses;
    }

    private static String className(Path path) {
        var relative = targetClasses().relativize(path);
        return relative.toString()
            .replace('/', '.')
            .replace('\\', '.')
            .replaceAll("\\.class$", "");
    }

    private static Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Unable to load " + name, exception);
        }
    }

    private static boolean isDomainEvent(Class<?> type) {
        return type.getSimpleName().endsWith("Event") || hasApprovedEventName(type);
    }

    private static boolean hasApprovedEventName(Class<?> type) {
        return Stream.of("Registered", "Created", "Updated", "Deleted", "LoggedIn", "Submitted", "Canceled")
            .anyMatch(type.getSimpleName()::endsWith);
    }

    private static boolean inFeaturePackage(String className) {
        return className.startsWith(FEATURES);
    }

    private static boolean inFeatureLayer(String className, String layer) {
        return inFeaturePackage(className) && className.contains(layer);
    }

    private record ClassDependency(String origin, String target) {
    }
}
