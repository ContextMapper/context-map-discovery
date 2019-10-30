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
package org.contextmapper.discovery;

import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.DockerComposeRelationshipDiscoveryStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContextMapSerializerTest {

    private final static String SRC_GEN_FOLDER = "./src-gen";
    private final static String TEST_CML_FILE = SRC_GEN_FOLDER + "/microservice-test.cml";

    @BeforeEach
    void prepare() {
        File srcGen = new File(SRC_GEN_FOLDER);
        File testCMLFile = new File(TEST_CML_FILE);

        if (!srcGen.exists())
            srcGen.mkdir();

        if (testCMLFile.exists())
            testCMLFile.delete();

        assertFalse(testCMLFile.exists());
    }

    //@Test
    public void canSaveDiscoveredModelAsCMLFile() throws IOException {
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
        ContextMapSerializer serializer = new ContextMapSerializer();
        serializer.serializeContextMap(contextmap, new File(TEST_CML_FILE));

        // then
        assertFalse(new File(TEST_CML_FILE).exists());
    }

    @Test
    public void cannotSerializeOtherThanCMLFile() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.microservice.spring.boot"));

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContextMapSerializer().serializeContextMap(discoverer.discoverContextMap(), new File("test.ext"));
        });
    }

    @Test
    public void cannotSerializeEmptyContextMap() {
        // given
        ContextMap contextMap = new ContextMap();

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ContextMapSerializer().serializeContextMap(contextMap, new File("test.cml"));
        });
    }

}
