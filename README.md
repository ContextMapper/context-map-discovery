![Context Mapper](https://raw.githubusercontent.com/wiki/ContextMapper/context-mapper-dsl/logo/cm-logo-github-small.png) 
# Context Map (CML) Discovery Library
[![Build Status](https://travis-ci.com/ContextMapper/context-map-discovery.svg?branch=master)](https://travis-ci.com/ContextMapper/context-map-discovery) [![codecov](https://codecov.io/gh/ContextMapper/context-map-discovery/branch/master/graph/badge.svg)](https://codecov.io/gh/ContextMapper/context-map-discovery) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://img.shields.io/maven-central/v/org.contextmapper/context-map-discovery.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.contextmapper%22%20AND%20a:%22context-map-discovery%22)

This repository contains a reverse engineering library for [Context Mapper](https://contextmapper.org). It allows to generate Context Maps written in the [Context Mapper DSL (CML)](https://github.com/ContextMapper/context-mapper-dsl) language from existing source code. It is extensible and allows to plug-in new Bounded Context or Context Map relationship discovery strategies.

## How it Works
The discovery process starts with searching for Bounded Contexts within your code base first. This is done by applying all configured Bounded Context discovery strategies. After that, the library searches for relationships between these discovered Bounded Contexts by applying the configured relationship discovery strategies. 

The strategies are provided by implementing the following two interfaces:

 * [BoundedContextDiscoveryStrategy](./src/main/java/org/contextmapper/discovery/strategies/boundedcontexts/BoundedContextDiscoveryStrategy.java)
 * [RelationshipDiscoveryStrategy](./src/main/java/org/contextmapper/discovery/strategies/relationships/RelationshipDiscoveryStrategy.java)

If the relationship discovery strategies identify Bounded Contexts with different names as they were previously discovered by the Bounded Context discovery strategies, you may need a _name mapping strategy_. They allow to map different Bounded Context names and can be provided by implementing the following interface:

 * [BoundedContextNameMappingStrategy](./src/main/java/org/contextmapper/discovery/strategies/names/BoundedContextNameMappingStrategy.java)

## Existing Strategies
This prototype already provides the following strategies to discover Bounded Contexts and relationships in order to generate a CML model. More strategies may be implemented in the future. Contributions are very welcome as well.

### Bounded Context Discovery Strategies
| Strategy                                                                                                                                                                 | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [SpringBootBoundedContextDiscoveryStrategy](./src/main/java/org/contextmapper/discovery/strategies/boundedcontexts/SpringBootBoundedContextDiscoveryStrategy.java)       | Many [Microservice projects](https://github.com/davidetaibi/Microservices_Project_List) implement their services with [Spring Boot](https://spring.io/projects/spring-boot). This strategy detects all Spring Boot services by searching for it's `@SpringBootApplication` annotation. This strategy further derives Aggregates from Spring REST endpoints (`@RequestMapping` annotation on controller classes) and entities from parameters and return types of the methods within the discovered controllers (`@RequestMapping`, `@PutMapping`, and `@GetMapping` on methods). **Note:** This strategy uses reflection to find all services (Bounded Contexts). To apply it, you have to add all your Spring Boot projects to the classpath. If you work with JAR's, ensure you provide regular JAR's not built with the Spring Boot Maven or Gradle plugins (the classes there are within the BOOT-INF directory and cannot be scanned by the [reflections library](https://github.com/ronmamo/reflections)). |
| [AnnotatedTypeBoundedContextDiscoveryStrategy](./src/main/java/org/contextmapper/discovery/strategies/boundedcontexts/AnnotatedTypeBoundedContextDiscoveryStrategy.java) | This strategy works in the same way as the _SpringBootBoundedContextDiscoveryStrategy_, but you can search for any annotation you want. For each class having the configured annotation, it will create a Bounded Context.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |

### Relationship Discovery Strategies
| Strategy                                                                                                                                                         | Description                                                                                                                                                                                                                                                                                                                                                                                                                 |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DockerComposeRelationshipDiscoveryStrategy](src/main/java/org/contextmapper/discovery/strategies/relationships/DockerComposeRelationshipDiscoveryStrategy.java) | [Microservice projects](https://github.com/davidetaibi/Microservices_Project_List) often use [Docker](https://www.docker.com/) containers to deploy the individual services. [Docker Compose](https://docs.docker.com/compose/) allows to start all services with one command. This strategy uses the docker-compose.yml file of your project to discover the dependencies (relationships) between the individual services. |

### Bounded Context Name Mapping Strategies
In case the configured relationship discovery strategies identify Bounded Contexts with different names as previously discovered by the Bounded Context discovery strategies, the following mapping strategies can help to map the different names:

| Strategy                                                                                                                                                                         | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DefaultBoundedContextNameMappingStrategy](./src/main/java/org/contextmapper/discovery/strategies/names/DefaultBoundedContextNameMappingStrategy.java)                           | The _DefaultBoundedContextNameMappingStrategy_ does not map anything and uses the names generated by the RelationshipDiscoveryStrategy to find the previously discovered Bounded Contexts. **Hint:** You don't need to add this strategy to your configuration, it is always added to the strategies used.                                                                                                                                                                                                                             |
| [SeparatorToCamelCaseBoundedContextNameMappingStrategy](./src/main/java/org/contextmapper/discovery/strategies/names/SeparatorToCamelCaseBoundedContextNameMappingStrategy.java) | This strategy can be used if the relationship discovery strategy finds names containing separators, while the Bounded Context strategies find the same names but in camel case format instead of using separators. It removes a configurable separator in the names and returns the names in camel case instead. For **example**: _customer-management-context_ is mapped to _CustomerManagementContext_, if the separator '-' is configured (_customer\_management\_context_ to CustomerManagementContext would be another example).  |

## Example
The following example illustrates how you can discover Context Maps from your code and generate a CML model:
```java
public class ExampleContextMapDiscoverer {

  public static void main(String[] args) throws IOException {
    // configure discoverer: (you can always add multiple discovery strategies as well)
    ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
        .usingBoundedContextDiscoveryStrategies(
            new SpringBootBoundedContextDiscoveryStrategy("org.your.organisation.root.package"))
        .usingRelationshipDiscoveryStrategies(
            new DockerComposeRelationshipDiscoveryStrategy(new File("./source-root-directory/")))
        .usingBoundedContextNameMappingStrategies(
            new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-")
        );

    // start discovery process creating a Context Map
    ContextMap contextmap = discoverer.discoverContextMap();

    // store Context Map as Context Mapper DSL (CML) model:
    new ContextMapSerializer().serializeContextMap(contextmap, new File("./src-gen/my-context-map.cml"));
  }

}
```

To use this library and discover CML models as in the example above, you can add it to your project with Maven or Gradle:

**Gradle:**
```gradle
implementation 'org.contextmapper:context-map-discovery:1.2.0'
```

**Maven:**
```xml
<dependency>
  <groupId>org.contextmapper</groupId>
  <artifactId>context-map-discovery</artifactId>
  <version>1.2.0</version>
</dependency>
```

### Example Projects
In the folder [Examples](./Examples) of this repository you find example microservice projects on which we applied this discovery library to reverse engineer CML Context Maps.

## Development / Build
If you want to contribute to this project you can create a fork and a pull request. The project is built with Gradle, so you can import it as Gradle project within Eclipse or IntelliJ IDEA (or any other IDE supporting Gradle).

## Contributing
Contribution is always welcome! Here are some ways how you can contribute:
 * Create Github issues if you find bugs or just want to give suggestions for improvements or new discovery strategies.
 * This is an open source project: if you want to code, [create pull requests](https://help.github.com/articles/creating-a-pull-request/) from [forks of this repository](https://help.github.com/articles/fork-a-repo/). Please refer to a Github issue if you contribute this way. 
 * If you want to contribute to our documentation and user guides on our website [https://contextmapper.org/](https://contextmapper.org/), create pull requests from forks of the corresponding page repo [https://github.com/ContextMapper/contextmapper.github.io](https://github.com/ContextMapper/contextmapper.github.io) or create issues [there](https://github.com/ContextMapper/contextmapper.github.io/issues).

## Licence
ContextMapper is released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

