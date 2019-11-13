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
 * Represents a discovered Bounded Context.
 *
 * @author Stefan Kapferer
 */
public class BoundedContext {

    private String name;
    private String technology;
    private Set<Aggregate> aggregates;

    public BoundedContext(String name) {
        setName(name);
        this.aggregates = new HashSet<>();
    }

    /**
     * Sets the name of this Bounded Context.
     *
     * @param name the name of the Bounded Context.
     */
    public void setName(String name) {
        if (name == null || "".equals(name))
            throw new IllegalArgumentException("The name of a Bounded Context must not be null or empty.");
        this.name = name;
    }

    /**
     * Gets the name of this Bounded Context.
     *
     * @return the name of the Bounded Context as String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the implementation technology of this Bounded Context.
     *
     * @param technology the implementation technology of this Bounded Context as a String
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the implementation technology of this Bounded Context.
     *
     * @return the implementation technology of this Bounded Context as a String
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Adds Aggregates to the Bounded Context.
     *
     * @param aggregates the set of Aggregates to be added
     */
    public void addAggregates(Set<Aggregate> aggregates) {
        this.aggregates.addAll(aggregates);
    }

    /**
     * Adds an Aggregate to the Bounded Context.
     *
     * @param aggregate the Aggregate to be added to the Bounded Context
     */
    public void addAggregate(Aggregate aggregate) {
        this.aggregates.add(aggregate);
    }

    /**
     * Gets the Aggregates of the Bounded Context.
     *
     * @return the set of Aggregates of the Bounded Context
     */
    public Set<Aggregate> getAggregates() {
        return new HashSet<>(aggregates);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BoundedContext))
            return false;

        BoundedContext bc = (BoundedContext) object;

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
