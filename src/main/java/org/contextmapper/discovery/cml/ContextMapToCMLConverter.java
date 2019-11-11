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
import org.contextmapper.tactic.dsl.tacticdsl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Converts a {@link org.contextmapper.discovery.model.ContextMap} to the CML {@link org.contextmapper.dsl.contextMappingDSL.ContextMap}
 *
 * @author Stefan Kapferer
 */
public class ContextMapToCMLConverter {

    private Map<String, BoundedContext> boundedContextMap = new HashMap<>();
    private Map<org.contextmapper.discovery.model.Entity, Entity> entityLookupMap = new HashMap<>();

    public ContextMappingModel convert(org.contextmapper.discovery.model.ContextMap inputMap) {
        ContextMappingModel model = ContextMappingDSLFactory.eINSTANCE.createContextMappingModel();
        ContextMap contextMap = ContextMappingDSLFactory.eINSTANCE.createContextMap();
        model.setMap(contextMap);

        for (org.contextmapper.discovery.model.BoundedContext boundedContext : inputMap.getBoundedContexts()) {
            BoundedContext bc = convert(boundedContext);
            model.getBoundedContexts().add(bc);
            contextMap.getBoundedContexts().add(bc);
        }

        for (Relationship relationship : inputMap.getRelationships()) {
            UpstreamDownstreamRelationship upstreamDownstreamRelationship = convert(relationship);
            if (!contextMap.getBoundedContexts().contains(upstreamDownstreamRelationship.getUpstream()))
                contextMap.getBoundedContexts().add(upstreamDownstreamRelationship.getUpstream());
            if (!contextMap.getBoundedContexts().contains(upstreamDownstreamRelationship.getDownstream()))
                contextMap.getBoundedContexts().add(upstreamDownstreamRelationship.getDownstream());
            contextMap.getRelationships().add(convert(relationship));
        }

        updateEntityAttributesAndReferences();

        return model;
    }

    private BoundedContext convert(org.contextmapper.discovery.model.BoundedContext inputContext) {
        BoundedContext bc = ContextMappingDSLFactory.eINSTANCE.createBoundedContext();
        bc.setName(inputContext.getName());
        bc.setImplementationTechnology(inputContext.getTechnology());
        for (org.contextmapper.discovery.model.Aggregate aggregate : inputContext.getAggregates()) {
            bc.getAggregates().add(convert(aggregate));
        }
        this.boundedContextMap.put(inputContext.getName(), bc);
        return bc;
    }

    private Aggregate convert(org.contextmapper.discovery.model.Aggregate inputAggregate) {
        Aggregate aggregate = ContextMappingDSLFactory.eINSTANCE.createAggregate();
        aggregate.setName(inputAggregate.getName());
        aggregate.setComment("// " + inputAggregate.getDiscoveryComment());
        for (org.contextmapper.discovery.model.Entity entity : inputAggregate.getEntities()) {
            aggregate.getDomainObjects().add(convert(entity));
        }
        return aggregate;
    }

    private Entity convert(org.contextmapper.discovery.model.Entity inputEntity) {
        Entity entity = TacticdslFactory.eINSTANCE.createEntity();
        entity.setName(inputEntity.getName());
        entity.setComment("// " + inputEntity.getDiscoveryComment());
        entityLookupMap.put(inputEntity, entity);
        return entity;
    }

    private UpstreamDownstreamRelationship convert(Relationship relationship) {
        UpstreamDownstreamRelationship upstreamDownstreamRelationship = ContextMappingDSLFactory.eINSTANCE.createUpstreamDownstreamRelationship();
        upstreamDownstreamRelationship.setUpstream(this.boundedContextMap.get(relationship.getUpstream().getName()));
        upstreamDownstreamRelationship.setDownstream(this.boundedContextMap.get(relationship.getDownstream().getName()));
        for (org.contextmapper.discovery.model.Aggregate aggregate : relationship.getExposedAggregates()) {
            Optional<Aggregate> cmlAggregate = upstreamDownstreamRelationship.getUpstream().getAggregates().stream().filter(a -> a.getName().equals(aggregate.getName())).findFirst();
            if (cmlAggregate.isPresent())
                upstreamDownstreamRelationship.getUpstreamExposedAggregates().add(cmlAggregate.get());
        }
        upstreamDownstreamRelationship.setExposedAggregatesComment("// " + relationship.getExposedAggregatesComment());
        return upstreamDownstreamRelationship;
    }

    private void updateEntityAttributesAndReferences() {
        for (Map.Entry<org.contextmapper.discovery.model.Entity, Entity> entry : this.entityLookupMap.entrySet()) {
            updateEntity(entry.getKey(), entry.getValue());
        }
    }

    private void updateEntity(org.contextmapper.discovery.model.Entity inputEntity, Entity entity) {
        for (org.contextmapper.discovery.model.Attribute inputAttribute : inputEntity.getAttributes()) {
            Attribute attribute = TacticdslFactory.eINSTANCE.createAttribute();
            attribute.setName(inputAttribute.getName());
            attribute.setType(inputAttribute.getType());
            attribute.setCollectionType(CollectionType.get(inputAttribute.getCollectionType()));
            entity.getAttributes().add(attribute);
        }
        for (org.contextmapper.discovery.model.Reference inputReference : inputEntity.getReferences()) {
            Reference reference = TacticdslFactory.eINSTANCE.createReference();
            reference.setName(inputReference.getName());
            reference.setDomainObjectType(this.entityLookupMap.get(inputReference.getType()));
            reference.setCollectionType(CollectionType.get(inputReference.getCollectionType()));
            entity.getReferences().add(reference);
        }
    }
}
