# Architecture

The application should be built as vertical feature slices.

For a business capability, evaluate the full path from domain model to
externally observable behavior. If a layer is not needed for a feature, the task
plan must state why.

## Module Responsibilities

- `full-application`: application code, domain model, adapters, controllers,
  persistence, unit tests, and integration tests.
- `full-application-acceptance`: Cucumber acceptance specifications for
  business-facing behavior.
- `full-application-e2e`: end-to-end tests that run against packaged application
  images and supporting infrastructure.

# Hexagonal Architecture

The project follows a strict Hexagonal architecture, emphasizing a clear separation of domain logic and infrastructure.

## Feature Structure (`<package-name>.features.<feature-name>`)

- Each feature is encapsulated in its own module to ensure high cohesion and low coupling. 
- A feature is a section or module boundary for the application.
- For example: `com.xyzcorp.features.order`

### Standard Layout for Hexagonal Architecture

In each feature, the following structure is followed:

- `domain/` - Pure business logic, no infrastructure references.
    - `model/` - Entities and value objects.
    - `service/` - Coordination of domain logic.
    - `events/` - Domain events.
- `application/` - Coordination logic.
    - `command/` - Intent to change state (ADTs). Includes `error/` and `result/` sub-packages.
    - `query/` - Intent to read state (Read Models). Includes `error/` and `result/` sub-packages.
    - `service/` - Implements `port/in`.
- `port/` - Boundaries.
    - `in/` - Injected into controllers.
    - `out/` - Implemented by infrastructure.
- `infrastructure/` - External systems.
    - `adapter/in/` - Concrete CLI, UI, AI drivers.
    - `adapter/out/` - Concrete Databases, Messaging, External Sources.

## Port Layer

### Port In

- Port In is located in `port/in` folder
- Port In has a `<Role><Aggregate><Command|Query>Port` name.

#### Command Ports

- Use the Command Pattern
- Command Ports have the name `<Role><Aggregate>CommandPort`, for example:

```java
public interface ClientCustomerCommandPort {
    CustomerResult execute(CustomerCommand command);
}
```

- They receive a Command ADT using `sealed` 
- They may have payload data to avoid having every fine-grained field set
- The payload data must not be a domain aggregate.
- The Command ADT must be in the `application/command` folder of the feature

```java
public sealed interface CustomerCommand permits CustomerCommand.CreateCustomer,CustomerCommand.UpdateCustomer {
    record CreateCustomer(CustomerId customerId, String name, BigDecimal creditLimit)   implements CustomerCommand {
    }
    record UpdateCustomer(CustomerId customerId, String name, BigDecimal creditLimit)   implements CustomerCommand {
    }
}
```

- The Command Port returns a Result ADT also using `sealed`

```java
public sealed interface CustomerResult permits CustomerResult.CustomerCreated, CustomerResult.CustomerUpdated {
    record CustomerCreated(CustomerId customerId) implements CustomerResult {
    }
    record CustomerUpdated(CustomerId customerId) implements CustomerResult {
    }
}
```

### Query Ports

- Query Ports are defined in `port/in`
- They are named as `<Role><Aggregate>QueryPort`
- They use rich methods like `findById`, `findAllLikeFirstName(...)`, etc.

```java
public interface ClientCustomerQueryPort {
    CustomerQueryResult findById(CustomerId id);
}
```

- They return QueryResult `sealed` ADT, which can be either anemic or rich in what they return

```java
public sealed interface CustomerQueryResult permits CustomerFound, CustomerNotFound, CustomerListFound {
}
```

#### Query Result DTOs 

Query Ports return any of the following dependeding on the weight of payload:

1. `<Aggregate>Summary` - A lightweight summary of Aggregate(s)
2. `<Aggregate>Card` - A Succinct Representation of Aggregate(s) 
3. `<Aggregate>Details` - Details of an Aggregate(s)
4. `<Aggregate>View` - Materialized Representation of Aggregate(s)
5. `<Aggregate>Page` - Paginated List of Items
6. `<Aggregate>Profile` - A Large Object containing representation of Aggregate(s)

#### Port Out

### Output Port Naming
| Responsibility | Port Interface | Adapter Implementation |
|---|---|---|
| Persistence / DB | OrderRepository | SlickOrderRepository |
| Messaging / Events | OrderEventPublisher | KafkaOrderEventPublisher |
| External API | ProductCatalogClient | RestProductCatalogClient |


