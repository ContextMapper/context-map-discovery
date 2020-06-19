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
 * Represents a discovered domain object (part of an Aggregate).
 *
 * @author Stefan Kapferer
 */
public class DomainObject {

    private String name;
    private String originalType;
    private Set<Attribute> attributes;
    private Set<Method> methods;
    private String discoveryComment;
    private DomainObjectType type;
    private Aggregate parent;

    public DomainObject(DomainObjectType type, String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("The name of a domain object must not be null or empty.");
        if (type == null)
            throw new IllegalArgumentException("The type of a domain object must not be null.");

        this.type = type;
        this.name = name;
        this.attributes = new HashSet<>();
        this.methods = new HashSet<>();
    }

    public DomainObject(DomainObjectType type, String name, String originalType) {
        this(type, name);
        this.originalType = originalType;
    }

    /**
     * Gets the name of the domain object.
     *
     * @return the name of the domain object
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the original type of the domain object.
     *
     * @return the original type of the domain object
     */
    public String getOriginalType() {
        return originalType;
    }

    /**
     * Gets the type of the domain object.
     *
     * @return the type of the domain object
     */
    public DomainObjectType getType() {
        return type;
    }

    /**
     * Sets the type of the domain object.
     *
     * @param type the type of the domain object
     */
    public void setType(DomainObjectType type) {
        this.type = type;
    }

    /**
     * Adds a new attribute to the domain object.
     *
     * @param attribute the attribute to be added to the domain object
     */
    public void addAttribute(Attribute attribute) {
        attribute.setParent(this);
        this.attributes.add(attribute);
    }

    /**
     * Gets the set of attributes of the domain object.
     *
     * @return the set of attributes of the domain object
     */
    public Set<Attribute> getAttributes() {
        return new HashSet<>(attributes);
    }

    /**
     * Adds a new method to the domain object.
     *
     * @param method the method to be added to the domain object
     */
    public void addMethod(Method method) {
        method.setParent(this);
        this.methods.add(method);
    }

    /**
     * Gets the set of methods of the domain object.
     *
     * @return the set of methods of the domain object
     */
    public Set<Method> getMethods() {
        return new HashSet<>(methods);
    }

    /**
     * Sets a comment describing how the domain object has been discovered.
     *
     * @param discoveryComment the comment describing how the domain object has been discovered
     */
    public void setDiscoveryComment(String discoveryComment) {
        this.discoveryComment = discoveryComment;
    }

    /**
     * Gets a comment describing how the domain object has been discovered.
     *
     * @return the comment describing how the domain object has been discovered
     */
    public String getDiscoveryComment() {
        return discoveryComment;
    }

    /**
     * Gets the parent Aggregate of which the domain object is part of.
     *
     * @return the parent Aggregate
     */
    public Aggregate getParent() {
        return parent;
    }

    /**
     * Sets the parent Aggregate of which the domain object is part of.
     *
     * @param parent the parent Aggregate
     */
    public void setParent(Aggregate parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DomainObject))
            return false;

        DomainObject domainObject = (DomainObject) object;

        return new EqualsBuilder()
                .append(type, domainObject.type)
                .append(name, domainObject.name)
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
