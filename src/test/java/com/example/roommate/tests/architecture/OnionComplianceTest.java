package com.example.roommate.tests.architecture;

import com.sun.tools.javac.Main;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;


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
                    .layer("Domain").definedBy("com.example.roommate.domain..")
                    .layer("Tests").definedBy("com.example.roommate.tests..")


                    .whereLayer("Controllers").mayOnlyBeAccessedByLayers("Tests")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Tests","Services")
                    .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers","Tests")
                    .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services","Tests");

}
