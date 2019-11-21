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
package org.contextmapper.discovery.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTest {

    @Test
    public void canCreateMethod() {
        // when
        Method method = new Method("testMethod");

        // then
        assertEquals("testMethod", method.getName());
    }

    @Test
    public void canSetReturnType() {
        // given
        Method method = new Method("testMethod");

        // then
        DomainObject returnType = new DomainObject(DomainObjectType.VALUE_OBJECT, "ReturnType");
        method.setReturnType(returnType);

        // then
        assertEquals("ReturnType", method.getReturnType().getName());
    }

    @Test
    public void canAddParameter() {
        // given
        Method method = new Method("TestMethod");

        // when
        DomainObject parameterType = new DomainObject(DomainObjectType.VALUE_OBJECT, "ParameterType");
        Parameter parameter = new Parameter("testParam", parameterType);
        method.addParameter(parameter);

        // then
        Parameter resultParam = method.getParameters().iterator().next();
        assertEquals("testParam", resultParam.getName());
        assertEquals("ParameterType", resultParam.getType().getName());
    }

}
