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

  CustomerCore -> PolicyManagement {
    exposedAggregates cities, CustomerCore_customers // The list of exposed Aggregates may contain Aggregates which are not used by the downstream (discovery strategy simply added all Aggregates).
  }

  CustomerCore -> CustomerManagement {
    exposedAggregates cities, CustomerCore_customers // The list of exposed Aggregates may contain Aggregates which are not used by the downstream (discovery strategy simply added all Aggregates).
  }

  PolicyManagement -> CustomerSelfService {
    exposedAggregates insurance_quote_requests, customers, riskfactor, policies // The list of exposed Aggregates may contain Aggregates which are not used by the downstream (discovery strategy simply added all Aggregates).
  }

  CustomerCore -> CustomerSelfService {
    exposedAggregates cities, CustomerCore_customers // The list of exposed Aggregates may contain Aggregates which are not used by the downstream (discovery strategy simply added all Aggregates).
  }

}

BoundedContext PolicyManagement {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.policymanagement.interfaces.InsuranceQuoteRequestInformationHolder.
  Aggregate insurance_quote_requests {
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto.
    ValueObject InsuranceQuoteRequestDto {
      List<RequestStatusChangeDto> statusHistory
      String policyId
      InsuranceOptionsDto insuranceOptions
      CustomerInfoDto customerInfo
      Long id
      InsuranceQuoteDto insuranceQuote
      Date date
    }
    Entity insurance_quote_requests_RootEntity {
      aggregateRoot
      def List<@InsuranceQuoteRequestDto> getInsuranceQuoteRequests;
      def @InsuranceQuoteRequestDto getInsuranceQuoteRequest;
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.policymanagement.interfaces.CustomerInformationHolder.
  Aggregate customers {
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerIdDto.
    ValueObject CustomerIdDto {
      String id
    }
    Entity customers_RootEntity {
      aggregateRoot
      def @CustomerDto getCustomer (@CustomerIdDto customerIdDto);
      def List<@customers_PolicyDto> getPolicies (@CustomerIdDto customerIdDto);
      def @PaginatedCustomerResponseDto getCustomers;
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto.
    ValueObject CustomerDto {
      String customerId
      CustomerProfileDto customerProfile
      List<Link> links
      - List<CustomerDto> customers
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto.
    ValueObject customers_PolicyDto {
      Object customer
      Date creationDate
      String policyType
      InsuringAgreementDto insuringAgreement
      MoneyAmountDto insurancePremium
      List<Link> links
      MoneyAmountDto policyLimit
      PolicyPeriodDto policyPeriod
      String policyId
      MoneyAmountDto deductible
      List<String> expandable
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.customer.PaginatedCustomerResponseDto.
    ValueObject PaginatedCustomerResponseDto {
      int offset
      int ^size
      String filter
      int limit
      List<Link> links
      - List<CustomerDto> customers
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.policymanagement.interfaces.RiskComputationService.
  Aggregate riskfactor {
    Entity riskfactor_RootEntity {
      aggregateRoot
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.policymanagement.interfaces.PolicyInformationHolder.
  Aggregate policies {
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto.
    ValueObject PolicyDto {
      MoneyAmountDto insurancePremium
      List<String> expandable
      String policyId
      String policyType
      MoneyAmountDto deductible
      List<Link> links
      MoneyAmountDto policyLimit
      PolicyPeriodDto policyPeriod
      InsuringAgreementDto insuringAgreement
      Date creationDate
      Object customer
      - List<PolicyDto> policies
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.CreatePolicyRequestDto.
    ValueObject CreatePolicyRequestDto {
      MoneyAmountDto insurancePremium
      InsuringAgreementDto insuringAgreement
      MoneyAmountDto policyLimit
      PolicyPeriodDto policyPeriod
      String customerId
      String policyType
      MoneyAmountDto deductible
    }
    Entity policies_RootEntity {
      aggregateRoot
      def @PolicyDto getPolicy (@PolicyId policyId);
      def @PolicyDto updatePolicy (@CreatePolicyRequestDto createPolicyDto, @PolicyId policyId);
      def @PaginatedPolicyResponseDto getPolicies;
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.interfaces.dtos.policy.PaginatedPolicyResponseDto.
    ValueObject PaginatedPolicyResponseDto {
      int limit
      List<Link> links
      int offset
      int ^size
      - List<PolicyDto> policies
    }
    // This value object has been derived from the class com.lakesidemutual.policymanagement.domain.policy.PolicyId.
    ValueObject PolicyId {
      long serialVersionUID
      String id
    }
  }
}

BoundedContext CustomerManagement {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customermanagement.interfaces.CustomerInformationHolder.
  Aggregate CustomerManagement_customers {
    // This value object has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto.
    ValueObject CustomerManagement_customers_PaginatedCustomerResponseDto {
      String filter
      int ^size
      int offset
      List<Link> links
      int limit
      - List<CustomerManagement_customers_CustomerDto> customers
    }
    // This value object has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto.
    ValueObject CustomerManagement_customers_CustomerDto {
      List<Link> links
      String customerId
      - CustomerProfileDto customerProfile - List<CustomerManagement_customers_CustomerDto> customers
    }
    // This value object has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto.
    ValueObject CustomerProfileDto {
      List<AddressDto> moveHistory
      String firstname
      String phoneNumber
      Date birthday
      String lastname
      String ^email
      AddressDto currentAddress
      - CustomerProfileDto customerProfile
    }
    Entity CustomerManagement_customers_RootEntity {
      aggregateRoot
      def @CustomerManagement_customers_PaginatedCustomerResponseDto getCustomers;
      def @CustomerManagement_customers_CustomerDto getCustomer (@CustomerManagement_customers_CustomerId customerId);
      def @CustomerManagement_customers_CustomerDto updateCustomer (@CustomerManagement_customers_CustomerId customerId, @CustomerProfileDto customerProfile);
    }
    // This value object has been derived from the class com.lakesidemutual.customermanagement.domain.customer.CustomerId.
    ValueObject CustomerManagement_customers_CustomerId {
      long serialVersionUID
      String id
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customermanagement.interfaces.NotificationInformationHolder.
  Aggregate notifications {
    Entity notifications_RootEntity {
      aggregateRoot
      def List<@NotificationDto> getNotifications;
    }
    // This value object has been derived from the class com.lakesidemutual.customermanagement.interfaces.dtos.NotificationDto.
    ValueObject NotificationDto {
      int count
      String customerId
      String username
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customermanagement.interfaces.InteractionLogInformationHolder.
  Aggregate interaction_logs {
    // This value object has been derived from the class com.lakesidemutual.customermanagement.domain.customer.CustomerId.
    ValueObject interaction_logs_CustomerId {
      long serialVersionUID
      String id
    }
    // This value object has been derived from the class com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogAggregateRoot.
    ValueObject InteractionLogAggregateRoot {
      String customerId
      String lastAcknowledgedInteractionId
      String username
      Collection<InteractionEntity> interactions
    }
    Entity interaction_logs_RootEntity {
      aggregateRoot
      def @InteractionLogAggregateRoot getInteractionLog (@interaction_logs_CustomerId customerId);
    }
  }
}

BoundedContext CustomerSelfService {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customerselfservice.interfaces.AuthenticationController.
  Aggregate auth {
    Entity auth_RootEntity {
      aggregateRoot
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customerselfservice.interfaces.InsuranceQuoteRequestInformationHolder.
  Aggregate CustomerSelfService_insurance_quote_requests {
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto.
    ValueObject CustomerSelfService_insurance_quote_requests_InsuranceQuoteRequestDto {
      List<RequestStatusChangeDto> statusHistory
      Long id
      Date date
      InsuranceQuoteDto insuranceQuote
      InsuranceOptionsDto insuranceOptions
      CustomerInfoDto customerInfo
      String policyId
    }
    Entity CustomerSelfService_insurance_quote_requests_RootEntity {
      aggregateRoot
      def @CustomerSelfService_insurance_quote_requests_InsuranceQuoteRequestDto getInsuranceQuoteRequest;
      def List<@CustomerSelfService_insurance_quote_requests_InsuranceQuoteRequestDto> getInsuranceQuoteRequests;
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customerselfservice.interfaces.CityStaticDataHolder.
  Aggregate CustomerSelfService_cities {
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.city.CitiesResponseDto.
    ValueObject CustomerSelfService_cities_CitiesResponseDto {
      List<String> cities
    }
    Entity CustomerSelfService_cities_RootEntity {
      aggregateRoot
      def @CustomerSelfService_cities_CitiesResponseDto getCitiesForPostalCode;
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customerselfservice.interfaces.UserInformationHolder.
  Aggregate user {
    Entity user_RootEntity {
      aggregateRoot
      def @UserResponseDto getCurrentUser;
    }
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserResponseDto.
    ValueObject UserResponseDto {
      String ^email
      String customerId
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customerselfservice.interfaces.CustomerInformationHolder.
  Aggregate CustomerSelfService_customers {
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto.
    ValueObject CustomerSelfService_customers_InsuranceQuoteRequestDto {
      String policyId
      List<RequestStatusChangeDto> statusHistory
      CustomerInfoDto customerInfo
      InsuranceOptionsDto insuranceOptions
      Long id
      InsuranceQuoteDto insuranceQuote
      Date date
    }
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto.
    ValueObject CustomerSelfService_customers_CustomerDto {
      String customerId
      List<Link> links
      CustomerProfileDto customerProfile
    }
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.domain.customer.CustomerId.
    ValueObject CustomerSelfService_customers_CustomerId {
      String id
      long serialVersionUID
    }
    Entity CustomerSelfService_customers_RootEntity {
      aggregateRoot
      def @CustomerSelfService_customers_AddressDto changeAddress (@CustomerSelfService_customers_CustomerId customerId, @CustomerSelfService_customers_AddressDto requestDto);
      def @CustomerSelfService_customers_CustomerDto getCustomer (@CustomerSelfService_customers_CustomerId customerId);
      def List<@CustomerSelfService_customers_InsuranceQuoteRequestDto> getInsuranceQuoteRequests (@CustomerSelfService_customers_CustomerId customerId);
    }
    // This value object has been derived from the class com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto.
    ValueObject CustomerSelfService_customers_AddressDto {
      String streetAddress
      String city
      String postalCode
    }
  }
}

BoundedContext CustomerCore {
  implementationTechnology "Spring Boot"
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customercore.interfaces.CityStaticDataHolder.
  Aggregate cities {
    Entity cities_RootEntity {
      aggregateRoot
      def @CitiesResponseDto getCitiesForPostalCode;
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.city.CitiesResponseDto.
    ValueObject CitiesResponseDto {
      List<String> cities
    }
  }
  // This Aggregate has been created on the basis of the RESTful HTTP controller com.lakesidemutual.customercore.interfaces.CustomerInformationHolder.
  Aggregate CustomerCore_customers {
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.AddressDto.
    ValueObject AddressDto {
      String streetAddress
      String city
      String postalCode
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.domain.customer.CustomerId.
    ValueObject CustomerId {
      String id
      long serialVersionUID
    }
    Entity CustomerCore_customers_RootEntity {
      aggregateRoot
      def @CustomerResponseDto updateCustomer (@CustomerId customerId, @CustomerProfileUpdateRequestDto requestDto);
      def @CustomersResponseDto getCustomer;
      def @AddressDto changeAddress (@AddressDto requestDto, @CustomerId customerId);
      def @CustomerCore_customers_PaginatedCustomerResponseDto getCustomers;
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerProfileUpdateRequestDto.
    ValueObject CustomerProfileUpdateRequestDto {
      String lastname
      Date birthday
      String phoneNumber
      String streetAddress
      String city
      String ^email
      String postalCode
      String firstname
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerResponseDto.
    ValueObject CustomerResponseDto {
      String phoneNumber
      String lastname
      String streetAddress
      Collection<Address> moveHistory
      String firstname
      String ^email
      String city
      String postalCode
      Date birthday
      String customerId
      List<Link> links
      - List<CustomerResponseDto> customers - List<CustomerResponseDto> customers
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.CustomersResponseDto.
    ValueObject CustomersResponseDto {
      List<Link> links
      - List<CustomerResponseDto> customers
    }
    // This value object has been derived from the class com.lakesidemutual.customercore.interfaces.dtos.customer.PaginatedCustomerResponseDto.
    ValueObject CustomerCore_customers_PaginatedCustomerResponseDto {
      List<Link> links
      String filter
      int limit
      int offset
      int ^size
      - List<CustomerResponseDto> customers
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
