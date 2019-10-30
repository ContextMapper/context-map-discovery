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

import org.contextmapper.discovery.model.Aggregate;
import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.model.Entity;
import org.contextmapper.discovery.strategies.helper.AnnotationScanner;
import org.contextmapper.discovery.strategies.helper.ReflectionHelpers;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SpringBootBoundedContextDiscoveryStrategy extends AbstractBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    private String packageName;
    private Set<String> aggregateNames;

    public SpringBootBoundedContextDiscoveryStrategy(String packageName) {
        this.packageName = packageName;
        this.aggregateNames = new HashSet<>();
    }

    @Override
    public Set<BoundedContext> discoverBoundedContexts() {
        AnnotatedTypeBoundedContextDiscoveryStrategy annotationStrategy =
                new AnnotatedTypeBoundedContextDiscoveryStrategy(packageName, SpringBootApplication.class, "Spring Boot");
        Set<BoundedContext> set = new HashSet<>();
        for (Class<?> type : new AnnotationScanner().scanForAnnotatedType(packageName, SpringBootApplication.class)) {
            String name = type.getSimpleName();
            if (name.endsWith("Application"))
                name = name.substring(0, name.length() - 11);
            BoundedContext bc = createBoundedContext(name, "Spring Boot");
            bc.addAggregates(discoverAggregates(bc, type.getPackage().getName()));
            set.add(bc);
        }
        return set;
    }

    private Set<Aggregate> discoverAggregates(BoundedContext bc, String packageName) {
        Set<Aggregate> resultSet = new HashSet<>();
        for (Class<?> type : new AnnotationScanner().scanForAnnotatedType(packageName, RequestMapping.class)) {
            RequestMapping requestMapping = type.getAnnotation(RequestMapping.class);
            if (requestMapping.value().length == 1) {
                Aggregate aggregate = createAggregate(bc, requestMapping.value()[0]);
                aggregate.addEntities(discoverEntities(type));
                resultSet.add(aggregate);
            }
        }
        return resultSet;
    }

    private Aggregate createAggregate(BoundedContext parentContext, String resourcePath) {
        return new Aggregate(getAggregateName(parentContext.getName(), resourcePath));
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

    private Set<Entity> discoverEntities(Class<?> controllerType) {
        Set<Entity> entities = new HashSet<>();
        Set<Class<?>> types = getInputAndOutputTypesOfResourceMethods(controllerType);
        for (Class<?> type : types) {
            entities.add(new Entity(type.getName(), type.getSimpleName()));
        }
        return entities;
    }

    private Set<Class<?>> getInputAndOutputTypesOfResourceMethods(Class<?> type) {
        Set<Class<?>> inputOutputTypes = new HashSet<>();
        for (Method method : new AnnotationScanner().scanForAnnotatedMethods(type, PutMapping.class)) {
            if (method.getGenericReturnType() instanceof ParameterizedType)
                inputOutputTypes.addAll(new ReflectionHelpers().getActualTypesOfParameterizedType((ParameterizedType) method.getGenericReturnType()));

            inputOutputTypes.add(method.getGenericReturnType().getClass());
            inputOutputTypes.addAll(Arrays.asList(method.getParameterTypes()));
        }
        return inputOutputTypes.stream().filter(c -> c.getPackage().getName().startsWith(packageName)).collect(Collectors.toSet());
    }

}
