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

/**
 * Represents a parameter of a domain object method.
 *
 * @author Stefan Kapferer
 */
public class Parameter {

    private String name;
    private DomainObject type;
    private String collectionType;

    public Parameter(String name, DomainObject type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the name of the parameter.
     *
     * @return the name of the parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type of the parameter.
     *
     * @return the type of the parameter
     */
    public DomainObject getType() {
        return type;
    }

    /**
     * Sets the collection type of the parameter
     *
     * @param collectionType the collection type of the parameter
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * Gets the collection type of the parameter.
     *
     * @return the collection type of the parameter
     */
    public String getCollectionType() {
        return collectionType;
    }
}
