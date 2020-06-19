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
package org.contextmapper.discovery.cml;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps types to CML primitive types, if possible.
 *
 * @author Stefan Kapferer
 */
public class CMLPrimitiveTypeMapper {

    private static final String[] PRIMITIVE_CML_TYPES = {"String", "int", "Integer", "Long", "Boolean", "Date",
            "DateTime", "Timestamp", "BigDecimal", "BigInteger", "Double", "Float", "Key", "PagingParameter",
            "PagedResult", "Blob", "Clob", "Object[]"};

    private Map<String, String> typeMap;

    public CMLPrimitiveTypeMapper() {
        this.typeMap = new HashMap<>();
        for (String primitiveType : PRIMITIVE_CML_TYPES) {
            typeMap.put(primitiveType.toLowerCase(), primitiveType);
        }
    }

    /**
     * Maps the input type to a primitive type supported by the CML language; if possible.
     *
     * @param type the type that shall be mapped
     * @return the mapped type; or the input type in case it could not be mapped
     */
    public String mapType(String type) {
        if (typeMap.containsKey(type.toLowerCase()))
            return typeMap.get(type.toLowerCase());
        return type;
    }

}
