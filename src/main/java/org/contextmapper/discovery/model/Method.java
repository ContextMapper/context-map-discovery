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
 * Represents a method in a domain object.
 *
 * @author Stefan Kapferer
 */
public class Method {

    private DomainObject parent;
    private String name;
    private Type returnType;
    private Set<Parameter> parameters;

    public Method(String name) {
        this.name = name;
        this.parameters = new HashSet<>();
    }

    /**
     * Gets the name of the method.
     *
     * @return the name of the method
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the return type of the method.
     *
     * @param returnType the return type of the method
     */
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    /**
     * Gets the return type of the method.
     *
     * @return the return type of the method
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * Adds a new parameter to the method.
     *
     * @param parameter the parameter to be added to the method
     */
    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    /**
     * Adds a set of new parameters to the method.
     *
     * @param parameters the set of parameters to be added to the method
     */
    public void addParameters(Set<Parameter> parameters) {
        this.parameters.addAll(parameters);
    }

    /**
     * Gets the parameters of the method.
     *
     * @return the set of parameters of the method
     */
    public Set<Parameter> getParameters() {
        return new HashSet<>(parameters);
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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Method))
            return false;

        Method method = (Method) object;

        return new EqualsBuilder()
                .append(parent, method.parent)
                .append(name, method.name)
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
