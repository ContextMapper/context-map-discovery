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

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.model.BoundedContext;

import java.util.Set;

/**
 * Interface for all Bounded Context discovery strategies.
 *
 * @author Stefan Kapferer
 */
public interface BoundedContextDiscoveryStrategy {

    /**
     * Discovers Bounded Contexts
     *
     * @return a set of discovered Bounded Contexts
     */
    Set<BoundedContext> discoverBoundedContexts();

}
