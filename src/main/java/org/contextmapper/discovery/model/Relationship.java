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

/**
 * Represents an upstream-downstream relationship between two Bounded Contexts.
 *
 * @author Stefan Kapferer
 */
public class Relationship {

    private BoundedContext upstream;
    private BoundedContext downstream;

    public Relationship(BoundedContext upstream, BoundedContext downstream) {
        this.upstream = upstream;
        this.downstream = downstream;
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
