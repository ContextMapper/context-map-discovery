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

import static org.contextmapper.discovery.model.DomainObjectType.ENTITY;
import static org.contextmapper.discovery.model.DomainObjectType.VALUE_OBJECT;
import static org.junit.jupiter.api.Assertions.*;

public class DomainObjectTest {

    @Test
    public void canCreateEntity() {
        // given
        DomainObject domainObject;

        // when
        domainObject = new DomainObject(ENTITY, "Customer", "test.Customer");

        // then
        assertEquals("test.Customer", domainObject.getOriginalType());
        assertEquals("Customer", domainObject.getName());
    }

    @Test
    public void canAddAttribute() {
        // given
        DomainObject domainObject = new DomainObject(ENTITY, "Name", "Type");

        // when
        Attribute attribute = new Attribute("String", "attr1");
        domainObject.addAttribute(attribute);

        // then
        assertEquals(1, domainObject.getAttributes().size());
        Attribute attribute1 = domainObject.getAttributes().iterator().next();
        assertEquals(attribute, attribute1);
        assertEquals(domainObject, attribute1.getParent());
        assertEquals("String", attribute1.getType());
        assertEquals("attr1", attribute1.getName());
        assertFalse(attribute1.equals(new Object()));
    }

    @Test
    public void canAddReference() {
        // given
        DomainObject domainObject = new DomainObject(ENTITY, "Name", "test.Entity");
        DomainObject referencedDomainObject = new DomainObject(ENTITY, "ReferencedType", "test.ReferencedType");

        // when
        Reference reference = new Reference(referencedDomainObject, "reference");
        domainObject.addReference(reference);

        // then
        assertEquals(1, domainObject.getReferences().size());
        Reference reference1 = domainObject.getReferences().iterator().next();
        assertEquals(reference, reference1);
        assertEquals(domainObject, reference1.getParent());
        assertEquals(referencedDomainObject, reference1.getType());
        assertEquals("reference", reference1.getName());
        assertFalse(reference1.equals(new Object()));
    }

    @Test
    public void canAddMethod() {
        // given
        DomainObject domainObject = new DomainObject(ENTITY, "Name", "test.Entity");
        Method method = new Method("testMethod");

        // when
        domainObject.addMethod(method);

        // then
        assertEquals(1, domainObject.getMethods().size());
        Method resultMethod = domainObject.getMethods().iterator().next();
        assertEquals("testMethod", resultMethod.getName());
        assertEquals(domainObject, resultMethod.getParent());
        assertEquals(resultMethod, method);
        assertFalse(resultMethod.equals(new Object()));
    }

    @Test
    public void cannotCreateEntityWithNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DomainObject(ENTITY, null, "Type");
        });
    }

    @Test
    public void cannotCreateEntityWithEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DomainObject(ENTITY, "", "Type");
        });
    }

    @Test
    public void cannotCreateEntityWithNullType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DomainObject(null, "Name");
        });
    }

    @Test
    public void entitiesWithSameTypeAndNameAreEqual() {
        // given
        DomainObject domainObject1 = new DomainObject(ENTITY, "Name", "Type");
        DomainObject domainObject2 = new DomainObject(ENTITY, "Name", "Type");

        // when
        boolean equals = domainObject1.equals(domainObject1);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        DomainObject domainObject = new DomainObject(ENTITY, "Name", "Type");

        // when
        boolean equals = domainObject.equals(new Object());

        // then
        assertFalse(equals);
    }

    @Test
    public void canAddDiscoveryComment() {
        // given
        DomainObject domainObject = new DomainObject(ENTITY, "Type", "test.Type");
        String discoveryComment = "This entity has been derived from the class test.Type.";

        // when
        domainObject.setDiscoveryComment(discoveryComment);

        // then
        assertEquals(discoveryComment, domainObject.getDiscoveryComment());
    }

    @Test
    public void canSetType() {
        // given
        DomainObject domainObject = new DomainObject(VALUE_OBJECT, "TestType", "org.contextmapper.TestType");

        // when
        domainObject.setType(ENTITY);

        // then
        assertEquals(ENTITY, domainObject.getType());
    }

}
