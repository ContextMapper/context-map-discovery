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

import java.util.HashSet;
import java.util.Set;

/**
 * Context Map with discovered Bounded Contexts and relationships
 *
 * @author Stefan Kapferer
 */
public class ContextMap {

    private Set<BoundedContext> boundedContexts = new HashSet<>();
    private Set<Relationship> relationships = new HashSet<>();

    /**
     * Adds a Bounded Context to the Context Map
     *
     * @param boundedContext the Bounded Context to be added to the Context Map
     */
    public void addBoundedContext(BoundedContext boundedContext) {
        this.boundedContexts.add(boundedContext);
    }

    /**
     * Adds all Bounded Contexts in the given set to the Context Map
     *
     * @param boundedContexts the set of Bounded Contexts to be added to the Context Map
     */
    public void addAllBoundedContexts(Set<BoundedContext> boundedContexts) {
        this.boundedContexts.addAll(boundedContexts);
    }

    /**
     * Adds a relationship to the Context Map
     *
     * @param relationship the relationship to be added to the Context Map
     */
    public void addRelationship(Relationship relationship) {
        if (!boundedContexts.contains(relationship.getUpstream()))
            throw new IllegalArgumentException("The upstream Bounded Context of this relationship is not part of the Context Map.");
        if (!boundedContexts.contains(relationship.getDownstream()))
            throw new IllegalArgumentException("The downstream Bounded Context of this relationship is not part of the Context Map.");
        this.relationships.add(relationship);
    }

    /**
     * Adds all relationships in the give set to the Context Map
     *
     * @param relationships the set of relationships to be added to the Context Map
     */
    public void addAllRelationships(Set<Relationship> relationships) {
        for (Relationship relationship : relationships) {
            addRelationship(relationship);
        }
    }

    /**
     * Gets the Bounded Contexts of the Context Map
     *
     * @return a set of Bounded Contexts
     */
    public Set<BoundedContext> getBoundedContexts() {
        return new HashSet<>(boundedContexts);
    }

    /**
     * Gets the relationships between Bounded Contexts of this Context Map
     *
     * @return a set of relationship between Bounded Contexts
     */
    public Set<Relationship> getRelationships() {
        return new HashSet<>(relationships);
    }
}
