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

public class TypeTest {

    @Test
    public void cannotCreateNullDomainObjectType() {
        assertThrows(RuntimeException.class, () -> {
            new Type((DomainObject) null);
        });
    }

    @Test
    public void cannotCreateNullPrimitiveType() {
        assertThrows(RuntimeException.class, () -> {
            new Type((String) null);
        });
    }

    @Test
    public void cannotCreateEmptyPrimitiveType() {
        assertThrows(RuntimeException.class, () -> {
            new Type("");
        });
    }

    @Test
    public void canCreateDomainObjectType() {
        // given
        DomainObject object = new DomainObject(DomainObjectType.ENTITY, "Customer");

        // when
        Type type = new Type(object);

        // then
        assertEquals(TypeKind.DOMAIN_OBJECT, type.getKind());
        assertTrue(type.isDomainObjectType());
        assertEquals("Customer", type.getDomainObjectType().getName());
    }

    @Test
    public void canCreatePrimitiveType() {
        // given
        String primitiveType = "String";

        // when
        Type type = new Type(primitiveType);

        // then
        assertEquals(TypeKind.PRIMITIVE, type.getKind());
        assertTrue(type.isPrimitiveType());
        assertEquals(primitiveType, type.getPrimitiveType());
    }

    @Test
    public void cannotGetPrimitiveType4DomainObjectType() {
        // given
        Type type = new Type(new DomainObject(DomainObjectType.ENTITY, "Customer"));

        // when, then
        assertThrows(RuntimeException.class, () -> {
            type.getPrimitiveType();
        });
    }

    @Test
    public void cannotGetDomainObjectType4PrimitiveType() {
        // given
        Type type = new Type("String");

        // when, then
        assertThrows(RuntimeException.class, () -> {
            type.getDomainObjectType();
        });
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        Type type = new Type("String");

        // when
        boolean equals = type.equals(new Object());

        // then
        assertFalse(equals);
    }

    @Test
    public void domainObjectTypesOfSameObjectAreEqual() {
        // given
        Type type1 = new Type(new DomainObject(DomainObjectType.ENTITY, "Customer"));
        Type type2 = new Type(new DomainObject(DomainObjectType.ENTITY, "Customer"));

        // when
        boolean equals = type1.equals(type2);

        // then
        assertTrue(equals);
    }

    @Test
    public void primitiveTypeOfSameTypeAreEqual() {
        // given
        Type type1 = new Type("String");
        Type type2 = new Type("String");

        // when
        boolean equals = type1.equals(type2);

        // then
        assertTrue(equals);
    }

    @Test
    public void canSetCollectionType() {
        // given
        Type type = new Type("String");

        // when
        type.setCollectionType("List");

        // then
        assertEquals("List", type.getCollectionType());
    }

    @Test
    public void cannotGetCollectionTypeIfItsNotCollection() {
        // given
        Type type = new Type("String");

        // when, then
        assertThrows(RuntimeException.class, () -> {
            type.getCollectionType();
        });
    }

}
