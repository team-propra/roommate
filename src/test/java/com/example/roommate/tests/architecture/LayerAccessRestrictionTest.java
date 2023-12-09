package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.example.roommate.architectureDeclaration.Layers.*;


@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class LayerAccessRestrictionTest {
    @ArchTest
    static ArchRule domainObjectsCanOnlyBeUsedInsideDomainServices = all
            .whereLayer(DOMAIN_ENTITIES).mayOnlyBeAccessedByLayers(DOMAIN_ENTITIES,DOMAIN_SERVICES,TESTS, FACTORIES);

    
}
