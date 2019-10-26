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
package org.contextmapper.discovery.cml;

import org.contextmapper.discovery.model.Relationship;
import org.contextmapper.dsl.contextMappingDSL.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a {@link org.contextmapper.discovery.model.ContextMap} to the CML {@link org.contextmapper.dsl.contextMappingDSL.ContextMap}
 *
 * @author Stefan Kapferer
 */
public class ContextMapToCMLConverter {

    private Map<String, BoundedContext> boundedContextMap = new HashMap<>();

    public ContextMappingModel convert(org.contextmapper.discovery.model.ContextMap inputMap) {
        ContextMappingModel model = ContextMappingDSLFactory.eINSTANCE.createContextMappingModel();
        ContextMap contextMap = ContextMappingDSLFactory.eINSTANCE.createContextMap();
        model.setMap(contextMap);

        for (org.contextmapper.discovery.model.BoundedContext boundedContext : inputMap.getBoundedContexts()) {
            model.getBoundedContexts().add(convert(boundedContext));
        }

        for (Relationship relationship : inputMap.getRelationships()) {
            UpstreamDownstreamRelationship upstreamDownstreamRelationship = convert(relationship);
            if (!contextMap.getBoundedContexts().contains(upstreamDownstreamRelationship.getUpstream()))
                contextMap.getBoundedContexts().add(upstreamDownstreamRelationship.getUpstream());
            if (!contextMap.getBoundedContexts().contains(upstreamDownstreamRelationship.getDownstream()))
                contextMap.getBoundedContexts().add(upstreamDownstreamRelationship.getDownstream());
            contextMap.getRelationships().add(convert(relationship));
        }

        return model;
    }

    private BoundedContext convert(org.contextmapper.discovery.model.BoundedContext inputContext) {
        BoundedContext bc = ContextMappingDSLFactory.eINSTANCE.createBoundedContext();
        bc.setName(inputContext.getName());
        bc.setImplementationTechnology(inputContext.getTechnology());
        this.boundedContextMap.put(inputContext.getName(), bc);
        return bc;
    }

    private UpstreamDownstreamRelationship convert(Relationship relationship) {
        UpstreamDownstreamRelationship upstreamDownstreamRelationship = ContextMappingDSLFactory.eINSTANCE.createUpstreamDownstreamRelationship();
        upstreamDownstreamRelationship.setUpstream(this.boundedContextMap.get(relationship.getUpstream().getName()));
        upstreamDownstreamRelationship.setDownstream(this.boundedContextMap.get(relationship.getDownstream().getName()));
        return upstreamDownstreamRelationship;
    }

}
