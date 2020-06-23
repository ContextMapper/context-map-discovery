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

import com.google.common.collect.Sets;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.models.RefType;
import org.contextmapper.discovery.cml.CMLPrimitiveTypeMapper;
import org.contextmapper.discovery.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Discovers Bounded Contexts with OpenAPI specifications as input.
 *
 * @author Stefan Kapferer
 */
public class OASBoundedContextDiscoveryStrategy implements BoundedContextDiscoveryStrategy {

    private static Logger LOG = LoggerFactory.getLogger(OASBoundedContextDiscoveryStrategy.class);

    private static final String JSON_MEDIA_TYPE = "application/json";

    private Set<String> oasLocations;
    private CMLPrimitiveTypeMapper typeMapper;
    private Map<Aggregate, Map<String, DomainObject>> domainObjectMap;
    private OpenAPI currentOAS;

    public OASBoundedContextDiscoveryStrategy(String... oasLocations) {
        this.oasLocations = Sets.newHashSet(oasLocations);
        this.typeMapper = new CMLPrimitiveTypeMapper();
        this.domainObjectMap = new HashMap<>();
    }

    @Override
    public Set<BoundedContext> discoverBoundedContexts() {
        var boundedContexts = Sets.<BoundedContext>newHashSet();
        for (String location : this.oasLocations) {
            var parseResult = new OpenAPIV3Parser().readLocation(location, null, new ParseOptions());
            if (!parseResult.getMessages().isEmpty())
                LOG.error("Parsing the OAS '" + location + "' resulted in validation errors: " + String.join(", ", parseResult.getMessages()));
            currentOAS = parseResult.getOpenAPI();
            if (currentOAS == null)
                throw new RuntimeException("Could not successfully parse OAS!");
            var context = discoverBoundedContext(currentOAS);
            if (context != null)
                boundedContexts.add(context);
        }
        return boundedContexts;
    }

    private BoundedContext discoverBoundedContext(OpenAPI oas) {
        var bc = new BoundedContext(oas.getInfo().getTitle());
        for (Map.Entry<String, PathItem> entry : oas.getPaths().entrySet()) {
            var aggregate = discoverAggregate(entry.getKey(), entry.getValue());
            if (aggregate != null)
                bc.addAggregate(aggregate);
        }
        return bc;
    }

    private Aggregate discoverAggregate(String pathItemKey, PathItem pathItem) {
        var aggregateName = pathItemKey.startsWith("/") ? pathItemKey.substring(1) : pathItemKey;
        aggregateName = aggregateName.replace("/", "_");
        var aggregate = new Aggregate(aggregateName);
        aggregate.setDiscoveryComment(pathItem.getSummary());

        if (endpointHasOperation(pathItem)) {
            var service = new Service(aggregateName + "Service");
            service.setDiscoveryComment("This service contains all operations of the following endpoint: " + pathItemKey);
            aggregate.addService(service);

            addOperationToService(service, discoverOperation(aggregate, pathItem.getGet()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getPut()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getPost()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getDelete()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getOptions()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getHead()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getPatch()));
            addOperationToService(service, discoverOperation(aggregate, pathItem.getTrace()));
        }

        return aggregate;
    }

    private void addOperationToService(Service service, Method operation) {
        if (operation != null)
            service.addOperation(operation);
    }

    private Method discoverOperation(Aggregate aggregate, Operation oasOperation) {
        if (oasOperation == null || oasOperation.getOperationId() == null || "".equals(oasOperation.getOperationId()))
            return null;

        var operation = new Method(oasOperation.getOperationId());

        // parameters
        if (oasOperation.getParameters() != null) {
            for (io.swagger.v3.oas.models.parameters.Parameter parameter : oasOperation.getParameters()) {
                Parameter param = discoverParameter(aggregate, parameter);
                if (param != null)
                    operation.addParameter(param);
            }
        }

        // request body
        if (oasOperation.getRequestBody() != null && oasOperation.getRequestBody().getContent() != null &&
                oasOperation.getRequestBody().getContent().containsKey(JSON_MEDIA_TYPE)) {
            operation.addParameter(new Parameter("input",
                    createType4JSONContent(oasOperation.getRequestBody().getContent(), aggregate,
                            formatTypeName(oasOperation.getOperationId() + "ParameterType"))));
        }

        // return type (currently an endpoint can only have one response, otherwise we do not discover)
        if (oasOperation.getResponses() != null && oasOperation.getResponses().size() == 1) {
            String responseKey = oasOperation.getResponses().keySet().iterator().next();
            ApiResponse response = oasOperation.getResponses().get(responseKey);
            // we currently only support json; TODO: implement more generic solution
            if (response.getContent() != null && response.getContent().containsKey(JSON_MEDIA_TYPE))
                operation.setReturnType(createType4JSONContent(response.getContent(), aggregate, oasOperation.getOperationId() + "ReturnType"));
        }

        return operation;
    }

