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

The example code above finally stores the discovered Context Map under [src-gen/lakesidemutual.cml](./src-gen/lakesidemutual.cml). This is the resulting CML model:

```
ContextMap {
  contains PolicyManagement
  contains CustomerManagement
  contains CustomerSelfService
  contains CustomerCore

  CustomerCore -> PolicyManagement

  CustomerCore -> CustomerManagement

  PolicyManagement -> CustomerSelfService

  CustomerCore -> CustomerSelfService

}

BoundedContext PolicyManagement {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.policymanagement.interfaces.RiskComputationService.
  Aggregate riskfactor
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.policymanagement.interfaces.InsuranceQuoteRequestInformationHolder.
  Aggregate PolicyManagement_insurance_quote_requests {
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto.
    Entity PolicyManagement_insurance_quote_requests_InsuranceQuoteRequestDto {
      String policyId
      List<RequestStatusChangeDto> statusHistory
      CustomerInfoDto customerInfo
      InsuranceOptionsDto insuranceOptions
      Date date
      InsuranceQuoteDto insuranceQuote
      Long id
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.policymanagement.interfaces.PolicyInformationHolder.
  Aggregate policies {
    // This entity has been derived from the class com.lakesidemutual.policymanagement.domain.policy.PolicyId.
    Entity PolicyId {
      long serialVersionUID
      String id
    }
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto.
    Entity PolicyDto {
      List<Link> links
      PolicyPeriodDto policyPeriod
      String policyId
      MoneyAmountDto policyLimit
      List<String> expandable
      Object customer
      Date creationDate
      InsuringAgreementDto insuringAgreement
      String policyType
      MoneyAmountDto deductible
      MoneyAmountDto insurancePremium
    }
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.CreatePolicyRequestDto.
    Entity CreatePolicyRequestDto {
      MoneyAmountDto insurancePremium
      InsuringAgreementDto insuringAgreement
      MoneyAmountDto policyLimit
      PolicyPeriodDto policyPeriod
      String customerId
      String policyType
      MoneyAmountDto deductible
    }
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.PaginatedPolicyResponseDto.
    Entity PaginatedPolicyResponseDto {
      int limit
      List<Link> links
      int offset
      int ^size
      - List<PolicyDto> policies
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.policymanagement.interfaces.CustomerInformationHolder.
  Aggregate PolicyManagement_customers {
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerIdDto.
    Entity CustomerIdDto {
      String id
    }
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.PaginatedCustomerResponseDto.
    Entity PolicyManagement_customers_PaginatedCustomerResponseDto {
      int offset
      int ^size
      String filter
      int limit
      List<Link> links
      - List<PolicyManagement_customers_CustomerDto> customers
    }
    // This entity has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto.
    Entity PolicyManagement_customers_CustomerDto {
      CustomerProfileDto customerProfile
      String customerId
      List<Link> links
    }
  }
}

BoundedContext CustomerManagement {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customermanagement.interfaces.CustomerInformationHolder.
  Aggregate CustomerManagement_customers {
    // This entity has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto.
    Entity CustomerManagement_customers_CustomerDto {
      String customerId
      List<Link> links
      - CustomerProfileDto customerProfile
    }
    // This entity has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto.
    Entity CustomerProfileDto {
      Date birthday
      List<AddressDto> moveHistory
      String firstname
      AddressDto currentAddress
      String phoneNumber
      String lastname
      String ^email
    }
    // This entity has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto.
    Entity CustomerManagement_customers_PaginatedCustomerResponseDto {
      int offset
      int ^size
      String filter
      int limit
      List<Link> links
      - List<CustomerManagement_customers_CustomerDto> customers
    }
    // This entity has been derived from the class com.lakesidemutual.customermanagement.domain.customer.CustomerId.
    Entity CustomerManagement_customers_CustomerId {
      long serialVersionUID
      String id
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customermanagement.interfaces.InteractionLogInformationHolder.
  Aggregate interaction_logs {
    // This entity has been derived from the class com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogAggregateRoot.
    Entity InteractionLogAggregateRoot {
      Collection<InteractionEntity> interactions
      String lastAcknowledgedInteractionId
      String customerId
      String username
    }
    // This entity has been derived from the class com.lakesidemutual.customermanagement.domain.customer.CustomerId.
    Entity interaction_logs_CustomerId
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customermanagement.interfaces.NotificationInformationHolder.
  Aggregate notifications
}

BoundedContext CustomerSelfService {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customerselfservice.interfaces.AuthenticationController.
  Aggregate auth
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customerselfservice.interfaces.CityStaticDataHolder.
  Aggregate cities {
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.city.CitiesResponseDto.
    Entity CitiesResponseDto {
      List<String> cities
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customerselfservice.interfaces.InsuranceQuoteRequestInformationHolder.
  Aggregate insurance_quote_requests {
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto.
    Entity InsuranceQuoteRequestDto {
      InsuranceQuoteDto insuranceQuote
      Long id
      String policyId
      List<RequestStatusChangeDto> statusHistory
      Date date
      InsuranceOptionsDto insuranceOptions
      CustomerInfoDto customerInfo
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customerselfservice.interfaces.CustomerInformationHolder.
  Aggregate customers {
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto.
    Entity AddressDto {
      String city
      String postalCode
      String streetAddress
    }
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto.
    Entity CustomerDto {
      CustomerProfileDto customerProfile
      List<Link> links
      String customerId
    }
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.domain.customer.CustomerId.
    Entity CustomerId {
      long serialVersionUID
      String id
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customerselfservice.interfaces.UserInformationHolder.
  Aggregate user {
    // This entity has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserResponseDto.
    Entity UserResponseDto {
      String customerId
      String ^email
    }
  }
}

BoundedContext CustomerCore {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customercore.interfaces.CityStaticDataHolder.
  Aggregate CustomerCore_cities {
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.city.CitiesResponseDto.
    Entity CustomerCore_cities_CitiesResponseDto {
      List<String> cities
    }
  }
  // This Aggregate has been created on the basis of the Spring REST controller com.lakesidemutual.customercore.interfaces.CustomerInformationHolder.
  Aggregate CustomerCore_customers {
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerResponseDto.
    Entity CustomerResponseDto {
      Collection<Address> moveHistory
      String firstname
      String lastname
      String city
      List<Link> links
      String phoneNumber
      Date birthday
      String postalCode
      String customerId
      String ^email
      String streetAddress
    }
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerProfileUpdateRequestDto.
    Entity CustomerProfileUpdateRequestDto {
      String ^email
      String postalCode
      String streetAddress
      String firstname
      String phoneNumber
      String lastname
      Date birthday
      String city
    }
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomersResponseDto.
    Entity CustomersResponseDto {
      List<Link> links
      - List<CustomerResponseDto> customers
    }
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.AddressDto.
    Entity CustomerCore_customers_AddressDto {
      String city
      String postalCode
      String streetAddress
    }
    // This entity has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.PaginatedCustomerResponseDto.
    Entity PaginatedCustomerResponseDto {
      int ^size
      int offset
      List<Link> links
      int limit
      String filter
      - List<CustomerResponseDto> customers
    }
    // This entity has been derived from the class com.lakesidemutual.customercore.domain.customer.CustomerId.
    Entity CustomerCore_customers_CustomerId {
      String id
      long serialVersionUID
    }
  }
}


```

