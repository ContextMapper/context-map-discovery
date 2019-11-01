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

import org.contextmapper.discovery.model.BoundedContext;
import org.contextmapper.discovery.strategies.helper.AnnotationScanner;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Find Bounded Contexts by annotated Java types.
 *
 * @author Stefan Kapferer
 */
public class AnnotatedTypeBoundedContextDiscoveryStrategy extends AbstractBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    private String packageName;
    private Class<? extends Annotation> annotation;
    private String technology;

    public AnnotatedTypeBoundedContextDiscoveryStrategy(String packageName, Class<? extends Annotation> annotation, String technology) {
        this.packageName = packageName;
        this.annotation = annotation;
        this.technology = technology;
    }

    @Override
    public Set<BoundedContext> discoverBoundedContexts() {
        Set<BoundedContext> set = new HashSet<>();
        for (Class<?> type : new AnnotationScanner().scanForAnnotatedType(packageName, annotation)) {
            set.add(createBoundedContext(type.getSimpleName(), technology));
        }
        return set;
    }

}
