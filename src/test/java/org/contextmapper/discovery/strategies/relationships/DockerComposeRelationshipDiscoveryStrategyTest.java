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
package org.contextmapper.discovery.strategies.relationships;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.model.Relationship;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DockerComposeRelationshipDiscoveryStrategyTest {

    @Test
    public void canDiscoverRelationship() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"))
                .usingRelationshipDiscoveryStrategies(
                        new DockerComposeRelationshipDiscoveryStrategy(new File("./src/test/resources/test/microservice/spring-boot")))
                .usingBoundedContextNameMappingStrategies(
                        new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-")
                );

        // when
        ContextMap contextmap = discoverer.discoverContextMap();

        // then
        assertEquals(1, contextmap.getRelationships().size());
        Relationship relationship = contextmap.getRelationships().iterator().next();
        assertEquals("Microservice1", relationship.getUpstream().getName());
        assertEquals("Microservice2", relationship.getDownstream().getName());
    }

    @Test
    public void canAddAllDiscoveredAggregatesToExposedAggregates() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"))
                .usingRelationshipDiscoveryStrategies(
                        new DockerComposeRelationshipDiscoveryStrategy(new File("./src/test/resources/test/microservice/spring-boot")))
                .usingBoundedContextNameMappingStrategies(
                        new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-")
                );

        // when
        ContextMap contextmap = discoverer.discoverContextMap();

        // then
        assertEquals(1, contextmap.getRelationships().size());
        Relationship relationship = contextmap.getRelationships().iterator().next();
        assertEquals("Microservice1", relationship.getUpstream().getName());
        assertEquals("Microservice2", relationship.getDownstream().getName());
        assertEquals(1, relationship.getExposedAggregates().size());
        assertEquals("customers", relationship.getExposedAggregates().iterator().next().getName());
        assertEquals("The list of exposed Aggregates may contain Aggregates which are not used by the downstream " +
                "(discovery strategy simply added all Aggregates).", relationship.getExposedAggregatesComment());
    }

    @ParameterizedTest
    @MethodSource("noServicesDockerComposeFiles")
    public void emptyResultIfDockerComposeFileDoesNotContainServices(String sourcePath) {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"))
                .usingRelationshipDiscoveryStrategies(
                        new DockerComposeRelationshipDiscoveryStrategy(new File(sourcePath))
                );

        // when
        ContextMap contextmap = discoverer.discoverContextMap();

        // then
        assertEquals(0, contextmap.getRelationships().size());
    }

    private static Stream<Arguments> noServicesDockerComposeFiles() {
        return Stream.of(Arguments.of("./src/test/resources/test/docker/compose/no-services-1"),
                Arguments.of("./src/test/resources/test/docker/compose/no-services-2"));
    }

    @Test
    public void throwExceptionIfFileDoesNotExist() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"))
                .usingRelationshipDiscoveryStrategies(
                        new TestDockerComposeStrategy(new File("./src/test/resources/test/microservice/spring-boot"))
                );

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            discoverer.discoverContextMap();
        });
    }

    private class TestDockerComposeStrategy extends DockerComposeRelationshipDiscoveryStrategy {
        public TestDockerComposeStrategy(File sourcePath) {
            super(sourcePath);
        }

        @Override
        protected List<DockerComposeRelationshipDiscoveryStrategy.ServiceDependency> parseDependencies(File dockerComposeFile) {
            return super.parseDependencies(new File("this-docker-compose-file-does-not-exist.yml"));
        }
    }

}
