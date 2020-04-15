# Lakeside Mutual Example
The [Lakeside Mutual](https://github.com/Microservice-API-Patterns/LakesideMutual/) project is a fictitious insurance company which serves as a sample application to demonstrate microservices and Domain-driven Design (DDD). 

We use the project here to illustrate how the Context Mapper discovery library can be used to reverse engineer a Context Mapping DSL (CML) model from existing source code. The project's backend services are based on [Spring Boot](https://spring.io/projects/spring-boot) and it provides a [Docker Compose](https://docs.docker.com/compose/) configuration to start all microservices. Thus, our [discovery strategies](https://github.com/ContextMapper/context-map-discovery#existing-strategies) support to reverse engineer a Context Map for the [Lakeside Mutual](https://github.com/Microservice-API-Patterns/LakesideMutual/) project.

## Lakeside Mutual Context Map Discovery
The class [src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java](./src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java) contains the code needed to discover the Context Map for the Lakeside Mutual project:

```java
public class LakesideMutualContextMapDiscoverer {

  public static void main(String[] args) throws IOException {
    // configure the discoverer
    ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
        .usingBoundedContextDiscoveryStrategies(
            new SpringBootBoundedContextDiscoveryStrategy("com.lakesidemutual"))
        .usingRelationshipDiscoveryStrategies(
            new DockerComposeRelationshipDiscoveryStrategy(new File(System.getProperty("user.home") + "/source/LakesideMutual/")))
        .usingBoundedContextNameMappingStrategies(
            new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-") {
              @Override
              public String mapBoundedContextName(String s) {
                // remove the "Backend" part of the Docker service names to map correctly...
                String name = super.mapBoundedContextName(s);
                return name.endsWith("Backend") ? name.substring(0, name.length() - 7) : name;
              }
            });

    // run the discovery process to get the Context Map
    ContextMap contextmap = discoverer.discoverContextMap();

    // serialize the Context Map to CML
    new ContextMapSerializer().serializeContextMap(contextmap, new File("./src-gen/lakesidemutual.cml"));
  }

}

```

**Spring Boot Bounded Context Discovery Strategy**:
The Spring Boot Context Map discovery strategy simply needs to know the package in which it shall search for the `@SpringBootApplication` annotation. For this example, we scan `com.lakesidemutual`.

**Docker Compose Relationship Discovery Strategy**:
The Docker Compose strategy needs to know the root directory of the cloned Lakeside Mutual project. Within this directory it will search for docker-compose.yml files to analyze the relationships (dependencies).

**Bounded Context Name Mapping Strategy**:
In our example illustrated above we use an adjusted [SeparatorToCamelCaseBoundedContextNameMappingStrategy](https://github.com/ContextMapper/context-map-discovery#bounded-context-name-mapping-strategies) strategy, since the services in the docker-compose.yml files are named in the format _customer-core_ while the Spring Boot discovery strategy will use the application class name in the format _CustomerCore_. The adjusted name mapping strategy illustrated above further removes the "Backend" strings at the end, since the Spring Boot discovery strategy will detect the Bounded Contexts without this ending.

**Resulting Model**:

The example code above finally stores the discovered Context Map under [src-gen/lakesidemutual.cml](./src-gen/lakesidemutual.cml).

## Procedure to Run this Example
1. Clone the [Lakeside Mutual](https://github.com/Microservice-API-Patterns/LakesideMutual) project.
2. Build all backend (Java) projects with the provided Maven build and publish them to your local Maven repository: 

   `mvn clean install`

   **Note:** Spring Boot works with it's own special class loader and the JAR's produced by the given Maven Builds are not scannable for the [reflections library](https://github.com/ronmamo/reflections) (classes located within BOOT-INF). In order to build real JAR archives, you first have to disable the _spring-boot-maven-plugin_ in all these backend projects (one solution is to simply comment the section out in the pom.xml files).
   
3. Clone this example project and import it within your favorite IDE (import Gradle project).
4. Adjust the source root path in the [LakesideMutualContextMapDiscoverer](./src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java) class to the location where you cloned the Lakeside Mutual project (DockerComposeRelationshipDiscoveryStrategy).
5. Run the [LakesideMutualContextMapDiscoverer](./src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java) main method to generate the Context Map.

**Note:** Of course there are other ways how to run the example main method. You just have to ensure that your classpath contains our discovery library (find Maven or Gradle include snippets [here](https://github.com/ContextMapper/context-map-discovery#example)) and all Lakeside Mutual microservices you want to discover (for the Spring Boot discovery strategy). We added all the projects to the classpath with Gradle here (see [build.gradle](./build.gradle)).
