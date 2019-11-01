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
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.DockerComposeRelationshipDiscoveryStrategy;
import org.contextmapper.dsl.contextMappingDSL.*;
import org.contextmapper.tactic.dsl.tacticdsl.Entity;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

    @Test
    public void canConvertEntities() {
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
        assertEquals(3, aggregate.getDomainObjects().size());
        assertNotNull(aggregate.getDomainObjects().stream().filter(o -> o.getName().equals("Address")).findFirst().get());
        assertNotNull(aggregate.getDomainObjects().stream().filter(o -> o.getName().equals("CustomerId")).findFirst().get());
        assertNotNull(aggregate.getDomainObjects().stream().filter(o -> o.getName().equals("Customer")).findFirst().get());
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
        Entity addressEntity = (Entity) aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Address")).findFirst().get();
        assertNotNull(addressEntity);
        assertEquals(4, addressEntity.getAttributes().size());
        assertNotNull(addressEntity.getAttributes().stream().filter(a -> a.getName().equals("street")).findFirst().get());
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
        Entity customerEntity = (Entity) aggregate.getDomainObjects().stream().filter(e -> e.getName().equals("Customer")).findFirst().get();
        assertNotNull(customerEntity);
        assertEquals(4, customerEntity.getReferences().size());
        assertEquals("CustomerId", customerEntity.getReferences().stream().filter(r -> r.getName().equals("id")).findFirst().get()
                .getDomainObjectType().getName());
    }

}
