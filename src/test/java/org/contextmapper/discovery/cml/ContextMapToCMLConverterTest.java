/*
 * Copyright 2019 The Context Mapper Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.contextmapper.discovery.cml;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.model.DomainObject;
import org.contextmapper.discovery.strategies.boundedcontexts.OASBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.boundedcontexts.OASBoundedContextDiscoveryStrategyTest;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.DockerComposeRelationshipDiscoveryStrategy;
import org.contextmapper.dsl.contextMappingDSL.*;
import org.contextmapper.tactic.dsl.tacticdsl.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ContextMapToCMLConverterTest {

    @Test
    public void canConvertToCMLModel() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"))
                .usingRelationshipDiscoveryStrategies(
                        new DockerComposeRelationshipDiscoveryStrategy(new File("./src/test/resources/test/microservice/spring-boot")))
                .usingBoundedContextNameMappingStrategies(
                        new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-")
                );

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(discoverer.discoverContextMap());
        ContextMap contextMap = model.getMap();

        // then
        assertNotNull(contextMap);
        assertEquals(2, model.getBoundedContexts().size());
        assertEquals(2, contextMap.getBoundedContexts().size());
        assertEquals(1, contextMap.getRelationships().size());
        UpstreamDownstreamRelationship relationship = (UpstreamDownstreamRelationship) contextMap.getRelationships().get(0);
        assertEquals("Microservice1", relationship.getUpstream().getName());
        assertEquals("Microservice2", relationship.getDownstream().getName());
        assertEquals(1, relationship.getUpstreamExposedAggregates().size());
        assertEquals("customers", relationship.getUpstreamExposedAggregates().iterator().next().getName());
        assertTrue(relationship.getExposedAggregatesComment().startsWith("// "));
    }

    @Test
    public void canConvertAggregates() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        assertEquals(1, model.getBoundedContexts().size());
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().get(0);
        assertEquals("customers", aggregate.getName());
        assertEquals("/* This Aggregate has been created on the basis of the RESTful HTTP controller test.application.spring.boot.interfaces.CustomerInformationHolder. */", aggregate.getComment());
    }

    @Test
    public void canConvertDomainObjects() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        assertEquals(1, model.getBoundedContexts().size());
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().get(0);
        assertEquals("customers", aggregate.getName());
        Set<ValueObject> resourceValueObjects = aggregate.getDomainObjects().stream().filter(o -> o instanceof ValueObject).map(o -> (ValueObject) o).collect(Collectors.toSet());
        assertEquals(3, resourceValueObjects.size());
        ValueObject addressValueObject = (ValueObject) resourceValueObjects.stream().filter(o -> o.getName().equals("Address")).findFirst().get();
        assertNotNull(addressValueObject);
        assertNotNull(resourceValueObjects.stream().filter(o -> o.getName().equals("CustomerId")).findFirst().get());
        assertNotNull(resourceValueObjects.stream().filter(o -> o.getName().equals("Customer")).findFirst().get());
        assertEquals("/* This value object has been derived from the class test.application.spring.boot.model.Address. */", addressValueObject.getComment());
    }

    @Test
    public void canConvertRootEntity() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        Aggregate aggregate = bc.getAggregates().get(0);
        Set<Entity> entities = aggregate.getDomainObjects().stream().filter(o -> o instanceof Entity).map(o -> (Entity) o).collect(Collectors.toSet());
        assertEquals(1, entities.size());
        Entity rootEntity = entities.iterator().next();
        assertEquals("customers_RootEntity", rootEntity.getName());
        assertTrue(rootEntity.isAggregateRoot());
    }

    @Test
    public void canConvertRootEntityMethods() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        Aggregate aggregate = bc.getAggregates().get(0);
        Set<Entity> entities = aggregate.getDomainObjects().stream().filter(o -> o instanceof Entity).map(o -> (Entity) o).collect(Collectors.toSet());
        Entity rootEntity = entities.iterator().next();
        assertEquals(4, rootEntity.getOperations().size());
        DomainObjectOperation changeAddress = rootEntity.getOperations().stream().filter(o -> o.getName().equals("changeAddress")).findFirst().get();
        DomainObjectOperation getCustomer = rootEntity.getOperations().stream().filter(o -> o.getName().equals("getCustomer")).findFirst().get();
        DomainObjectOperation getCustomers = rootEntity.getOperations().stream().filter(o -> o.getName().equals("getCustomers")).findFirst().get();
        DomainObjectOperation deleteCustomer = rootEntity.getOperations().stream().filter(o -> o.getName().equals("deleteCustomer")).findFirst().get();
        assertNotNull(changeAddress);
        assertNotNull(getCustomer);
        assertNotNull(getCustomers);
        assertNotNull(deleteCustomer);
        assertEquals("Address", changeAddress.getReturnType().getDomainObjectType().getName());
        assertEquals("Customer", getCustomer.getReturnType().getDomainObjectType().getName());
        assertEquals("Customer", getCustomers.getReturnType().getDomainObjectType().getName());
        assertEquals("List", getCustomers.getReturnType().getCollectionType().getName());
        assertNull(deleteCustomer.getReturnType());
        Parameter customerIdParam = changeAddress.getParameters().stream().filter(p -> p.getName().equals("arg0")).findFirst().get();
        Parameter requestDtoParam = changeAddress.getParameters().stream().filter(p -> p.getName().equals("arg1")).findFirst().get();
        Parameter customerIdParam2 = getCustomer.getParameters().stream().filter(p -> p.getName().equals("arg0")).findFirst().get();
        Parameter customerIdsParam = getCustomers.getParameters().stream().filter(p -> p.getName().equals("arg0")).findFirst().get();
        Parameter deleteCustomerIdParam = deleteCustomer.getParameters().stream().filter(p -> p.getName().equals("arg0")).findFirst().get();
        assertNotNull(customerIdParam);
        assertNotNull(requestDtoParam);
        assertNotNull(customerIdParam2);
        assertNotNull(customerIdsParam);
        assertNotNull(deleteCustomerIdParam);
        assertEquals("CustomerId", customerIdParam.getParameterType().getDomainObjectType().getName());
        assertEquals("Address", requestDtoParam.getParameterType().getDomainObjectType().getName());
        assertEquals("CustomerId", customerIdParam2.getParameterType().getDomainObjectType().getName());
        assertEquals("CustomerId", customerIdsParam.getParameterType().getDomainObjectType().getName());
        assertEquals("List", customerIdsParam.getParameterType().getCollectionType().getName());
        assertEquals("CustomerId", deleteCustomerIdParam.getParameterType().getDomainObjectType().getName());
    }

    @Test
    public void canConvertAttributes() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        assertEquals(1, model.getBoundedContexts().size());
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().get(0);
        ValueObject addressValueObject = (ValueObject) aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressValueObject);
        assertEquals(4, addressValueObject.getAttributes().size());
        assertNotNull(addressValueObject.getAttributes().stream().filter(a -> a.getName().equals("street")).findFirst().get());
    }

    @Test
    public void canConvertReferences() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        assertEquals(1, model.getBoundedContexts().size());
        BoundedContext bc = model.getBoundedContexts().iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().get(0);
        ValueObject customerValueObject = (ValueObject) aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        assertNotNull(customerValueObject);
        assertEquals(4, customerValueObject.getReferences().size());
        assertEquals("CustomerId", customerValueObject.getReferences().stream().filter(r -> r.getName().equals("id")).findFirst().get()
                .getDomainObjectType().getName());
    }

    @Test
    public void canConvertServices() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(OASBoundedContextDiscoveryStrategyTest.SAMPLE_CONTRACT_LOCATION));
        org.contextmapper.discovery.model.ContextMap contextMap = discoverer.discoverContextMap();

        // when
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);

        // then
        assertEquals(1, model.getBoundedContexts().size());
        BoundedContext bc = model.getBoundedContexts().get(0);
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().get(0);
        assertEquals(1, aggregate.getServices().size());
        Service service = aggregate.getServices().get(0);
        assertEquals(3, service.getOperations().size());
    }

}
