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
    public void canDiscoverEntitiesFromResourceMethods() {
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
        assertEquals(3, aggregate.getEntities().size());
        assertTrue(aggregate.getEntities().contains(new Entity("test.application.spring.boot.model.Address", "Address")));
        assertTrue(aggregate.getEntities().contains(new Entity("test.application.spring.boot.model.CustomerId", "CustomerId")));
        assertTrue(aggregate.getEntities().contains(new Entity("test.application.spring.boot.model.Customer", "Customer")));
    }

    @Test
    public void canDiscoverEntityAttributes() {
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
        Entity addressEntity = aggregate.getEntities().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressEntity);
        assertEquals(4, addressEntity.getAttributes().size());
        Attribute streetAttribute = addressEntity.getAttributes().stream().filter(a -> a.getName().equals("street")).findFirst().get();
        assertNotNull(streetAttribute);
        assertEquals("street", streetAttribute.getName());
        assertEquals("String", streetAttribute.getType());
        Attribute plzAttribute = addressEntity.getAttributes().stream().filter(a -> a.getName().equals("plz")).findFirst().get();
        assertNotNull(plzAttribute);
        assertEquals("plz", plzAttribute.getName());
        assertEquals("int", plzAttribute.getType());
    }

    @Test
    public void canDiscoverEntityReferences() {
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
        Entity customerEntity = aggregate.getEntities().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        Entity customerIdEntity = aggregate.getEntities().stream().filter(e -> e.getName().equals("CustomerId")).findFirst().get();
        assertNotNull(customerEntity);
        assertNotNull(customerIdEntity);
        Set<Reference> singleReferences = customerEntity.getReferences().stream().filter(r -> r.getCollectionType() == null || "".equals(r.getCollectionType())).collect(Collectors.toSet());
        assertEquals(1, singleReferences.size());
        assertEquals(customerIdEntity, singleReferences.iterator().next().getType());
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
        Entity customerEntity = aggregate.getEntities().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        assertNotNull(customerEntity);
        assertEquals(4, customerEntity.getReferences().size());
        Set<Reference> collectionReferences = customerEntity.getReferences().stream().filter(r -> r.getCollectionType() != null).collect(Collectors.toSet());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getCollectionType().equals("List")).findFirst().get().getType().getName());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getCollectionType().equals("Set")).findFirst().get().getType().getName());
        assertEquals("Address", collectionReferences.stream().filter(r -> r.getCollectionType().equals("Collection")).findFirst().get().getType().getName());
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
        Entity addressEntity = aggregate.getEntities().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressEntity);
        Attribute arrayAttribute = addressEntity.getAttributes().stream().filter(a -> a.getName().equals("arrayTest")).findFirst().get();
        assertNotNull(arrayAttribute);
        assertEquals("String", arrayAttribute.getType());
        assertEquals("List", arrayAttribute.getCollectionType());
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
    public void canHandleMultipleEntitiesWithSameName() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.duplicate.entity.name")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().iterator().next();
        Set<String> entityNames = aggregate.getEntities().stream().map(e -> e.getName()).collect(Collectors.toSet());
        assertTrue(entityNames.contains("CustomerId"));
        assertTrue(entityNames.contains("customers_CustomerId"));
        assertTrue(entityNames.contains("customers_CustomerId_1"));
    }

}
