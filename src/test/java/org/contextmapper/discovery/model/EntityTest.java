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

public class EntityTest {

    @Test
    public void canCreateEntity() {
        // given
        Entity entity;

        // when
        entity = new Entity("test.Customer", "Customer");

        // then
        assertEquals("test.Customer", entity.getType());
        assertEquals("Customer", entity.getName());
    }

    @Test
    public void canAddAttribute() {
        // given
        Entity entity = new Entity("Type", "Name");

        // when
        Attribute attribute = new Attribute("String", "attr1");
        entity.addAttribute(attribute);

        // then
        assertEquals(1, entity.getAttributes().size());
        Attribute attribute1 = entity.getAttributes().iterator().next();
        assertEquals(attribute, attribute1);
        assertEquals(entity, attribute1.getParent());
        assertEquals("String", attribute1.getType());
        assertEquals("attr1", attribute1.getName());
        assertFalse(attribute1.equals(new Object()));
    }

    @Test
    public void canAddReference() {
        // given
        Entity entity = new Entity("test.Entity", "Name");
        Entity referencedEntity = new Entity("test.ReferencedType", "ReferencedType");

        // when
        Reference reference = new Reference(referencedEntity, "reference");
        entity.addReference(reference);

        // then
        assertEquals(1, entity.getReferences().size());
        Reference reference1 = entity.getReferences().iterator().next();
        assertEquals(reference, reference1);
        assertEquals(entity, reference1.getParent());
        assertEquals(referencedEntity, reference1.getType());
        assertEquals("reference", reference1.getName());
        assertFalse(reference1.equals(new Object()));
    }

    @Test
    public void cannotCreateEntityWithNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Entity("Type", null);
        });
    }

    @Test
    public void cannotCreateEntityWithEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Entity("Type", "");
        });
    }

    @Test
    public void cannotCreateEntityWithNullType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Entity(null, "Name");
        });
    }

    @Test
    public void cannotCreateEntityWithEmptyType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Entity("", "Name");
        });
    }

    @Test
    public void entitiesWithSameTypeAndNameAreEqual() {
        // given
        Entity entity1 = new Entity("Type", "Name");
        Entity entity2 = new Entity("Type", "Name");

        // when
        boolean equals = entity1.equals(entity1);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        Entity entity = new Entity("Type", "Name");

        // when
        boolean equals = entity.equals(new Object());

        // then
        assertFalse(equals);
    }

    @Test
    public void canAddDiscoveryComment() {
        // given
        Entity entity = new Entity("test.Type", "Type");
        String discoveryComment = "This entity has been derived from the class test.Type.";

        // when
        entity.setDiscoveryComment(discoveryComment);

        // then
        assertEquals(discoveryComment, entity.getDiscoveryComment());
    }

}
