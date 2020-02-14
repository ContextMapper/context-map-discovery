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

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.reflections.util.Utils.findLogger;

/**
 * Provides methods for annotation scanning.
 *
 * @author Stefan Kapferer
 */
public class AnnotationScanner {

    public static Logger log = findLogger(AnnotationScanner.class);

    /**
     * Finds all types within a package annotated with a given annotation.
     *
     * @param packageName the package within which to search for the types
     * @param annotation  the annotation with which the types must be annotated
     * @return the set of types within the given package which are annotated with the given annotation
     */
    public Set<Class<?>> scanForAnnotatedType(String packageName, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false), new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(annotation);
    }

    /**
     * Finds all methods of a type which are annotated with a given annotation.
     *
     * @param type        the type within which you want to search for methods
     * @param annotations the annotations with which the methods must be annotated (at least one of the given annotations)
     * @return the set of methods within the given type which are annotated with the given annotation
     */
    public Set<Method> scanForAnnotatedMethods(Class<?> type, Class<? extends Annotation>... annotations) {
        Set<Method> methods = new HashSet<>();
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forClass(type))
                    .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner())
                    .filterInputsBy(new FilterBuilder().includePackage(type))
            );
            for (Class<? extends Annotation> annotation : annotations) {
                methods.addAll(reflections.getMethodsAnnotatedWith(annotation));
            }
        } catch (ReflectionsException e) {
            // unfortunately the reflections library in version 0.9.12 (needed for Java 11) throws an exception if nothing is found
            // just log errors here for now; should no longer be needed to catch this when the following issue is fixed:
            // https://github.com/ronmamo/reflections/issues/273
            log.error(e.getMessage());
        }
        return methods;
    }

}
