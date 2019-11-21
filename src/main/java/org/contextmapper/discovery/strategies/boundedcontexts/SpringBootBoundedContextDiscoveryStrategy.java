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

import org.contextmapper.discovery.strategies.helper.AnnotationScanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Set;

public class SpringBootBoundedContextDiscoveryStrategy extends AbstractRESTResourceBasedBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    private String packageName;

    public SpringBootBoundedContextDiscoveryStrategy(String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected Set<Class<?>> findBoundedContextTypes() {
        return new AnnotationScanner().scanForAnnotatedType(packageName, SpringBootApplication.class);
    }

    @Override
    protected String findBoundedContextTechnology(Class<?> boundedContextType) {
        return "Spring Boot";
    }

    @Override
    protected Set<Class<?>> findResourceTypes(String packageName) {
        return new AnnotationScanner().scanForAnnotatedType(packageName, RequestMapping.class);
    }

    @Override
    protected String findResourcePath(Class<?> resourceType) {
        RequestMapping requestMapping = resourceType.getAnnotation(RequestMapping.class);
        if (requestMapping.value().length > 0)
            return requestMapping.value()[0];
        return "";
    }

    @Override
    protected Set<Method> findResourceMethods(Class<?> resourceType) {
        return new AnnotationScanner().scanForAnnotatedMethods(resourceType, PutMapping.class, GetMapping.class, RequestMapping.class);
    }

}