## Procedure to Run this Example
1. Clone the [Lakeside Mutual](https://github.com/Microservice-API-Patterns/LakesideMutual) project.
2. Build all backend (Java) projects with the provided Maven build and publish them to your local Maven repository: 

   `mvn clean install`

   **Note:** Spring Boot works with it's own special class loader and the JAR's produced by the given Maven Builds are not scannable for the [reflections library](https://github.com/ronmamo/reflections) (classes located within BOOT-INF). In order to build real JAR archives, you first have to disable the _spring-boot-maven-plugin_ in all these backend projects (one solution is to simply comment the section out in the pom.xml files).
   
3. Clone this example project and import it within your favorite IDE (import Gradle project).
4. Adjust the source root path in the [LakesideMutualContextMapDiscoverer](./src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java) class to the location where you cloned the Lakeside Mutual project (DockerComposeRelationshipDiscoveryStrategy).
5. Run the [LakesideMutualContextMapDiscoverer](./src/main/java/org/contextmapper/lakesidemutual/example/LakesideMutualContextMapDiscoverer.java) main method to generate the Context Map.

**Note:** Of course there are other ways how to run the example main method. You just have to ensure that your classpath contains our discovery library (find Maven or Gradle include snippets [here](https://github.com/ContextMapper/context-map-discovery#example)) and all Lakeside Mutual microservices you want to discover (for the Spring Boot discovery strategy). We added all the projects to the classpath with Gradle here (see [build.gradle](./build.gradle)).
