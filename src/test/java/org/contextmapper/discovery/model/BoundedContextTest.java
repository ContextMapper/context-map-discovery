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

public class BoundedContextTest {

    @Test
    public void cannotCreateBoundedContextWithNullName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BoundedContext(null);
        });
    }

    @Test
    public void cannotCreateBoundedContextWithEmptyName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BoundedContext("");
        });
    }

    @Test
    public void canCreateBoundedContextWithName() {
        // given
        BoundedContext boundedContext;

        // when
        boundedContext = new BoundedContext("TestContext");

        // then
        assertEquals("TestContext", boundedContext.getName());
    }

    @Test
    public void canSetTechnology() {
        // given
        BoundedContext boundedContext = new BoundedContext("TestContext");

        // when
        boundedContext.setTechnology("Java");

        // then
        assertEquals("Java", boundedContext.getTechnology());
    }

    @Test
    public void boundedContextsWithSameNameAreEqual() {
        // given
        BoundedContext bc1 = new BoundedContext("TestContext");
        BoundedContext bc2 = new BoundedContext("TestContext");

        // when
        boolean equals = bc1.equals(bc2);

        // then
        assertTrue(equals);
    }

    @Test
    public void otherObjectsAreNotEqual() {
        // given
        BoundedContext bc = new BoundedContext("TestContext");

        // when
        boolean equals = bc.equals(new Object());

        // then
        assertFalse(equals);
    }

}