## Domain Layer

### Domain Model

- Domain Driven Design Aggregates with Entities and Value Objects generally as java `record`
- They are stored in the `domain/model`
- All aggregates must have a root and boundary:
  - `Order` and `OrderItem` should be together as an object graph
  - products, customers, associated with orders should be separate aggregates and referenced by the `Id`
- All aggregate root's ids, example `ProductId` should be a value object as a java `record` wrapped around a `UUID`
- An aggregate can have domain events that occur during the lifecycle of the aggregate.
- For a domain event required to be created at aggregate instantiation, prefer to use a static factory method.

#### Example

Here is a sample of an `Order` aggregate:
- `Order` as an aggregate root.
- `OrderItem` as an entity and part of the aggregate.
- The `OrderEvent` as a domain event.
- The static factory method `of` as a constructor creating the `OrderCreated` domain event.
- There is no inconsistent state within an aggregate
- `CustomerId` is a value object and is not a part of the aggregate.
- This can be modeled as a java `record`

```java
public class Order {
    public static final String CANCELED_STATEMENT = "You can't submit a canceled order";
    private final OrderId orderId;
    private final List<OrderItem> orderItemList;
    private final List<OrderEvent> orderEventList;
    private final CustomerId customerId;

    protected Order(OrderId orderId, CustomerId customerId, ArrayList<OrderEvent> orderEventList) {
        this.orderId = orderId;
        this.orderItemList = new ArrayList<>();
        this.orderEventList = orderEventList;
        this.customerId = customerId;
    }

    public static Order of(OrderId orderId, CustomerId customer) {
        ArrayList<OrderEvent> orderEventList = new ArrayList<>();
        orderEventList.add(new OrderCreated(orderId, LocalDateTime.now()));
        return new Order(orderId, customer, orderEventList);
    }

    public void cancel() {
        orderEventList.add(new OrderCanceled(LocalDateTime.now()));
    }

    public void submit() {
        if (Objects.requireNonNull(getState())
            instanceof OrderCanceled) {
            throw new
                IllegalStateException(CANCELED_STATEMENT);
        } else {
            orderEventList.add(new OrderSubmitted(LocalDateTime.now()));
        }
    }

    public void addOrderItem(ProductId productId, int quantity, BigDecimal price) {
        OrderItem orderItem = new OrderItem(productId, quantity, price);
        orderItemList.add(orderItem);
        orderEventList.add(new OrderItemAdded(this.orderId, orderItem));
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public List<OrderEvent> getOrderEventList() {
        return orderEventList;
    }

    public OrderEvent getState() {
        return orderEventList.getLast();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public BigDecimal total() {
        return orderItemList.stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void fulfill() {

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
            .add("orderId=" + orderId)
            .add("customerId=" + customerId)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(orderId, order.orderId) && Objects.equals(orderItemList, order.orderItemList) && Objects.equals(customerId, order.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderItemList, customerId);
    }
}
```

- The id value object can be modeled as a java `record`
- It wraps around a `UUID`

```java
public record OrderId(UUID value) { }
```

- `OrderItem` represents a line item in an order.
- It is a part of the `Order` aggregate.
- It is modeled as a java `record` or class.
- It has a `productId` because product is not part of the aggregate.
- It has a `quantity` and `price` because they are part of the aggregate.

```java
public record OrderItem(ProductId productId,
                        int quantity, BigDecimal price) {}
```

### Domain Services

- Domain Services are a class that is _stateless_ and coordinates behavior between one or more aggregates.

```java
public class OrderDomainService {

    /* In this section, I am using both a customer and an order aggregate */
    public static boolean checkCredit(Order order, Customer customer) {
        return order.total().compareTo(customer.creditLimit()) > 0;
    }
}
```

### Domain Events

- Domain Events are immutable objects representing a change in state; they must be in the past tense.
- For example, `OrderCreated` is a domain event that represents the creation of an order.

## Application Layer

### Application Services

- An application service implements a `port/in` interface.
- It is responsible for coordinating the behavior of the domain layer.
- It is not responsible for any persistence or external systems.
- It is not responsible for any UI or CLI output.
- It is not responsible for any domain events.
- It is typically injected with an implementation of `port/out`.
- It is typically named after the port `<PortName>ApplicationService`, for example, given the port name `AdminProductCommandPort` the application service name would be `AdminProductApplicationService`, where `Admin` is the role of who is calling the service, `Product` is the `feature` or even the `aggregate`, and `ApplicationService` is the `ApplicationService`

