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
 * Represents reference to other entity.
 *
 * @author Stefan Kapferer
 */
public class Reference {

    private Entity parent;
    private String name;
    private Entity type;

    public Reference(Entity type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Gets the name of the reference attribute.
     *
     * @return the name of the reference attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type ({@link Entity}) of the reference attribute.
     *
     * @return the type ({@link Entity}) of the reference attribute
     */
    public Entity getType() {
        return type;
    }

    /**
     * Gets the parent entity containing this reference attribute.
     *
     * @return the parent entity containing this reference attribute
     */
    public Entity getParent() {
        return parent;
    }

    /**
     * Sets the parent entity containing this reference attribute.
     *
     * @param parent the parent entity containing this reference attribute
     */
    public void setParent(Entity parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reference))
            return false;

        Reference reference = (Reference) object;

        return new EqualsBuilder()
                .append(parent, reference.parent)
                .append(name, reference.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(parent)
                .append(name)
                .hashCode();
    }
}
