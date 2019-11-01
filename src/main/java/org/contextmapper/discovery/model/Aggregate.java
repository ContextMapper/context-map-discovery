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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a discovered Aggregate.
 *
 * @author Stefan Kapferer
 */
public class Aggregate {

    private String name;
    private Set<Entity> entities;

    public Aggregate(String name) {
        setName(name);
        this.entities = new HashSet<>();
    }

    /**
     * Sets the name of this Aggregate.
     *
     * @param name the name of the Aggregate.
     */
    public void setName(String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("The name of an Aggregate must not be null or empty.");
        this.name = name;
    }

    /**
     * Gets the name of this Aggregate.
     *
     * @return the name of the Aggregate as String
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an entity to the Aggregate.
     *
     * @param entity the entity to be added to the Aggregate
     */
    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    /**
     * Adds all entities in the given set to the Aggregate.
     *
     * @param entities the set of entities to be added to the Aggregate
     */
    public void addEntities(Set<Entity> entities) {
        this.entities.addAll(entities);
    }

    /**
     * Gets the set of entities within the Aggregate.
     *
     * @return the set of entities which are part of the Aggregate
     */
    public Set<Entity> getEntities() {
        return new HashSet<>(entities);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Aggregate))
            return false;

        Aggregate bc = (Aggregate) object;

        return new EqualsBuilder()
                .append(name, bc.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .hashCode();
    }
}