#### Example

- `CustomerCommandApplicationService` is an application service that implements `ClientCustomerCommandPort`.
- It is responsible for executing a `CustomerCommand` and returning a `CustomerResult`.
- It is injected with a `CustomerRepository` and a `Transactional` implementation. `Transactional` is a project definition that handles a transaction around a block of code. It is not a part of architecture, but how perform transactions using `ScopedValue`
- It is named `CustomerCommandApplicationService`.

```java
public class CustomerCommandApplicationService implements ClientCustomerCommandPort {
    private final CustomerRepository customerRepository;
    private final Transactional transactional;

    public CustomerCommandApplicationService(CustomerRepository customerRepository,
                                             Transactional transactional) {
        this.customerRepository = customerRepository;
        this.transactional = transactional;
    }


    @Override
    public CustomerResult execute(CustomerCommand command) {
        return switch (command) {
            case CustomerCommand.CreateCustomer createCustomer ->
                transactional.transactionally(() -> {
                var customer = new Customer(createCustomer.customerId(),
                    createCustomer.name(),
                    createCustomer.creditLimit());
                customerRepository.save(customer);
                return new CustomerCreated(customer.id());
            });
        };
    }
}
```

## Infrastructure 

### Infrastructure In

- Represents Controllers in the case of web applications
- Represents Main Methods as CLI
- Could Represent Consumers in Messaging
- Will always inject one more `port/in` interfaces which will be wired either explicitly or with a dependency injection framework.

### Infrastructure Out

- Implements the `port/out` interfaces only
- They are concrete implementations for what is interfaces outward
- They are tested using test containers and/or test containers with JQwik
- They are injected into services which are themselves implementation of `port/in` interfaces

## Property-Based And Integration Testing

Use jqwik at varying levels of the application where generated inputs can prove
behavior across meaningful cases.

Use Testcontainers for tests that require real infrastructure, especially
repository and persistence behavior.

When jqwik and Testcontainers are used together in the same test, add the
`net.jqwik:jqwik-testcontainers` dependency and use its Testcontainers
annotations. This avoids lifecycle races between jqwik property execution and
container startup.

### Repository Properties

For repositories, every generated record that is created and saved must be
findable by id.

Repository property tests should combine jqwik-generated records with
Testcontainers-backed infrastructure when persistence is involved.

### Service Properties

For application services, every command accepted by a command port must produce
state that can be observed through the corresponding query port.

Service property tests should generate command inputs, execute the command port,
and verify the result through the query port rather than by inspecting
implementation details.

### Controller Properties

For controllers, every successful `POST` that creates a resource must be
observable through a corresponding `GET`.

Controller property tests should generate request payloads, call the controller
through the same HTTP-facing boundary used by integration tests, and verify the
created resource through `GET`.

### Arbitraries And Generators

If jqwik `Gen` or `Arbitrary` helpers are needed, place them in an `arbitrary`
package under the corresponding test package.

Example:

```text
src/test/java/com/xyzcorp/feature/account/arbitrary/AccountArbitrary.java
```

## Application Layers

Feature tasks should be considered:

- domain
- domain events
- domain services
- application services
- repository ports and adapters
- controllers
- domain unit tests
- application service unit tests
- controller unit tests
- repository integration tests with Testcontainers and jqwik
- application service integration tests
- controller integration tests

## End-To-End Layer

End-to-end tests should run through `full-application-e2e`.

The `full-application` module must produce an application container image using
the Jib Maven plugin during the Maven `package` phase.

The Jib image is used by `full-application-e2e` as the application under test.
The e2e module should use Docker Compose to run the application image and
supporting services such as Postgres.

The `full-application-e2e` module should keep an `.env` file in
`src/main/resources` with the application image version:

```text
FULL_APPLICATION_VERSION=${project.version}
```

The `${project.version}` value must be interpolated by the Maven Resources
Plugin during `process-resources`.

Use the latest available `maven-resources-plugin` version, following this
configuration shape:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version><!-- Use the latest version. --></version>
    <executions>
        <execution>
            <id>generate-env-file</id>
            <phase>process-resources</phase>
            <goals>
                <goal>resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                <resources>
                    <resource>
                        <directory>src/main/resources</directory>
                        <filtering>true</filtering>
                        <includes>
                            <include>.env</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

