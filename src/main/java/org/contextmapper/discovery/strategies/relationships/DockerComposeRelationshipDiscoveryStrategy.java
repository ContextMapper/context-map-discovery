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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.model.Relationship;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class DockerComposeRelationshipDiscoveryStrategy extends AbstractRelationshipDiscoveryStrategy implements RelationshipDiscoveryStrategy {

    private File sourcePath;

    public DockerComposeRelationshipDiscoveryStrategy(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public Set<Relationship> discoverRelationships() {
        Set<Relationship> relationships = new HashSet<>();
        for (File dockerComposeFile : findDockerComposeFiles()) {
            relationships.addAll(discoverRelationships(dockerComposeFile));
        }
        return relationships;
    }

    private Set<Relationship> discoverRelationships(File dockerComposeFile) {
        Set<Relationship> relationships = new HashSet<>();
        for (ServiceDependency dependency : parseDependencies(dockerComposeFile)) {
            BoundedContext upstreamContext = discoverer.lookupBoundedContext(dependency.dependsOn);
            BoundedContext downstreamContext = discoverer.lookupBoundedContext(dependency.service);
            if (upstreamContext != null && downstreamContext != null) {
                relationships.add(new Relationship(upstreamContext, downstreamContext));
            }
        }
        return relationships;
    }

    protected List<ServiceDependency> parseDependencies(File dockerComposeFile) {
        List<ServiceDependency> result = new ArrayList<>();
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> composeObjectMap = yaml.load(new FileInputStream(dockerComposeFile));
            if (!composeObjectMap.containsKey("services"))
                return result;
            Map<String, Object> services = (Map<String, Object>) composeObjectMap.get("services");
            if (services == null)
                return result;
            for (String service : services.keySet()) {
                if (((Map<String, Object>) services.get(service)).containsKey("depends_on"))
                    result.addAll(createServiceDependencies4Service(service, (List<String>) ((Map<String, Object>) services.get(service)).get("depends_on")));
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The file '" + dockerComposeFile.toString() + "' does not exist!", e);
        }
        return result;
    }

    private List<ServiceDependency> createServiceDependencies4Service(String service, List<String> dependencies) {
        List<ServiceDependency> resultList = new ArrayList<>();
        for (String dependency : dependencies) {
            resultList.add(new ServiceDependency(service, dependency));
        }
        return resultList;
    }

    private Collection<File> findDockerComposeFiles() {
        return FileUtils.listFiles(sourcePath, new NameFileFilter("docker-compose.yml"), TrueFileFilter.INSTANCE);
    }

    protected class ServiceDependency {
        private String service;
        private String dependsOn;

        ServiceDependency(String service, String dependsOn) {
            this.service = service;
            this.dependsOn = dependsOn;
        }
    }
}
