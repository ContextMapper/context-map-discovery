/*
 * Copyright 2020 The Context Mapper Project Team
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
package org.contextmapper.discovery.examples;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.ContextMapSerializer;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.strategies.boundedcontexts.OASBoundedContextDiscoveryStrategy;

import java.io.File;
import java.io.IOException;

/**
 * Open API specification (OAS) discovery example:
 */
public class PublicationManagementDiscovery {

    public static void main(String[] args) throws IOException {
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new OASBoundedContextDiscoveryStrategy("./src/main/resources/specification.yml"));

        ContextMap contextMap = discoverer.discoverContextMap();
        new ContextMapSerializer().serializeContextMap(contextMap, new File("./src-gen/publication-management.cml"));
    }

}