End-to-end tests should use Testcontainers and REST Assured for HTTP-facing
verification. Use jqwik with them when generated inputs add meaningful coverage.
Use Selenium when browser-level behavior is part of the business flow.

The e2e Docker Compose file should follow this shape:

```yaml
services:
    full-application:
        image: dhinojosa/full-application:${FULL_APPLICATION_VERSION:-1.0-SNAPSHOT}
        ports:
            - "8080:8080"
        depends_on:
            - postgres
        environment:
            DATABASE_URL: jdbc:postgresql://postgres:5432/orders
            DATABASE_USERNAME: postgres
            DATABASE_PASSWORD: postgres

    postgres:
        image: postgres:15.2
        environment:
            POSTGRES_USER: postgres
            POSTGRES_PASSWORD: postgres
            POSTGRES_DB: orders
        volumes:
            - postgres_data:/var/lib/postgresql/data
            - ./init.sql:/docker-entrypoint-initdb.d/init.sql

volumes:
    postgres_data:
```

Include `init.sql` when the e2e database requires schema or seed data before the
application starts.

### Jib Container Image

Configure Jib in `full-application`.

Use:

- plugin: `com.google.cloud.tools:jib-maven-plugin`
- version: `3.4.6`
- execution phase: `package`
- goal: `build`
- base image: an OpenJDK slim Bookworm image matching the application's Java
  version, such as `openjdk:26-slim-bookworm` for Java 26 or
  `openjdk:25-ea-slim-bookworm` for Java 25
- target image: `dhinojosa/full-application:${project.version}`
- main class: `com.evolutionnext.Runner`
- JVM flag: `--enable-preview`
- exposed port: `8080` when the application exposes an HTTP server
- platforms:
  - `linux/amd64`
  - `linux/arm64`

Use this configuration shape:

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.6</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>build</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <from>
            <image><!-- Use an OpenJDK slim Bookworm image matching the application Java version. --></image>
            <platforms>
                <platform>
                    <architecture>amd64</architecture>
                    <os>linux</os>
                </platform>
                <platform>
                    <architecture>arm64</architecture>
                    <os>linux</os>
                </platform>
            </platforms>
        </from>
        <to>
            <image>dhinojosa/full-application:${project.version}</image>
        </to>
        <container>
            <mainClass>com.evolutionnext.Runner</mainClass>
            <jvmFlags>
                <jvmFlag>--enable-preview</jvmFlag>
            </jvmFlags>
            <ports>
                <port>8080</port>
            </ports>
        </container>
    </configuration>
</plugin>
```

## Architectural Style

Use ports and adapters where it helps keep the application core independent of infrastructure concerns.

Keep the dependency direction clear:

- domain code should not depend on application services, controllers, or
  infrastructure
- application services may depend on domain code and ports
- adapters implement ports
- controllers call application services
- must be enforced by arch unit

## ArchUnit Enforcement

Use architecture tests in `full-application` to enforce the hexagonal and DDD
boundaries.

The project should keep the latest ArchUnit dependency available. As of
ArchUnit `1.4.1`, ArchUnit imports class files through Java 25, while this
project compiles Java 26 class files. Until ArchUnit supports Java 26 bytecode
directly, enforce the same rules with JDK tooling such as `jdeps` and
reflection from the architecture test class. Document that exception in the
test class.

Architecture rules should cover:

- hexagonal dependency direction between domain, application, ports, and
  infrastructure
- domain purity from application, port, infrastructure, HTTP, and runner packages
- application independence from infrastructure adapters and HTTP handlers
- port independence from infrastructure adapters
- HTTP handler placement under `infrastructure/adapter/in`
- repository adapter placement under `infrastructure/adapter/out`
- domain model placement under `domain/model`
- domain event placement under `domain/events`
- domain event past-tense naming
- domain service placement and statelessness
- port naming for `<Role><Aggregate>CommandPort` and
  `<Role><Aggregate>QueryPort`
- repository port naming under `port/out`
- command application services implementing command ports only
- query application services implementing query ports only
- no application service implementing both a command port and query port

When a rule needs an intentional exception, document the exception and the
reason in the ArchUnit test class.
