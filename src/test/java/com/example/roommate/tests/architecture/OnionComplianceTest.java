package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class OnionComplianceTest {
//    @ArchTest
//    static ArchRule onion = onionArchitecture()
//                .domainModels("domain")
////                .domainServices("example.domainservices")
//                .applicationServices("services")
//                .adapter("http", "controller")
//                .adapter("persistence", "repositories");

    @ArchTest
    static ArchRule onion =
            layeredArchitecture().consideringAllDependencies()

                    .layer("Controllers").definedBy("com.example.roommate.controller..")
                    .layer("Services").definedBy("com.example.roommate.services..")
                    .layer("Persistence").definedBy("com.example.roommate.persistence..")
                    .layer("Domain").definedBy("com.example.roommate.tests.domain..")
                    .layer("Tests").definedBy("com.example.roommate.tests..")
                    .layer("DTOs").definedBy("com.example.roommate.dtos..")
                    .layer("Factory").definedBy("com.example.roommate.factories..")


                    .whereLayer("Controllers").mayOnlyBeAccessedByLayers("Tests")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Tests","Services","Factory")
                    .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers","Tests","Factory")
                    .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services","Tests","Factory")
                    .whereLayer("DTOs").mayOnlyBeAccessedByLayers("Controllers","Tests","Services","Factory");

}
