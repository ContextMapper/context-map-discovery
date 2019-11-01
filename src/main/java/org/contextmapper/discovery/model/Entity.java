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
 * Represents an Entity (part of Aggregate).
 *
 * @author Stefan Kapferer
 */
public class Entity {

    private String name;
    private String type;
    private Set<Attribute> attributes;
    private Set<Reference> references;
    private String discoveryComment;

    public Entity(String type, String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("The name of an entity must not be null or empty.");
        if (type == null || "".equals(type))
            throw new IllegalArgumentException("The type of an entity must not be null or empty.");

        this.type = type;
        this.name = name;
        this.attributes = new HashSet<>();
        this.references = new HashSet<>();
    }

    /**
     * Gets the name of the entity.
     *
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the entity.
     *
     * @return the type of the entity
     */
    public String getType() {
        return type;
    }

    /**
     * Adds a new attribute to the entity.
     *
     * @param attribute the attribute to be added to the entity
     */
    public void addAttribute(Attribute attribute) {
        attribute.setParent(this);
        this.attributes.add(attribute);
    }

    /**
     * Gets the set of attributes of the entity.
     *
     * @return the set of attributes of the entity
     */
    public Set<Attribute> getAttributes() {
        return new HashSet<>(attributes);
    }

    /**
     * Adds a new reference to the entity.
     *
     * @param reference the reference to be added to the entity
     */
    public void addReference(Reference reference) {
        reference.setParent(this);
        this.references.add(reference);
    }

    /**
     * Gets the set of references of the entity.
     *
     * @return the set of reference of the entity
     */
    public Set<Reference> getReferences() {
        return new HashSet<>(references);
    }

    /**
     * Sets a comment describing how the entity has been discovered.
     *
     * @param discoveryComment the comment describing how the entity has been discovered
     */
    public void setDiscoveryComment(String discoveryComment) {
        this.discoveryComment = discoveryComment;
    }

    /**
     * Gets a comment describing how the entity has been discovered.
     *
     * @return the comment describing how the entity has been discovered
     */
    public String getDiscoveryComment() {
        return discoveryComment;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Entity))
            return false;

        Entity entity = (Entity) object;

        return new EqualsBuilder()
                .append(type, entity.type)
                .append(name, entity.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .append(name)
                .hashCode();
    }
}
