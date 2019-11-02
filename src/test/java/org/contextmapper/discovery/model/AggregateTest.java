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

import static org.junit.jupiter.api.Assertions.*;

public class AggregateTest {

    @Test
    public void cannotCreateAggregateWithNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Aggregate(null);
        });
    }

    @Test
    public void cannotCreateAggregateWithEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Aggregate("");
        });
    }

    @Test
    public void canCreateAggregateWithName() {
        // given
        Aggregate aggregate;

        // when
        aggregate = new Aggregate("TestAggregate");

        // then
        assertEquals("TestAggregate", aggregate.getName());
    }

    @Test
    public void canAddEntity() {
        // given
        Aggregate aggregate = new Aggregate("TestAggregate");

        // when
        aggregate.addEntity(new Entity("test.Entity", "Entity"));

        // then
        assertEquals(1, aggregate.getEntities().size());
        assertEquals(new Entity("test.Entity", "Entity"), aggregate.getEntities().iterator().next());
    }

    @Test
    public void aggregatesWithSameNameAreEqual() {
        // given
        Aggregate aggregate1 = new Aggregate("TestAggregate");
        Aggregate aggregate2 = new Aggregate("TestAggregate");

        // when
        boolean equals = aggregate1.equals(aggregate2);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        Aggregate aggregate = new Aggregate("TestAggregate");

        // when
        boolean equals = aggregate.equals(new Object());

        // then
        assertFalse(equals);
    }

    @Test
    public void canAddComment() {
        // given
        Aggregate aggregate = new Aggregate("TestAggregate");
        String comment = "This aggregate has been created on the basis of the REST controller test.TestAggregate";

        // when
        aggregate.setDiscoveryComment(comment);

        // then
        assertEquals(comment, aggregate.getDiscoveryComment());
    }
}
