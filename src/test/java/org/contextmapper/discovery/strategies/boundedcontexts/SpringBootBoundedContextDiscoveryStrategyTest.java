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
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringBootBoundedContextDiscoveryStrategyTest {

    @Test
    public void canDiscoverSpringBootApplication() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("test.application.spring.boot")
                );

        // when
        Set<BoundedContext> boundedContexts = discoverer.discoverContextMap().getBoundedContexts();

        // then
        assertEquals(1, boundedContexts.size());
        BoundedContext bc = boundedContexts.iterator().next();
        assertEquals("TestSpringBoot", bc.getName());
        assertEquals("Spring Boot", bc.getTechnology());
    }

}