    private Parameter discoverParameter(Aggregate aggregate, io.swagger.v3.oas.models.parameters.Parameter oasParameter) {
        var schema = oasParameter.getSchema();
        if (isRefSchema(schema))
            schema = resolveSchemaByRef(schema.get$ref());
        return new Parameter(oasParameter.getName(), createType4Schema(aggregate, schema, oasParameter.getName() + "Type"));
    }

    private Type createType4JSONContent(Content content, Aggregate aggregate, String inputTypeName) {
        Schema schema = content.get(JSON_MEDIA_TYPE).getSchema();
        var typeName = inputTypeName;
        if (isRefSchema(schema)) {
            typeName = getTypeNameFromSchemaRef(schema.get$ref());
            schema = resolveSchemaByRef(schema.get$ref());
        }
        return createType4Schema(aggregate, schema, typeName);
    }

    private Type createType4Schema(Aggregate aggregate, Schema inputSchema, String inputTypeName) {
        var schema = inputSchema;
        var typeName = inputTypeName;
        if (schema == null)
            return null;
        if (isRefSchema(schema)) {
            typeName = getTypeNameFromSchemaRef(schema.get$ref());
            schema = resolveSchemaByRef(schema.get$ref());
        }

        switch (schema.getType()) {
            case "object":
                DomainObject object = createEntity4Schema(aggregate, formatTypeName(typeName), schema);
                return new Type(object);
            case "array":
                ArraySchema arraySchema = (ArraySchema) schema;
                Type type = createType4Schema(aggregate, arraySchema.getItems(), typeName);
                type.setCollectionType("List");
                return type;
            default:
                return new Type(typeMapper.mapType(schema.getType()));
        }
    }

    private DomainObject createEntity4Schema(Aggregate aggregate, String objectName, Schema objectSchema) {
        if (!domainObjectMap.containsKey(aggregate))
            domainObjectMap.put(aggregate, new HashMap<>());

        // don't create a new object, if an entity with that name already exists
        if (domainObjectMap.get(aggregate).containsKey(objectName))
            return domainObjectMap.get(aggregate).get(objectName);

        // create entity
        var domainObject = new DomainObject(DomainObjectType.ENTITY, objectName);
        for (String propertyKey : (Set<String>) objectSchema.getProperties().keySet()) {
            var property = (Schema) objectSchema.getProperties().get(propertyKey);
            if (isRefSchema(property))
                property = resolveSchemaByRef(property.get$ref());
            Type type = null;
            switch (property.getType()) {
                case "object":
                    type = new Type(createEntity4Schema(aggregate, formatTypeName(propertyKey + "Type"), property));
                    break;
                default:
                    type = new Type(typeMapper.mapType(property.getType()));
            }
            domainObject.addAttribute(new Attribute(type, propertyKey));
        }

        aggregate.addDomainObject(domainObject);
        domainObjectMap.get(aggregate).put(objectName, domainObject);
        return domainObject;
    }

    private boolean endpointHasOperation(PathItem pathItem) {
        Set<Operation> operations = Sets.newHashSet();
        addOperation2ListIfNotNull(operations, pathItem.getGet());
        addOperation2ListIfNotNull(operations, pathItem.getPost());
        addOperation2ListIfNotNull(operations, pathItem.getPut());
        addOperation2ListIfNotNull(operations, pathItem.getDelete());
        addOperation2ListIfNotNull(operations, pathItem.getHead());
        addOperation2ListIfNotNull(operations, pathItem.getOptions());
        addOperation2ListIfNotNull(operations, pathItem.getPatch());
        addOperation2ListIfNotNull(operations, pathItem.getTrace());
        return !operations.isEmpty();
    }

    private void addOperation2ListIfNotNull(Set<Operation> operations, Operation operation) {
        if (operation != null)
            operations.add(operation);
    }

    private String formatTypeName(String typeName) {
        if (typeName == null || "".equals(typeName))
            return "UnknownType";
        return typeName.substring(0, 1).toUpperCase() + (typeName.length() > 1 ? typeName.substring(1) : "");
    }

    private boolean isRefSchema(Schema schema) {
        return schema.get$ref() != null && !"".equals(schema.get$ref());
    }

    private Schema resolveSchemaByRef(String ref) {
        if (!ref.startsWith(RefType.SCHEMAS.getInternalPrefix()))
            throw new RuntimeException("Can only handle references to schemas here.");
        if (currentOAS.getComponents() != null && currentOAS.getComponents().getSchemas() != null)
            return currentOAS.getComponents().getSchemas().get(getTypeNameFromSchemaRef(ref));
        return null;
    }

    private String getTypeNameFromSchemaRef(String ref) {
        if (!ref.startsWith(RefType.SCHEMAS.getInternalPrefix()))
            throw new RuntimeException("Given String is not a schema ref!");
        return ref.replace(RefType.SCHEMAS.getInternalPrefix(), "");
    }

}
