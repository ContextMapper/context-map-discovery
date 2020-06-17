package org.contextmapper.generated.referencemanagementcontext;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.contextmapper.generated.referencemanagementcontext");

        noClasses()
            .that()
                .resideInAnyPackage("org.contextmapper.generated.referencemanagementcontext.service..")
            .or()
                .resideInAnyPackage("org.contextmapper.generated.referencemanagementcontext.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..org.contextmapper.generated.referencemanagementcontext.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
