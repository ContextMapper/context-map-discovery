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
import org.contextmapper.discovery.model.Aggregate;
import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.model.Entity;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(2, aggregate.getEntities().size());
        assertTrue(aggregate.getEntities().contains(new Entity("test.application.spring.boot.model.Address", "Address")));
        assertTrue(aggregate.getEntities().contains(new Entity("test.application.spring.boot.model.CustomerId", "CustomerId")));
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

}
