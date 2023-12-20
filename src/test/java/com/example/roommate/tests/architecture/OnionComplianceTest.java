package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.example.roommate.architectureDeclaration.Layers.*;


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
    static ArchRule onion = all
                    .whereLayer(CONTROLLERS).mayOnlyBeAccessedByLayers(TESTS)
                    .whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(TESTS, APPLICATION_SERVICES, FACTORIES)
                    .whereLayer(APPLICATION_SERVICES).mayOnlyBeAccessedByLayers(CONTROLLERS,TESTS, FACTORIES)
                    .whereLayer(PERSISTENCE).mayOnlyBeAccessedByLayers(APPLICATION_SERVICES,TESTS, FACTORIES)
                    .whereLayer(FORMS).mayOnlyBeAccessedByLayers(CONTROLLERS,TESTS, APPLICATION_SERVICES, FACTORIES, DOMAIN_VALUES);

}
