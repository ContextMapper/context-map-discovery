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
package org.contextmapper.discovery.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a discovered Service.
 *
 * @author Stefan Kapferer
 */
public class Service {

    private String name;
    private String discoveryComment;
    private Set<Method> operations;

    public Service(String name) {
        setName(name);
        this.operations = new HashSet<>();
    }

    /**
     * Sets the name of this Service.
     *
     * @param name the name of the Service.
     */
    public void setName(String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("The name of a Service must not be null or empty.");
        this.name = name;
    }

    /**
     * Gets the name of this Service.
     *
     * @return the name of the Service as String
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an operation to the service.
     *
     * @param operation the operation that shall be added to the service
     */
    public void addOperation(Method operation) {
        this.operations.add(operation);
    }

    /**
     * Returns all operations of the service.
     *
     * @return all operations of the service.
     */
    public Set<Method> getOperations() {
        return new HashSet<>(operations);
    }

    /**
     * Sets a comment regarding how the Service has been discovered.
     *
     * @param discoveryComment the comment regarding how the Service has been discovered
     */
    public void setDiscoveryComment(String discoveryComment) {
        this.discoveryComment = discoveryComment;
    }

    /**
     * Gets a comment regarding how the Service has been discovered.
     *
     * @return a comment regarding how the Service has been discovered.
     */
    public String getDiscoveryComment() {
        return discoveryComment;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Service))
            return false;

        Service bc = (Service) object;

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
