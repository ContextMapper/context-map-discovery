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
package org.contextmapper.discovery.strategies.boundedcontexts;

import org.contextmapper.discovery.model.*;
import org.contextmapper.discovery.strategies.helper.ReflectionHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRESTResourceBasedBoundedContextDiscoveryStrategy extends AbstractBoundedContextDiscoveryStrategy {

    private static final String AGG_ROOT_ENTITY_POSTFIX = "_RootEntity";

    protected Set<String> aggregateNames;
    protected ReflectionHelpers reflectionHelpers;
    protected Map<Aggregate, Map<Class<?>, DomainObject>> domainObjectMap;
    protected Set<String> discoveredDomainObjectNames;

    public AbstractRESTResourceBasedBoundedContextDiscoveryStrategy() {
        this.aggregateNames = new HashSet<>();
        this.reflectionHelpers = new ReflectionHelpers();
        this.domainObjectMap = new HashMap<>();
        this.discoveredDomainObjectNames = new HashSet<>();
    }

    /**
     * Discover Bounded Contexts by certain types representing the contexts.
     */
    @Override
    public Set<BoundedContext> discoverBoundedContexts() {
        Set<BoundedContext> set = new HashSet<>();
        for (Class<?> type : findBoundedContextTypes()) {
            String name = type.getSimpleName();
            if (name.endsWith("Application"))
                name = name.substring(0, name.length() - 11);
            BoundedContext bc = createBoundedContext(name, findBoundedContextTechnology(type));
            bc.addAggregates(discoverAggregates(bc, type.getPackage().getName()));
            set.add(bc);
        }
        updateDomainObjectAttributesAndReferences();
        return set;
    }

    /**
     * Find types representing a Bounded Context.
     */
    protected abstract Set<Class<?>> findBoundedContextTypes();

    /**
     * Find the implementation technology of a Bounded Context by the type representing it.
     */
    protected abstract String findBoundedContextTechnology(Class<?> boundedContextType);

    /**
     * Find types representing an Aggregate/resource (within a given package).
     */
    protected abstract Set<Class<?>> findResourceTypes(String packageName);

    /**
     * Find RESTful HTTP resource path by the given resource type.
     */
    protected abstract String findResourcePath(Class<?> resourceType);

    /**
     * Find RESTful HTTP operations by it methods in a given resource type.
     */
    protected abstract Set<Method> findResourceMethods(Class<?> resourceType);

    /**
     * Discover Aggregates for RESTful HTTP resources (annotated types)
     */
    protected Set<Aggregate> discoverAggregates(BoundedContext bc, String packageName) {
        Set<Aggregate> resultSet = new HashSet<>();
        for (Class<?> type : findResourceTypes(packageName)) {
            String resourePath = findResourcePath(type);
            if (resourePath == null || "".equals(resourePath))
                continue;
            Aggregate aggregate = createAggregate(bc, resourePath);
            this.domainObjectMap.put(aggregate, new HashMap<>());
            aggregate.addDomainObject(createRootEntity(aggregate.getName()));
            aggregate.addDomainObjects(discoverValueObjectsByMethods(aggregate, type, packageName));
            aggregate.setDiscoveryComment("This Aggregate has been created on the basis of the RESTful HTTP controller " + type.getName() + ".");
            resultSet.add(aggregate);
        }
        return resultSet;
    }

    /**
     * Create an Aggregate for a RESTful HTTP endpoint/resource.
     */
    protected Aggregate createAggregate(BoundedContext parentContext, String resourcePath) {
        return new Aggregate(getAggregateName(parentContext.getName(), resourcePath));
    }

    protected DomainObject createRootEntity(String aggregateName) {
        return new DomainObject(DomainObjectType.ENTITY, aggregateName + AGG_ROOT_ENTITY_POSTFIX);
    }

    private String getAggregateName(String boundedContextName, String resourcePath) {
        String name = resourcePath;
        if (name.startsWith("/"))
            name = name.substring(1);
        name = name.replaceAll("/", "_");
        name = name.replaceAll("-", "_");

        if (this.aggregateNames.contains(name))
            name = boundedContextName + "_" + name;

        int counter = 1;
        while (this.aggregateNames.contains(name)) {
            name = name + "_" + counter;
            counter++;
        }

        this.aggregateNames.add(name);
        return name;
    }

    protected Set<DomainObject> discoverValueObjectsByMethods(Aggregate aggregate, Class<?> controllerType, String packageName) {
        Set<DomainObject> valueObjects = new HashSet<>();
        for (Method method : findResourceMethods(controllerType)) {
            org.contextmapper.discovery.model.Method aggRootMethod = new org.contextmapper.discovery.model.Method(method.getName());
            DiscoveredType returnType = getMethodReturnType(method, packageName);
            if (returnType != null) {
                DomainObject returnTypeObject = createValueObjectFromType(aggregate, returnType.domainType);
                valueObjects.add(returnTypeObject);
                aggRootMethod.setReturnType(returnTypeObject);
                aggRootMethod.setReturnCollectionType(returnType.collectionType);
            }
            Set<DiscoveredParameterType> parameterTypes = getMethodParameterTypes(method, packageName);
            Set<Parameter> parameterTypeObjects = createValueObjectParameters(aggregate, parameterTypes.toArray(new DiscoveredParameterType[parameterTypes.size()]));
            valueObjects.addAll(parameterTypeObjects.stream().map(p -> p.getType()).collect(Collectors.toSet()));
            aggRootMethod.addParameters(parameterTypeObjects);
            Optional<DomainObject> aggRootEntity = aggregate.getDomainObjects().stream().filter(o -> o.getName().endsWith(AGG_ROOT_ENTITY_POSTFIX)).findFirst();
            if (aggRootEntity.isPresent())
                aggRootEntity.get().addMethod(aggRootMethod);
        }
        return valueObjects;
    }

    private DiscoveredType getMethodReturnType(Method method, String packageName) {
        DiscoveredType returnType = null;
        if (method.getGenericReturnType() instanceof ParameterizedType) {
            returnType = getType((ParameterizedType) method.getGenericReturnType());
        } else {
            returnType = new DiscoveredType(null, method.getReturnType());
        }
        if (returnType != null && returnType.domainType.getPackage().getName().startsWith(packageName))
            return returnType;
        return null;
    }

    protected Set<DiscoveredParameterType> getMethodParameterTypes(Method method, String packageName) {
        Set<DiscoveredParameterType> parameterTypes = new HashSet<>();
        for (java.lang.reflect.Parameter parameter : method.getParameters()) {
            if (parameter.getParameterizedType() instanceof ParameterizedType) {
                parameterTypes.add(new DiscoveredParameterType(parameter.getName(), getType((ParameterizedType) parameter.getParameterizedType())));
            } else {
                parameterTypes.add(new DiscoveredParameterType(parameter.getName(), new DiscoveredType(null, parameter.getType())));
            }
        }
        return parameterTypes.stream().filter(p -> p.type.domainType.getPackage().getName().startsWith(packageName)).collect(Collectors.toSet());
    }

    private Set<Parameter> createValueObjectParameters(Aggregate aggregate, DiscoveredParameterType... parameterTypes) {
        Set<Parameter> valueObjectParameters = new HashSet<>();
        for (DiscoveredParameterType parameterType : parameterTypes) {
            Parameter parameter = new Parameter(parameterType.parameterName, createValueObjectFromType(aggregate, parameterType.type.domainType));
            parameter.setCollectionType(parameterType.type.collectionType);
            valueObjectParameters.add(parameter);
        }
        return valueObjectParameters;
    }

    private DomainObject createValueObjectFromType(Aggregate aggregate, Class<?> type) {
        if (this.domainObjectMap.get(aggregate).containsKey(type))
            return this.domainObjectMap.get(aggregate).get(type);

        String valueObjectName = type.getSimpleName();
        if (this.discoveredDomainObjectNames.contains(valueObjectName))
            valueObjectName = aggregate.getName() + "_" + valueObjectName;
        int counter = 1;
        while (this.discoveredDomainObjectNames.contains(valueObjectName)) {
            valueObjectName = valueObjectName + "_" + counter;
            counter++;
        }
        this.discoveredDomainObjectNames.add(valueObjectName);
        DomainObject domainObject = new DomainObject(DomainObjectType.VALUE_OBJECT, valueObjectName, type.getName());
        domainObject.setDiscoveryComment("This value object has been derived from the class " + type.getName() + ".");
        this.domainObjectMap.get(aggregate).put(type, domainObject);
        return domainObject;
    }

    private void updateDomainObjectAttributesAndReferences() {
        for (Map.Entry<Aggregate, Map<Class<?>, DomainObject>> entry : this.domainObjectMap.entrySet()) {
            entry.getValue().entrySet().forEach(e -> createAttributesAndReferences4DomainObject(e.getValue(), e.getKey()));
        }
    }

    private void createAttributesAndReferences4DomainObject(DomainObject domainObject, Class<?> domainObjectType) {
        for (Field field : reflectionHelpers.getAllFieldsOfType(domainObjectType)) {
            String collectionType = null;
            if (reflectionHelpers.isCollectionType(field.getType()))
                collectionType = field.getType().getSimpleName();
            Class<?> fieldType = getType(field);
            String simpleName = fieldType.getSimpleName();
            if (fieldType.getSimpleName().endsWith("[]")) {
                simpleName = simpleName.substring(0, simpleName.length() - 2);
                collectionType = "List";
            }

            // reference outside aggregate (only use this if object is not part of aggregate)
            DomainObject globallySearchedObject = searchDomainObjectInAllAggregates(fieldType);

            // search in aggregate first:
            if (this.domainObjectMap.get(domainObject.getParent()).containsKey(fieldType)) {
                domainObject.addReference(createReference(field.getName(), this.domainObjectMap.get(domainObject.getParent()).get(fieldType), collectionType));
            } else if (globallySearchedObject != null) {
                domainObject.addReference(createReference(field.getName(), globallySearchedObject, collectionType));
            } else {
                Attribute attribute = new Attribute(simpleName, field.getName());
                attribute.setCollectionType(collectionType);
                domainObject.addAttribute(attribute);
            }
        }
    }

    private DomainObject searchDomainObjectInAllAggregates(Class<?> type) {
        for (Map.Entry<Aggregate, Map<Class<?>, DomainObject>> entry : this.domainObjectMap.entrySet()) {
            if (entry.getValue().containsKey(type))
                return entry.getValue().get(type);
        }
        return null;
    }

    private Reference createReference(String name, DomainObject domainObject, String collectionType) {
        Reference reference = new Reference(domainObject, name);
        reference.setCollectionType(collectionType);
        domainObject.addReference(reference);
        return reference;
    }

    private Class<?> getType(Field field) {
        if (reflectionHelpers.isCollectionType(field.getType())) {
            return reflectionHelpers.getActualTypesOfParameterizedType((ParameterizedType) field.getGenericType()).iterator().next();
        } else {
            return field.getType();
        }
    }

    private DiscoveredType getType(ParameterizedType type) {
        if (type.getActualTypeArguments().length < 1)
            throw new RuntimeException("ParameterizedTypes without parameters not supported!");

        // we assume there is only one return type for now
        Type paramType = type.getActualTypeArguments()[0];

        if (paramType instanceof ParameterizedType) {
            return getType((ParameterizedType) paramType);
        } else if (paramType instanceof Class<?>) {
            String collectionType = null;
            if (type.getRawType() instanceof Class<?> && reflectionHelpers.isCollectionType((Class<?>) type.getRawType()))
                collectionType = ((Class<?>) type.getRawType()).getSimpleName();
            return new DiscoveredType(collectionType, (Class<?>) paramType);
        } else {
            return new DiscoveredType(null, (Class<?>) type.getRawType());
        }
    }

    private class DiscoveredType {
        private Class<?> domainType;
        private String collectionType;

        DiscoveredType(String collectionType, Class<?> domainType) {
            this.domainType = domainType;
            this.collectionType = collectionType;
        }
    }

    private class DiscoveredParameterType {
        private DiscoveredType type;
        private String parameterName;

        DiscoveredParameterType(String parameterName, DiscoveredType discoveredType) {
            this.parameterName = parameterName;
            this.type = discoveredType;
        }
    }

}
