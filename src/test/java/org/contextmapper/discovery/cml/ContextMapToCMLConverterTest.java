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
package org.contextmapper.discovery.cml;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.DockerComposeRelationshipDiscoveryStrategy;
import org.contextmapper.dsl.contextMappingDSL.ContextMap;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.contextmapper.dsl.contextMappingDSL.UpstreamDownstreamRelationship;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContextMapToCMLConverterTest {

    @Test
    public void canConvertToCMLModel() {
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
        ContextMappingModel model = new ContextMapToCMLConverter().convert(discoverer.discoverContextMap());
        ContextMap contextMap = model.getMap();

        // then
        assertNotNull(contextMap);
        assertEquals(2, model.getBoundedContexts().size());
        assertEquals(2, contextMap.getBoundedContexts().size());
        assertEquals(1, contextMap.getRelationships().size());
        UpstreamDownstreamRelationship relationship = (UpstreamDownstreamRelationship) contextMap.getRelationships().get(0);
        assertEquals("Microservice1", relationship.getUpstream().getName());
        assertEquals("Microservice2", relationship.getDownstream().getName());
    }

}
