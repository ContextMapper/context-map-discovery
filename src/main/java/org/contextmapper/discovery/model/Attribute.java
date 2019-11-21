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
 * Represents an entity attribute (primitive types).
 * For references to other non-primitive types use {@link Reference}.
 *
 * @author Stefan Kapferer
 */
public class Attribute {

    private DomainObject parent;
    private String name;
    private String type;
    private String collectionType;

    public Attribute(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Gets the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the attribute.
     *
     * @return the type of the attribute
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the parent domain object containing this attribute.
     *
     * @return the parent domain object containing this attribute
     */
    public DomainObject getParent() {
        return parent;
    }

    /**
     * Sets the parent domain object containing this attribute.
     *
     * @param parent the parent domain object containing this attribute
     */
    public void setParent(DomainObject parent) {
        this.parent = parent;
    }

    /**
     * Sets the collection type of the reference attribute.
     *
     * @param collectionType the collection type of the reference attribute
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * Gets the collection type of the reference attribute.
     *
     * @return the collection type of the reference attribute
     */
    public String getCollectionType() {
        return collectionType;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Attribute))
            return false;

        Attribute attribute = (Attribute) object;

        return new EqualsBuilder()
                .append(parent, attribute.parent)
                .append(name, attribute.name)
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
