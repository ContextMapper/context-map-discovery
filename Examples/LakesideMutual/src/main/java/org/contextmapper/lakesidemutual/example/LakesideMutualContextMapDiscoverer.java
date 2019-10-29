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
package org.contextmapper.lakesidemutual.example;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.ContextMapSerializer;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.SeparatorToCamelCaseBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.DockerComposeRelationshipDiscoveryStrategy;

import java.io.File;
import java.io.IOException;

public class LakesideMutualContextMapDiscoverer {

    public static void main(String[] args) throws IOException {
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("com.lakesidemutual"))
                .usingRelationshipDiscoveryStrategies(
                        new DockerComposeRelationshipDiscoveryStrategy(new File(System.getProperty("user.home") + "/source/LakesideMutual/")))
                .usingBoundedContextNameMappingStrategies(
                        new SeparatorToCamelCaseBoundedContextNameMappingStrategy("-") {
                            @Override
                            public String mapBoundedContextName(String s) {
                                // remove the "Backend" part of the Docker service names to map correctly...
                                String name = super.mapBoundedContextName(s);
                                return name.endsWith("Backend") ? name.substring(0, name.length() - 7) : name;
                            }
                        });

        ContextMap contextmap = discoverer.discoverContextMap();
        new ContextMapSerializer().serializeContextMap(contextmap, new File("./src-gen/lakesidemutual.cml"));
    }

}
