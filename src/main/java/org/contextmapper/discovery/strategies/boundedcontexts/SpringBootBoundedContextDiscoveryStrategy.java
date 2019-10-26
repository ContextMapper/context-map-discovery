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
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

public class SpringBootBoundedContextDiscoveryStrategy extends AbstractBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    private String packageName;

    public SpringBootBoundedContextDiscoveryStrategy(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Set<BoundedContext> discoverBoundedContexts() {
        AnnotatedTypeBoundedContextDiscoveryStrategy annotationStrategy =
                new AnnotatedTypeBoundedContextDiscoveryStrategy(packageName, SpringBootApplication.class, "Spring Boot");
        Set<BoundedContext> set = annotationStrategy.discoverBoundedContexts();
        set.stream().forEach(bc -> {
            if (bc.getName().endsWith("Application"))
                bc.setName(bc.getName().substring(0, bc.getName().length() - 11));
        });
        return set;
    }

}
