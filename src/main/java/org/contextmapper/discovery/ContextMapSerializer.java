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
package org.contextmapper.discovery;

import org.apache.commons.io.FilenameUtils;
import org.contextmapper.discovery.cml.ContextMapToCMLConverter;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.dsl.ContextMappingDSLStandaloneSetup;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.SaveOptions;

import java.io.File;
import java.io.IOException;

/**
 * Serializes discovered {@link org.contextmapper.discovery.model.ContextMap} to Context Mapper DSL (CML) code.
 *
 * @author Stefan Kapferer
 */
public class ContextMapSerializer {

    public void serializeContextMap(ContextMap contextMap, File cmlFile) throws IOException {
        if (!FilenameUtils.getExtension(cmlFile.toString()).equals("cml"))
            throw new IllegalArgumentException("The CML file must end with the file extension '*.cml'!");
        if (contextMap.getBoundedContexts().size() <= 0)
            throw new IllegalArgumentException("The Context Map must at least contain one Bounded Context to be serialized!");

        ContextMappingDSLStandaloneSetup.doSetup();
        Resource resource = new ResourceSetImpl().createResource(URI.createURI(cmlFile.toURI().toString()));
        ContextMappingModel model = new ContextMapToCMLConverter().convert(contextMap);
        resource.getContents().add(model);
        resource.save(SaveOptions.defaultOptions().toOptionsMap());
    }

}
