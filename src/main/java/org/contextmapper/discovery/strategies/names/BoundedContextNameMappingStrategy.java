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
 * Interface for name mapping strategies. Used in {@link org.contextmapper.discovery.ContextMapDiscoverer} to provide
 * different approaches to lookup discovered Bounded Contexts (relationship discovering strategies may search for
 * Bounded Contexts with different namings).
 *
 * @author Stefan Kapferer
 */
public interface BoundedContextNameMappingStrategy {

    /**
     * Maps a Bounded Context name to another one identifying the same context.
     *
     * @param name the given Bounded Context name
     * @return the other Bounded Context name used for the same context
     */
    String mapBoundedContextName(String name);

}
