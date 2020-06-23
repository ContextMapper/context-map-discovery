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

/**
 * Represents a discovered type. Can occur in: operation return and parameter types
 *
 * @author Stefan Kapferer
 */
public class Type {

    private TypeKind kind;
    private String primitiveType;
    private DomainObject domainObjectType;
    private String collectionType;

    /**
     * Creates a primitive type.
     *
     * @param primitiveType the name of the primitive (or unknown; no domain object available for reference) type.
     */
    public Type(String primitiveType) {
        if (primitiveType == null || "".equals(primitiveType))
            throw new RuntimeException("Primitive type cannot be null or empty String!");

        this.kind = TypeKind.PRIMITIVE;
        this.primitiveType = primitiveType;
        this.collectionType = "";
    }

    /**
     * Creates a domain object type.
     *
     * @param domainObjectType the domain object that represents the type.
     */
    public Type(DomainObject domainObjectType) {
        if (domainObjectType == null)
            throw new RuntimeException("The domain object of a domain object type cannot be null!");

        this.kind = TypeKind.DOMAIN_OBJECT;
        this.domainObjectType = domainObjectType;
        this.collectionType = "";
    }

    /**
     * Returns the kind of the type. Primitive or domain object.
     *
     * @return the kind of the type.
     */
    public TypeKind getKind() {
        return kind;
    }

    /**
     * Returns the name of the type as a String.
     *
     * @return the name of the type.
     */
    public String getName() {
        if (kind == TypeKind.DOMAIN_OBJECT)
            return domainObjectType.getName();
        else
            return primitiveType;
    }

    /**
     * Returns the domain object; in case it is a domain object type.
     *
     * @return the domain object that represents the domain object type.
     */
    public DomainObject getDomainObjectType() {
        if (this.kind != TypeKind.DOMAIN_OBJECT)
            throw new RuntimeException("This is not a domain object type!");
        return domainObjectType;
    }

    /**
     * Returns the name of the primitive (or unknown) type; in case it is a primitive type.
     *
     * @return the name of the primitive type.
     */
    public String getPrimitiveType() {
        if (this.kind != TypeKind.PRIMITIVE)
            throw new RuntimeException("This is not a primitive type");
        return primitiveType;
    }

    /**
     * Indicates whether a type is a primitive (or unknown) type or not.
     *
     * @return true, in case it is a primitive type, false otherwise.
     */
    public boolean isPrimitiveType() {
        return kind == TypeKind.PRIMITIVE;
    }

    /**
     * Indicates whether a type is a domain object type or not.
     *
     * @return true, in case is is a domain object type, false otherwise.
     */
    public boolean isDomainObjectType() {
        return kind == TypeKind.DOMAIN_OBJECT;
    }

    /**
     * Indicates whether a type is a collection type or not.
     *
     * @return true, if a type is a collection type, false otherwise.
     */
    public boolean isCollectionType() {
        return collectionType != null && !"".equals(collectionType);
    }

    /**
     * Sets the collection type (optional).
     *
     * @param collectionType the collection type to be set.
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * Returns the collection type; in case the type is a collection type.
     *
     * @return the collection type.
     */
    public String getCollectionType() {
        if (!isCollectionType())
            throw new RuntimeException("This type is not a collection type");
        return collectionType;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Type))
            return false;

        Type type = (Type) object;

        if (kind == TypeKind.DOMAIN_OBJECT)
            return new EqualsBuilder()
                    .append(domainObjectType, type.domainObjectType)
                    .append(collectionType, type.collectionType)
                    .isEquals();
        else
            return new EqualsBuilder()
                    .append(primitiveType, type.primitiveType)
                    .append(collectionType, type.collectionType)
                    .isEquals();
    }

    @Override
    public int hashCode() {
        if (kind == TypeKind.DOMAIN_OBJECT)
            return new HashCodeBuilder()
                    .append(domainObjectType)
                    .append(kind)
                    .append(collectionType)
                    .hashCode();
        else
            return new HashCodeBuilder()
                    .append(primitiveType)
                    .append(kind)
                    .append(collectionType)
                    .hashCode();
    }
}
