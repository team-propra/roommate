package com.example.roommate.tests.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


@AnalyzeClasses(packages = "com.example.roommate")
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


                    .whereLayer("Controllers").mayOnlyBeAccessedByLayers("Tests")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Tests","Services")
                    .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers","Tests")
                    .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services","Tests")
                    .whereLayer("DTOs").mayOnlyBeAccessedByLayers("Controllers","Tests","Services");

}