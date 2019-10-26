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
package org.contextmapper.discovery.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextMapTest {

    @Test
    public void canAddBoundedContext() {
        // given
        ContextMap contextMap = new ContextMap();
        BoundedContext context = new BoundedContext("TestContext");

        // when
        contextMap.addBoundedContext(context);

        // then
        assertEquals(1, contextMap.getBoundedContexts().size());
        assertEquals("TestContext", contextMap.getBoundedContexts().iterator().next().getName());
    }

    @Test
    public void canAddRelationship() {
        // given
        ContextMap contextMap = new ContextMap();
        BoundedContext upstream = new BoundedContext("Upstream");
        BoundedContext downstream = new BoundedContext("Downstream");
        contextMap.addBoundedContext(upstream);
        contextMap.addBoundedContext(downstream);
        Relationship relationship = new Relationship(upstream, downstream);

        // when
        contextMap.addRelationship(relationship);

        // then
        assertEquals(1, contextMap.getRelationships().size());
        Relationship testRel = contextMap.getRelationships().iterator().next();
        assertEquals("Upstream", testRel.getUpstream().getName());
        assertEquals("Downstream", testRel.getDownstream().getName());
    }

    @Test
    public void cannotAddRelationshipWithMissingUpstream() {
        // given
        ContextMap contextMap = new ContextMap();
        BoundedContext downstream = new BoundedContext("Downstream");
        Relationship relationship = new Relationship(new BoundedContext("Upstream"), downstream);
        contextMap.addBoundedContext(downstream);

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contextMap.addRelationship(relationship);
        });
    }

    @Test
    public void cannotAddRelationshipWithMissingDownstream() {
        // given
        ContextMap contextMap = new ContextMap();
        BoundedContext upstream = new BoundedContext("Upstream");
        Relationship relationship = new Relationship(upstream, new BoundedContext("Downstream"));
        contextMap.addBoundedContext(upstream);

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            contextMap.addRelationship(relationship);
        });
    }

}
