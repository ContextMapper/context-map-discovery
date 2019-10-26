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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RelationshipTest {

    @Test
    public void canCreateRelationship() {
        // given
        BoundedContext bc1 = new BoundedContext("TestContext1");
        BoundedContext bc2 = new BoundedContext("TestContext2");

        // when
        Relationship relationship = new Relationship(bc1, bc2);

        // then
        assertEquals("TestContext1", relationship.getUpstream().getName());
        assertEquals("TestContext2", relationship.getDownstream().getName());
    }

    @Test
    public void relationshipsWithSameContextsAreEqual() {
        // given
        Relationship relationship1 = new Relationship(new BoundedContext("TestContext1"), new BoundedContext("TestContext1"));
        Relationship relationship2 = new Relationship(new BoundedContext("TestContext1"), new BoundedContext("TestContext1"));

        // when
        boolean equals = relationship1.equals(relationship2);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        Relationship relationship = new Relationship(new BoundedContext("TestContext1"), new BoundedContext("TestContext1"));

        // when
        boolean equals = relationship.equals(new Object());

        // then
        assertFalse(equals);
    }

}
