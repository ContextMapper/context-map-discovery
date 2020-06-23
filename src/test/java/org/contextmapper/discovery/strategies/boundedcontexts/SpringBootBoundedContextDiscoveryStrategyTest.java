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
package org.contextmapper.discovery.strategies.boundedcontexts;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SpringBootBoundedContextDiscoveryStrategyTest {

    @Test
    public void canDiscoverSpringBootApplication() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals("TestSpringBoot", bc.getName());
        assertEquals("Spring Boot", bc.getTechnology());
    }

    @Test
    public void canDiscoverResourcesAsAggregates() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().iterator().next();
        assertEquals("customers", aggregate.getName());
    }

    @Test
    public void canCreateAggregateRootEntity() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate customers = bc.getAggregates().stream().filter(a -> a.getName().equals("customers")).findFirst().get();
        Set<DomainObject> domainObjects = customers.getDomainObjects().stream().filter(o -> o.getType().equals(DomainObjectType.ENTITY)).collect(Collectors.toSet());
        assertEquals(1, domainObjects.size());
        assertEquals("customers_RootEntity", domainObjects.iterator().next().getName());
    }

    @Test
    public void canDiscoverDomainObjectsFromResourceMethods() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().iterator().next();
        assertEquals("customers", aggregate.getName());
        Set<DomainObject> resourceDomainObject = aggregate.getDomainObjects().stream().filter(o -> o.getType().equals(DomainObjectType.VALUE_OBJECT)).collect(Collectors.toSet());
        assertEquals(3, resourceDomainObject.size());
        assertTrue(resourceDomainObject.contains(new DomainObject(DomainObjectType.VALUE_OBJECT, "Address", "test.application.spring.boot.model.Address")));
        assertTrue(resourceDomainObject.contains(new DomainObject(DomainObjectType.VALUE_OBJECT, "CustomerId", "test.application.spring.boot.model.CustomerId")));
        assertTrue(resourceDomainObject.contains(new DomainObject(DomainObjectType.VALUE_OBJECT, "Customer", "test.application.spring.boot.model.Customer")));
    }

    @Test
    public void canCreateAggregateRootMethodsFromResourceMethods() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject aggregateRoot = aggregate.getDomainObjects().stream().filter(o -> o.getType().equals(DomainObjectType.ENTITY)).findFirst().get();
        assertEquals(4, aggregateRoot.getMethods().size());
        Method changeAddress = aggregateRoot.getMethods().stream().filter(m -> m.getName().equals("changeAddress")).findFirst().get();
        Method getCustomer = aggregateRoot.getMethods().stream().filter(m -> m.getName().equals("getCustomer")).findFirst().get();
        Method getCustomers = aggregateRoot.getMethods().stream().filter(m -> m.getName().equals("getCustomers")).findFirst().get();
        Method deleteCustomer = aggregateRoot.getMethods().stream().filter(m -> m.getName().equals("deleteCustomer")).findFirst().get();
        assertNotNull(changeAddress);
        assertNotNull(getCustomer);
        assertNotNull(getCustomers);
        assertNotNull(deleteCustomer);
        assertEquals("Address", changeAddress.getReturnType().getName());
        assertEquals("Customer", getCustomer.getReturnType().getName());
        assertEquals("Customer", getCustomers.getReturnType().getName());
        assertEquals("List", getCustomers.getReturnType().getCollectionType());
        assertNull(deleteCustomer.getReturnType());
        Set<String> changeAddressParameterTypes = changeAddress.getParameters().stream().map(p -> p.getType().getName()).collect(Collectors.toSet());
        Set<String> getCustomerParameterTypes = getCustomer.getParameters().stream().map(p -> p.getType().getName()).collect(Collectors.toSet());
        Set<String> getCustomersParameterTypes = getCustomer.getParameters().stream().map(p -> p.getType().getName()).collect(Collectors.toSet());
        Set<String> getCustomersParameterCollectionTypes = getCustomers.getParameters().stream()
                .filter(p -> p.getType().isCollectionType())
                .map(p -> p.getType().getCollectionType()).collect(Collectors.toSet());
        assertTrue(changeAddressParameterTypes.contains("CustomerId"));
        assertTrue(changeAddressParameterTypes.contains("Address"));
        assertTrue(getCustomerParameterTypes.contains("CustomerId"));
        assertTrue(getCustomersParameterTypes.contains("CustomerId"));
        assertTrue(getCustomersParameterCollectionTypes.contains("List"));
    }

    @Test
    public void canDiscoverDomainObjectAttributes() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject addressDomainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressDomainObject);
        assertEquals(4, addressDomainObject.getAttributes().size());
        Attribute streetAttribute = addressDomainObject.getAttributes().stream().filter(a -> a.getName().equals("street")).findFirst().get();
        assertNotNull(streetAttribute);
        assertEquals("street", streetAttribute.getName());
        assertEquals("String", streetAttribute.getType().getName());
        Attribute plzAttribute = addressDomainObject.getAttributes().stream().filter(a -> a.getName().equals("plz")).findFirst().get();
        assertNotNull(plzAttribute);
        assertEquals("plz", plzAttribute.getName());
        assertEquals("int", plzAttribute.getType().getName());
    }

    @Test
    public void canDiscoverDomainObjectReferences() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject customerDomainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        DomainObject customerIdDomainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("CustomerId")).findFirst().get();
        assertNotNull(customerDomainObject);
        assertNotNull(customerIdDomainObject);
        Set<Attribute> singleReferences = customerDomainObject.getAttributes().stream().filter(a -> a.getType().isDomainObjectType()).filter(a -> !a.getType().isCollectionType()).collect(Collectors.toSet());
        assertEquals(1, singleReferences.size());
        assertEquals(customerIdDomainObject, singleReferences.iterator().next().getType().getDomainObjectType());
    }

    @Test
    public void canHandleCollectionTypes() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject customerDomainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        assertNotNull(customerDomainObject);
        assertEquals(4, customerDomainObject.getAttributes().size());
        Set<Attribute> collectionReferences = customerDomainObject.getAttributes().stream().filter(a -> a.getType().isCollectionType()).collect(Collectors.toSet());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getType().getCollectionType().equals("List")).findFirst().get().getType().getName());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getType().getCollectionType().equals("Set")).findFirst().get().getType().getName());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getType().getCollectionType().equals("Collection")).findFirst().get().getType().getName());
    }

    @Test
    public void canHandleArrays() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject addressDomainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressDomainObject);
        Attribute arrayAttribute = addressDomainObject.getAttributes().stream().filter(a -> a.getName().equals("arrayTest")).findFirst().get();
        assertNotNull(arrayAttribute);
        assertEquals("String", arrayAttribute.getType().getName());
        assertEquals("List", arrayAttribute.getType().getCollectionType());
    }

    @Test
    public void canHandleMultipleAggregatesWithSameName() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.duplicate.aggregate.name")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(3, bc.getAggregates().size());
        assertTrue(bc.getAggregates().contains(new Aggregate("test")));
        assertTrue(bc.getAggregates().contains(new Aggregate("TestSpringBoot_test")));
        assertTrue(bc.getAggregates().contains(new Aggregate("TestSpringBoot_test_1")));
    }

    @Test
    public void canHandleMultipleDomainObjectsWithSameName() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.duplicate.domainobject.name")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().iterator().next();
        Set<String> domainObjectNames = aggregate.getDomainObjects().stream().map(e -> e.getName()).collect(Collectors.toSet());
        assertTrue(domainObjectNames.contains("CustomerId"));
        assertTrue(domainObjectNames.contains("customers_CustomerId"));
        assertTrue(domainObjectNames.contains("customers_CustomerId_1"));
    }

    @Test
    public void canCreateDiscoveryCommentOnAggregate() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        assertEquals("This Aggregate has been created on the basis of the RESTful HTTP controller test.application.spring.boot.interfaces.CustomerInformationHolder.", aggregate.getDiscoveryComment());
    }

    @Test
    public void canCreateDiscoveryCommentOnDomainObject() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        BoundedContext bc = boundedContexts.iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        DomainObject domainObject = aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Address")).findAny().get();
        assertEquals("This value object has been derived from the class test.application.spring.boot.model.Address.", domainObject.getDiscoveryComment());
    }
}
