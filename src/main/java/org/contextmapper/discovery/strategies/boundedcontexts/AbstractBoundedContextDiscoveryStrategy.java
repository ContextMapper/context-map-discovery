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

public abstract class AbstractBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    protected BoundedContext createBoundedContext(String name, String technology) {
        BoundedContext bc = new BoundedContext(name);
        bc.setTechnology(technology);
        return bc;
    }

}
