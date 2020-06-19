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

public class ServiceTest {

    @Test
    public void cannotCreateServiceWithNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Service(null);
        });
    }

    @Test
    public void cannotCreateServiceWithEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Service("");
        });
    }

    @Test
    public void canCreateServiceWithName() {
        // given
        Service service;

        // when
        service = new Service("TestService");

        // then
        assertEquals("TestService", service.getName());
    }

    @Test
    public void canAddOperation() {
        // given
        Service service = new Service("TestService");

        // when
        service.addOperation(new Method("createEntity"));

        // then
        assertEquals(1, service.getOperations().size());
        assertEquals("createEntity", service.getOperations().iterator().next().getName());
    }

    @Test
    public void servicesWithSameNameAreEqual() {
        // given
        Service service1 = new Service("TestService");
        Service service2 = new Service("TestService");

        // when
        boolean equals = service1.equals(service2);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        Service service = new Service("TestService");

        // when
        boolean equals = service.equals(new Object());

        // then
        assertFalse(equals);
    }

    @Test
    public void canAddComment() {
        // given
        Service service = new Service("TestService");
        String comment = "Test comment";

        // when
        service.setDiscoveryComment(comment);

        // then
        assertEquals(comment, service.getDiscoveryComment());
    }
}
