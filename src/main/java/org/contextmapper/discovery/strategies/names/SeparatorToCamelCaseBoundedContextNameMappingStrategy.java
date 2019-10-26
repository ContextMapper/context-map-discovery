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
package org.contextmapper.discovery.strategies.names;

/**
 * Maps Bounded Context name using a separator to Camel-Case notation.
 * <p>
 * For example:
 * - 'example-context' to 'ExampleContext'
 * - 'example_context' to 'ExampleContext'
 * - etc.
 *
 * @author Stefan Kapferer
 */
public class SeparatorToCamelCaseBoundedContextNameMappingStrategy implements BoundedContextNameMappingStrategy {

    private String separator;

    public SeparatorToCamelCaseBoundedContextNameMappingStrategy(String separator) {
        this.separator = separator;
    }

    @Override
    public String mapBoundedContextName(String name) {
        String[] parts = name.split(separator);
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!"".equals(part))
                sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1, part.length()));
        }
        return sb.toString();
    }

}
