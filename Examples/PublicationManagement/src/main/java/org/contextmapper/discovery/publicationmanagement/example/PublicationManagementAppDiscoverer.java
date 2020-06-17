package org.contextmapper.discovery.publicationmanagement.example;

import org.contextmapper.discovery.ContextMapDiscoverer;
import org.contextmapper.discovery.ContextMapSerializer;
import org.contextmapper.discovery.model.ContextMap;
import org.contextmapper.discovery.strategies.boundedcontexts.SpringBootBoundedContextDiscoveryStrategy;

import java.io.File;
import java.io.IOException;

/**
 * Discovers the subproject publication-management-application which is generated with JHipster according to the following
 * blogpost: https://ozimmer.ch/practices/2020/06/10/ICWEKeynoteAndDemo.html
 */
public class PublicationManagementAppDiscoverer {

    public static void main(String[] args) throws IOException {
        ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
                .usingBoundedContextDiscoveryStrategies(
                        new SpringBootBoundedContextDiscoveryStrategy("org.contextmapper.generated"));

        ContextMap contextmap = discoverer.discoverContextMap();
        new ContextMapSerializer().serializeContextMap(contextmap, new File("./src-gen/publicationmanagement.cml"));
    }

}
