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
package org.contextmapper.discovery.strategies.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Some helper methods for scanning entities etc.
 *
 * @author Stefan Kapferer
 */
public class ReflectionHelpers {

    /**
     * Gets the actual types of a parameterized type.
     *
     * @param parameterizedType the parameterized type
     * @return the actual types
     */
    public Set<Class<?>> getActualTypesOfParameterizedType(ParameterizedType parameterizedType) {
        Set<Class<?>> actualTypes = new HashSet<>();
        for (Type actualType : parameterizedType.getActualTypeArguments()) {
            if (!(actualType instanceof Class<?>))
                continue;
            actualTypes.add((Class<?>) actualType);
        }
        return actualTypes;
    }

    /**
     * Gets all fields of a type.
     *
     * @param type the type for which the fields shall be returned
     * @return the list of fields of the given type
     */
    public List<Field> getAllFieldsOfType(Class<?> type) {
        if (type == null) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFieldsOfType(type.getSuperclass()));
        result.addAll(Arrays.asList(type.getDeclaredFields()).stream().filter(f -> !f.getName().startsWith("$")).collect(Collectors.toSet()));
        return result;
    }

    /**
     * Checks whether a type is a collection, set, or list.
     *
     * @param type the type to be checked for collection types
     * @return true, if the given type is a set, list, or collection. false otherwise.
     */
    public boolean isCollectionType(Class<?> type) {
        if (type.equals(List.class))
            return true;
        else if (type.equals(Set.class))
            return true;
        else if (type.equals(Collection.class))
            return true;
        return false;
    }

}
