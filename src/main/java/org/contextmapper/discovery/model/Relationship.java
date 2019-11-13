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
 * Represents an upstream-downstream relationship between two Bounded Contexts.
 *
 * @author Stefan Kapferer
 */
public class Relationship {

    private BoundedContext upstream;
    private BoundedContext downstream;
    private Set<Aggregate> exposedAggregates;
    private String exposedAggregatesComment;

    public Relationship(BoundedContext upstream, BoundedContext downstream) {
        this.upstream = upstream;
        this.downstream = downstream;
        this.exposedAggregates = new HashSet<>();
    }

    /**
     * Gets the upstream Bounded Context.
     *
     * @return the upstream Bounded Context
     */
    public BoundedContext getUpstream() {
        return upstream;
    }

    /**
     * Gets the downstream Bounded Context.
     *
     * @return the downstream Bounded Context
     */
    public BoundedContext getDownstream() {
        return downstream;
    }

    /**
     * Gets the exposed Aggregates up the upstream within this relationship.
     *
     * @return the set of exposed Aggregates by the upstream context
     */
    public Set<Aggregate> getExposedAggregates() {
        return new HashSet<>(exposedAggregates);
    }

    /**
     * Adds a set of Aggregates to the exposed Aggregates of the relationship.
     *
     * @param exposedAggregates the set of Aggregates which are exposed
     */
    public void addExposedAggregates(Set<Aggregate> exposedAggregates) {
        for (Aggregate aggregate : exposedAggregates) {
            addExposedAggregate(aggregate);
        }
    }

    /**
     * Sets a comment regarding how the exposed Aggregates have been discovered.
     *
     * @param exposedAggregatesComment the comment explaining how the exposed Aggregates have been discovered
     */
    public void setExposedAggregatesComment(String exposedAggregatesComment) {
        this.exposedAggregatesComment = exposedAggregatesComment;
    }

    /**
     * Gets a comment regarding how the exposed Aggregates have been discovered.
     *
     * @return the comment explaining how the exposed Aggregates have been discovered
     */
    public String getExposedAggregatesComment() {
        return exposedAggregatesComment;
    }

    /**
     * Adds an Aggregate to the exposed Aggregates of the relationship.
     *
     * @param aggregate the Aggregate which is exposed
     */
    public void addExposedAggregate(Aggregate aggregate) {
        if (!this.upstream.getAggregates().contains(aggregate))
            throw new IllegalArgumentException("The exposed Aggregates must be part of the upstream Bounded Context! " +
                    "('" + aggregate.getName() + "' is not part of '" + upstream.getName() + "')");
        this.exposedAggregates.add(aggregate);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Relationship))
            return false;

        Relationship relationship = (Relationship) object;

        return new EqualsBuilder()
                .append(upstream, relationship.upstream)
                .append(downstream, relationship.downstream)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(upstream)
                .append(downstream)
                .hashCode();
    }
}
