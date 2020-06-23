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
    private Set<DomainObject> domainObjects;
    private Set<Service> services;
    private String discoveryComment;

    public Aggregate(String name) {
        setName(name);
        this.domainObjects = new HashSet<>();
        this.services = new HashSet<>();
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
     * Adds a domain object to the Aggregate.
     *
     * @param domainObject the domain object to be added to the Aggregate
     */
    public void addDomainObject(DomainObject domainObject) {
        this.domainObjects.add(domainObject);
        domainObject.setParent(this);
    }

    /**
     * Adds all domain objects in the given set to the Aggregate.
     *
     * @param domainObjects the set of domain objects to be added to the Aggregate
     */
    public void addDomainObjects(Set<DomainObject> domainObjects) {
        for (DomainObject domainObject : domainObjects) {
            this.addDomainObject(domainObject);
        }
    }

    /**
     * Adds a service to the Aggregate.
     *
     * @param service the service to be added to the Aggregate
     */
    public void addService(Service service) {
        this.services.add(service);
    }

    /**
     * Adds all services in the given set to the Aggregate.
     *
     * @param services the set of services to be added to the Aggregate
     */
    public void addServices(Set<Service> services) {
        for (Service service : services) {
            this.addService(service);
        }
    }

    /**
     * Gets the set of domain objects within the Aggregate.
     *
     * @return the set of domain objects which are part of the Aggregate
     */
    public Set<DomainObject> getDomainObjects() {
        return new HashSet<>(domainObjects);
    }

    /**
     * Gets the set of services within the Aggregate.
     *
     * @return the set of services which are part of the Aggregate
     */
    public Set<Service> getServices() {
        return new HashSet<>(services);
    }

    /**
     * Sets a comment regarding how the Aggregate has been discovered.
     *
     * @param discoveryComment the comment regarding how the Aggregate has been discovered
     */
    public void setDiscoveryComment(String discoveryComment) {
        this.discoveryComment = discoveryComment;
    }

    /**
     * Gets a comment regarding how the Aggregate has been discovered.
     *
     * @return a comment regarding how the Aggregate has been discovered.
     */
    public String getDiscoveryComment() {
        return discoveryComment;
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
