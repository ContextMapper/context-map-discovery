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
package org.contextmapper.discovery.strategies.names;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SeparatorToCamelCaseBoundedContextNameMappingStrategyTest {

    @ParameterizedTest
    @MethodSource("createMappingTestParameters")
    public void canMapBoundedContextName(String inputName, String expectedName) {
        // given
        SeparatorToCamelCaseBoundedContextNameMappingStrategy strategy = new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-");

        // when
        String boundedContextName = strategy.mapBoundedContextName(inputName);

        // then
        assertEquals(expectedName, boundedContextName);
    }

    private static Stream<Arguments> createMappingTestParameters() {
        return Stream.of(Arguments.of("", ""),
                Arguments.of("a", "A"),
                Arguments.of("a-b", "AB"),
                Arguments.of("example-context", "ExampleContext"),
                Arguments.of("a-example-context", "AExampleContext"),
                Arguments.of("example", "Example"));
    }

    @Test
    public void canMapBoundedContextsInDiscoverer() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                )
                .usingBoundedContextNameMappingStrategies(
                        new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-")
                );

        // when
        discoverer.discoverContextMap();
        BoundedContext bc = discoverer.lookupBoundedContext("test-spring-boot");

        // then
        assertNotNull(bc);
        assertEquals("TestSpringBoot", bc.getName());
    }

}
