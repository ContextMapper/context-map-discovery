/*
 * Copyright 2020 The Context Mapper Project Team
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
import org.contextmapper.discovery.model.*;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OASBoundedContextDiscoveryStrategyTest {

    private static final String SAMPLE_CONTRACT_LOCATION = "./src/test/resources/test/oas-tests/sample-contract.yml";

    @Test
    public void canDiscoverContext() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(SAMPLE_CONTRACT_LOCATION));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();

        // then
        assertEquals(1, contextMap.getBoundedContexts().size());
        BoundedContext bc = contextMap.getBoundedContexts().iterator().next();
        assertEquals("ReferenceManagementServiceAPI", bc.getName());
    }

    @Test
    public void canDiscoveryAggregates() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(SAMPLE_CONTRACT_LOCATION));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();

        // then
        BoundedContext bc = contextMap.getBoundedContexts().stream().filter(context -> context.getName().equals("ReferenceManagementServiceAPI")).findFirst().get();
        assertNotNull(bc);
        assertEquals(1, bc.getAggregates().size());
        Aggregate aggregate = bc.getAggregates().iterator().next();
        assertEquals("PaperArchiveFacade", aggregate.getName());
        assertEquals("general data-oriented endpoint", aggregate.getDiscoveryComment());
    }

    @Test
    public void canDiscoverServices() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(SAMPLE_CONTRACT_LOCATION));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();

        // then
        BoundedContext bc = contextMap.getBoundedContexts().stream().filter(context -> context.getName().equals("ReferenceManagementServiceAPI")).findFirst().get();
        assertNotNull(bc);
        Aggregate aggregate = bc.getAggregates().stream().filter(agg -> agg.getName().equals("PaperArchiveFacade")).findFirst().get();
        assertNotNull(bc);
        assertEquals(1, aggregate.getServices().size());
        assertEquals("PaperArchiveFacadeService", aggregate.getServices().iterator().next().getName());
    }

    @Test
    public void canDiscoverServiceOperations() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(SAMPLE_CONTRACT_LOCATION));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();
        BoundedContext bc = contextMap.getBoundedContexts().iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        Service service = aggregate.getServices().iterator().next();

        // then
        assertNotNull(service);
        assertEquals(3, service.getOperations().size());
        Set<String> operationNames = service.getOperations().stream().map(o -> o.getName()).collect(Collectors.toSet());
        assertTrue(operationNames.contains("createPaperItem"));
        assertTrue(operationNames.contains("lookupPapersFromAuthor"));
        assertTrue(operationNames.contains("convertToMarkdownForWebsite"));
    }

    @Test
    public void canDiscoverParameters() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy(SAMPLE_CONTRACT_LOCATION));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();
        BoundedContext bc = contextMap.getBoundedContexts().iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        Service service = aggregate.getServices().iterator().next();
        Method operation = service.getOperations().stream().filter(o -> o.getName().equals("lookupPapersFromAuthor")).findFirst().get();

        // then
        assertNotNull(operation);
        assertEquals(1, operation.getParameters().size());
        Parameter parameter = operation.getParameters().iterator().next();
        assertEquals("Parameter1", parameter.getName());
        assertEquals("String", parameter.getType().getName());
        assertTrue(parameter.getType().isPrimitiveType());
        assertFalse(parameter.getType().isCollectionType());
    }

    @Test
    public void canDiscoverObjectInParameter() {
        // given
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(new OASBoundedContextDiscoveryStrategy("./src/test/resources/test/oas-tests/sample-object-as-parameter.yml"));

        // when
        ContextMap contextMap = discoverer.discoverContextMap();
        BoundedContext bc = contextMap.getBoundedContexts().iterator().next();
        Aggregate aggregate = bc.getAggregates().iterator().next();
        Service service = aggregate.getServices().iterator().next();
        Method operation = service.getOperations().stream().filter(o -> o.getName().equals("lookupPapersFromAuthor")).findFirst().get();

        // then
        assertNotNull(operation);
        assertEquals(1, operation.getParameters().size());
        assertEquals(2, aggregate.getDomainObjects().size());
        Parameter parameter = operation.getParameters().iterator().next();
        DomainObject object = aggregate.getDomainObjects().stream().filter(o -> o.getName().equals("Parameter1Type")).findFirst().get();
        DomainObject referencedObject = aggregate.getDomainObjects().stream().filter(o -> o.getName().equals("Attr3Type")).findFirst().get();
        assertNotNull(object);
        assertNotNull(referencedObject);
        assertEquals("Parameter1", parameter.getName());
        assertEquals("Parameter1Type", parameter.getType().getName());
        assertTrue(parameter.getType().isDomainObjectType());
        assertEquals("Parameter1Type", object.getName());
        assertEquals(3, object.getAttributes().size());
        Attribute attribute1 = object.getAttributes().stream().filter(a -> a.getName().equals("attr1")).findFirst().get();
        Attribute attribute2 = object.getAttributes().stream().filter(a -> a.getName().equals("attr2")).findFirst().get();
        Attribute attribute3 = object.getAttributes().stream().filter(a -> a.getName().equals("attr3")).findFirst().get();
        assertNotNull(attribute1);
        assertNotNull(attribute2);
        assertEquals("String", attribute1.getType().getName());
        assertEquals("Integer", attribute2.getType().getName());
        assertEquals(referencedObject, attribute3.getType().getDomainObjectType());
        Attribute subAttribute1 = referencedObject.getAttributes().stream().filter(a -> a.getName().equals("subattr1")).findFirst().get();
        Attribute subAttribute2 = referencedObject.getAttributes().stream().filter(a -> a.getName().equals("subattr2")).findFirst().get();
        assertEquals("String", subAttribute1.getType().getName());
        assertEquals("String", subAttribute2.getType().getName());
    }

}
