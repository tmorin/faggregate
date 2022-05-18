# faggregate

> A Java library helping to implement DDD aggregates with a functional touch.

1. define the data structure of the aggregate's state
2. define the commands and events which mutates the state
3. define the side effects : initialization, storing, loading, destroying
4. and finally bundle everything with a builder

## Installation

The library provides an implementation: `faggregate-core-simple`.
Its usage can be done _manually_ or via [Service Loader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html).

The artifacts are deployed on [GitHub](https://github.com/tmorin?tab=packages&repo_name=faggregate).
Therefore, a custom configuration is required to resolve the artifacts, please review [Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

### Manual integration

```xml
<dependency>
    <groupId>io.morin.faggregate</groupId>
    <artifactId>faggregate-core-simple</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Service Loader integration

```xml
<dependency>
    <groupId>io.morin.faggregate</groupId>
    <artifactId>faggregate-spi-simple</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Release

```shell
./mvnw --batch-mode release:clean \
&& ./mvnw --batch-mode release:prepare \
  -DreleaseVersion=X.Y.Z \
  -DdevelopmentVersion=Y.X.Z-SNAPSHOT
```
