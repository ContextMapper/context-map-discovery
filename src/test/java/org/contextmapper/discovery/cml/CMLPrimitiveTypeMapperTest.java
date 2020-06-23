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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CMLPrimitiveTypeMapperTest {

    @Test
    public void canMapPrimitiveType() {
        // given
        CMLPrimitiveTypeMapper mapper = new CMLPrimitiveTypeMapper();

        // when
        String type1 = mapper.mapType("string");
        String type2 = mapper.mapType("String");

        // then
        assertEquals("String", type1);
        assertEquals("String", type2);
    }

    @Test
    public void canReturnInputIfNotMappable() {
        // given
        CMLPrimitiveTypeMapper mapper = new CMLPrimitiveTypeMapper();

        // when
        String type = mapper.mapType("MySuperImaginativeType");

        // then
        assertEquals("MySuperImaginativeType", type);
    }

}
