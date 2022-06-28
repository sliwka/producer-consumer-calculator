package org.example;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

public class ArchTest {
    @Test
    void testPackagesDependencies() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.example");

        noClasses()
                .that()
                .resideInAnyPackage("org.example.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.example.tasks..")
                .because("Domain should not depend on tasks")
                .check(importedClasses);

        noClasses()
                .that()
                .resideInAnyPackage("org.example.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.example.infrastructure..")
                .because("Domain should not depend on infrastructure")
                .check(importedClasses);

        noClasses()
                .that()
                .resideInAnyPackage("org.example.tasks..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.example.infrastructure..")
                .because("Tasks should not depend on infrastructure")
                .check(importedClasses);

    }
}
