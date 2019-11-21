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

import org.contextmapper.discovery.model.Method;
import org.contextmapper.discovery.model.Relationship;
import org.contextmapper.dsl.contextMappingDSL.*;
import org.contextmapper.tactic.dsl.tacticdsl.*;

import java.util.*;

import static org.contextmapper.discovery.model.DomainObjectType.ENTITY;

/**
 * Converts a {@link org.contextmapper.discovery.model.ContextMap} to the CML {@link org.contextmapper.dsl.contextMappingDSL.ContextMap}
 *
 * @author Stefan Kapferer
 */
public class ContextMapToCMLConverter {

    private Map<String, BoundedContext> boundedContextMap = new HashMap<>();
    private Map<org.contextmapper.discovery.model.DomainObject, DomainObject> domainObjectLookupMap = new HashMap<>();

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
        if (inputAggregate.getDiscoveryComment() != null && !"".equals(inputAggregate.getDiscoveryComment()))
            aggregate.setComment("// " + inputAggregate.getDiscoveryComment());
        for (org.contextmapper.discovery.model.DomainObject domainObject : inputAggregate.getDomainObjects()) {
            aggregate.getDomainObjects().add(convert(domainObject));
        }
        for (org.contextmapper.discovery.model.DomainObject domainObject : inputAggregate.getDomainObjects()) {
            convertDomainObjectMethods(domainObject);
        }
        Optional<Entity> rootEntity = aggregate.getDomainObjects().stream().filter(o -> o instanceof Entity).map(o -> (Entity) o)
                .filter(e -> e.getName().endsWith("_RootEntity")).findFirst();
        if (rootEntity.isPresent())
            rootEntity.get().setAggregateRoot(true);
        return aggregate;
    }

    private DomainObject convert(org.contextmapper.discovery.model.DomainObject inputDomainObject) {
        if (ENTITY.equals(inputDomainObject.getType()))
            return convertDomainObjectToEntity(inputDomainObject);
        return convertDomainObjectToValueObject(inputDomainObject);
    }

    private Entity convertDomainObjectToEntity(org.contextmapper.discovery.model.DomainObject inputDomainObject) {
        Entity entity = TacticdslFactory.eINSTANCE.createEntity();
        entity.setName(inputDomainObject.getName());
        if (inputDomainObject.getDiscoveryComment() != null && !"".equals(inputDomainObject.getDiscoveryComment()))
            entity.setComment("// " + inputDomainObject.getDiscoveryComment());
        domainObjectLookupMap.put(inputDomainObject, entity);
        return entity;
    }

    private ValueObject convertDomainObjectToValueObject(org.contextmapper.discovery.model.DomainObject inputDomainObject) {
        ValueObject valueObject = TacticdslFactory.eINSTANCE.createValueObject();
        valueObject.setName(inputDomainObject.getName());
        if (inputDomainObject.getDiscoveryComment() != null && !"".equals(inputDomainObject.getDiscoveryComment()))
            valueObject.setComment("// " + inputDomainObject.getDiscoveryComment());
        domainObjectLookupMap.put(inputDomainObject, valueObject);
        return valueObject;
    }

    private void convertDomainObjectMethods(org.contextmapper.discovery.model.DomainObject inputDomainObject) {
        DomainObject domainObject = this.domainObjectLookupMap.get(inputDomainObject);
        for (Method inputMethod : inputDomainObject.getMethods()) {
            DomainObjectOperation operation = TacticdslFactory.eINSTANCE.createDomainObjectOperation();
            operation.setName(inputMethod.getName());
            operation.setReturnType(createComplexType(inputMethod.getReturnType(), inputMethod.getReturnCollectionType()));
            operation.getParameters().addAll(createParameters(inputMethod.getParameters()));
            domainObject.getOperations().add(operation);
        }
    }

    private Set<Parameter> createParameters(Set<org.contextmapper.discovery.model.Parameter> inputParameters) {
        Set<Parameter> parameters = new HashSet<>();
        for (org.contextmapper.discovery.model.Parameter inputParameter : inputParameters) {
            Parameter parameter = TacticdslFactory.eINSTANCE.createParameter();
            parameter.setName(inputParameter.getName());
            parameter.setParameterType(createComplexType(inputParameter.getType(), inputParameter.getCollectionType()));
            parameters.add(parameter);
        }
        return parameters;
    }

    private ComplexType createComplexType(org.contextmapper.discovery.model.DomainObject inputDomainObject, String collectionType) {
        ComplexType complexType = TacticdslFactory.eINSTANCE.createComplexType();
        complexType.setDomainObjectType(this.domainObjectLookupMap.get(inputDomainObject));
        if (collectionType != null)
            complexType.setCollectionType(CollectionType.get(collectionType));
        return complexType;
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
        if (relationship.getExposedAggregatesComment() != null && !"".equals(relationship.getExposedAggregatesComment()))
            upstreamDownstreamRelationship.setExposedAggregatesComment("// " + relationship.getExposedAggregatesComment());
        return upstreamDownstreamRelationship;
    }

    private void updateEntityAttributesAndReferences() {
        for (Map.Entry<org.contextmapper.discovery.model.DomainObject, DomainObject> entry : this.domainObjectLookupMap.entrySet()) {
            updateDomainObject(entry.getKey(), entry.getValue());
        }
    }

    private void updateDomainObject(org.contextmapper.discovery.model.DomainObject inputDomainObject, DomainObject domainObject) {
        for (org.contextmapper.discovery.model.Attribute inputAttribute : inputDomainObject.getAttributes()) {
            Attribute attribute = TacticdslFactory.eINSTANCE.createAttribute();
            attribute.setName(inputAttribute.getName());
            attribute.setType(inputAttribute.getType());
            attribute.setCollectionType(CollectionType.get(inputAttribute.getCollectionType()));
            domainObject.getAttributes().add(attribute);
        }
        for (org.contextmapper.discovery.model.Reference inputReference : inputDomainObject.getReferences()) {
            Reference reference = TacticdslFactory.eINSTANCE.createReference();
            reference.setName(inputReference.getName());
            reference.setDomainObjectType(this.domainObjectLookupMap.get(inputReference.getType()));
            reference.setCollectionType(CollectionType.get(inputReference.getCollectionType()));
            domainObject.getReferences().add(reference);
        }
    }
}
