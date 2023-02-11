# faggregate

[![Continuous Integration - Build](https://github.com/tmorin/faggregate/actions/workflows/ci-build.yaml/badge.svg)](https://github.com/tmorin/faggregate/actions/workflows/ci-build.yaml)

> Unlock the Power of DDD Aggregates and Functional Programming with `faggregate`!
> This Java Library simplifies the implementation of complex business systems by offering a set of tools and abstractions that make it easy to build robust and scalable software.
> Whether you're a seasoned developer or just getting started, our library provides a powerful solution for leveraging the full potential of Domain Driven Design Aggregates and functional programming concepts.
> Don't miss out on this opportunity to streamline your development process and build high-quality software - try our library today!

The Value Stream is barely simple:

1. define the data structure of the aggregate's state
2. define the commands and events which change the state
3. define the side effects : initialization, storing, loading, destroying
4. and finally bundle everything with the help of a nice builder

> Empower Your Development with DDD Aggregates & Functional Programming - Learn with this [Tutorial](https://tmorin.github.io/faggregate)!

## Features

### Outbox Pattern

The library handle the transactional part of the [Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html).

The [todo-infra-quarkus](examples/todo-infra-quarkus) example demonstrates the usage of MongoDB to store the state and the domain events of an Aggregate.

### Native Image

The library is designed to be integrated directly into a [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/basics/).

The [todo-infra-quarkus](examples/todo-infra-quarkus) example provides an E2E exemple leveraging on [Quarkus](https://quarkus.io).

### Functional

The library emphases some functional programing concepts like immutable structures.

The [todo-core](examples/todo-core) example provides an implementation leveraging on the [Immutables](https://immutables.github.io) Java library to easily and nicely force the immutable structures of the Aggregate state.

The [todo-core](examples/todo-core) module is also used by [todo-infra-quarkus](examples/todo-infra-quarkus).
Therefore, it demonstrates [Immutables](https://immutables.github.io) can be used also into a Native Image.

## Installation

The library provides an implementation: `faggregate-core-simple`.
Its usage can be done _manually_ or via [Service Loader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html).

The artifacts are deployed on [GitHub](https://github.com/tmorin?tab=packages&repo_name=faggregate).
Therefore, a custom configuration is required to resolve the artifacts, please review [Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

### Manual integration

[The GitHub Package](https://github.com/tmorin/faggregate/packages/1453016)

```xml
<dependency>
    <groupId>io.morin.faggregate</groupId>
    <artifactId>faggregate-core-simple</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Service Loader integration

[The GitHub Package](https://github.com/tmorin/faggregate/packages/1453021)

```xml
<dependency>
    <groupId>io.morin.faggregate</groupId>
    <artifactId>faggregate-spi-simple</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Maintenance

**Dependencies upgrade**
```shell
./mvnw versions:display-dependency-updates
```

**Release**
```shell
./mvnw --batch-mode release:clean \
&& ./mvnw --batch-mode release:prepare \
  -DreleaseVersion=X.Y.Z \
  -DdevelopmentVersion=Y.X.Z-SNAPSHOT
```
