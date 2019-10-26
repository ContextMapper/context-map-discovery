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
package org.contextmapper.discovery;

import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.model.Relationship;
import org.contextmapper.discovery.strategies.boundedcontexts.AbstractBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.boundedcontexts.BoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.relationships.AbstractRelationshipDiscoveryStrategy;
import org.contextmapper.discovery.strategies.relationships.RelationshipDiscoveryStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ContextMapDiscovererTest {

    @Test
    public void canDiscoverBoundedContexts() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new DummyDiscoveryStrategy()
                );

        // when
        ContextMap contextMap = discoverer.discoverContextMap();

        // then
        assertEquals(2, contextMap.getBoundedContexts().size());
        assertTrue(contextMap.getBoundedContexts().contains(new BoundedContext("DummyUpstreamContext")));
        assertTrue(contextMap.getBoundedContexts().contains(new BoundedContext("DummyDownstreamContext")));
    }

    @Test
    public void canDiscoverRelationship() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new DummyDiscoveryStrategy())
                .usingRelationshipDiscoveryStrategies(
                        new DummyRelationshipStrategy()
                );

        // when
        ContextMap contextMap = discoverer.discoverContextMap();

        // then
        assertEquals(2, contextMap.getBoundedContexts().size());
        assertEquals(1, contextMap.getRelationships().size());
        Relationship relationship = contextMap.getRelationships().iterator().next();
        assertEquals("DummyUpstreamContext", relationship.getUpstream().getName());
        assertEquals("DummyDownstreamContext", relationship.getDownstream().getName());
    }

    @Test
    public void canLookupDiscoveredBoundedContextByName() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new DummyDiscoveryStrategy()
                );

        // when
        discoverer.discoverContextMap();

        // then
        assertNotNull(discoverer.lookupBoundedContext("DummyUpstreamContext"));
    }

    @Test
    public void nameLookupReturnsNullIfBCNotFound() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new DummyDiscoveryStrategy()
                );

        // when
        discoverer.discoverContextMap();

        // then
        assertNull(discoverer.lookupBoundedContext("NotExistingContext"));
    }

    private class DummyDiscoveryStrategy extends AbstractBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {
        @Override
        public Set<BoundedContext> discoverBoundedContexts() {
            // just discover a dummy Bounded Context:
            Set<BoundedContext> bcs = new HashSet<>();
            bcs.add(new BoundedContext("DummyUpstreamContext"));
            bcs.add(new BoundedContext("DummyDownstreamContext"));
            return bcs;
        }
    }

    private class DummyRelationshipStrategy extends AbstractRelationshipDiscoveryStrategy implements RelationshipDiscoveryStrategy {
        @Override
        public Set<Relationship> discoverRelationships() {
            Set<Relationship> relationships = new HashSet<>();
            relationships.add(new Relationship(new BoundedContext("DummyUpstreamContext"), new BoundedContext("DummyDownstreamContext")));
            return relationships;
        }
    }

}
