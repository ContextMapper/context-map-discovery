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

import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.strategies.boundedcontexts.BoundedContextDiscoveryStrategy;
import org.contextmapper.discovery.strategies.names.BoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.names.DefaultBoundedContextNameMappingStrategy;
import org.contextmapper.discovery.strategies.relationships.RelationshipDiscoveryStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Discovers Bounded Contexts and relationships between them with given strategies.
 *
 * @author Stefan Kapferer
 */
public class ContextMapDiscoverer {

    private ContextMap contextMap;
    private List<BoundedContextDiscoveryStrategy> boundedContextDiscoveryStrategies = new ArrayList<>();
    private List<RelationshipDiscoveryStrategy> relationshipDiscoveryStrategies = new ArrayList<>();
    private List<BoundedContextNameMappingStrategy> boundedContextNameMappingStrategies = new ArrayList<>();

    public ContextMapDiscoverer() {
        this.contextMap = new ContextMap();
        this.boundedContextNameMappingStrategies.add(new DefaultBoundedContextNameMappingStrategy());
    }

    /**
     * Registers the Bounded Context discovery strategies to be used.
     *
     * @param boundedContextDiscoveryStrategies the Bounded Context discovery strategies
     */
    public ContextMapDiscoverer usingBoundedContextDiscoveryStrategies(BoundedContextDiscoveryStrategy... boundedContextDiscoveryStrategies) {
        this.boundedContextDiscoveryStrategies.addAll(Arrays.asList(boundedContextDiscoveryStrategies));
        return this;
    }

    /**
     * Registers the relationship discovery strategies to be used.
     *
     * @param relationshipDiscoveryStrategies the relationship discovery strategies
     */
    public ContextMapDiscoverer usingRelationshipDiscoveryStrategies(RelationshipDiscoveryStrategy... relationshipDiscoveryStrategies) {
        for (RelationshipDiscoveryStrategy strategy : relationshipDiscoveryStrategies) {
            strategy.setContextMapDiscoverer(this);
        }
        this.relationshipDiscoveryStrategies.addAll(Arrays.asList(relationshipDiscoveryStrategies));
        return this;
    }

    /**
     * Registers Bounded Context name mapping strategies to find Bounded Context in
     * {@link #lookupBoundedContext(String) lookupBoundedContext} with different input names.
     *
     * @param boundedContextNameMappingStrategies the Bounded Context name mapping strategies
     */
    public ContextMapDiscoverer usingBoundedContextNameMappingStrategies(BoundedContextNameMappingStrategy... boundedContextNameMappingStrategies) {
        this.boundedContextNameMappingStrategies.addAll(Arrays.asList(boundedContextNameMappingStrategies));
        return this;
    }

    /**
     * Discovers the Bounded Contexts and relationships.
     *
     * @return the Context Map with the discovered Bounded Contexts and relationships
     */
    public ContextMap discoverContextMap() {
        for (BoundedContextDiscoveryStrategy strategy : boundedContextDiscoveryStrategies) {
            contextMap.addAllBoundedContexts(strategy.discoverBoundedContexts());
        }
        for (RelationshipDiscoveryStrategy strategy : relationshipDiscoveryStrategies) {
            contextMap.addAllRelationships(strategy.discoverRelationships());
        }
        return contextMap;
    }

    /**
     * Finds discovered Bounded Context by name, using lookup strategy if configured.
     *
     * @param name the Bounded Context name to be looked up
     * @return the corresponding Bounded Context or null, if the name was not found
     */
    public BoundedContext lookupBoundedContext(String name) {
        for (BoundedContextNameMappingStrategy strategy : boundedContextNameMappingStrategies) {
            BoundedContext bc = getBoundedContextByName(strategy.mapBoundedContextName(name));
            if (bc != null)
                return bc;
        }
        return null;
    }

    private BoundedContext getBoundedContextByName(String name) {
        return this.contextMap.getBoundedContexts().stream().filter(bc -> bc.getName().equals(name)).findFirst().orElse(null);
    }

}
