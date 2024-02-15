package com.example.roommate.tests.architecture;

// These are not properly covered yet and just here as a reminder

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class UncategorizedRulesTest {
    @ArchTest
    static ArchRule normalServicesShouldNotExist = classes()
            .should()
            .notBeAnnotatedWith("Service");
    
    @ArchTest
    static ArchRule classesInInterfacesShouldBeInterfaces = classes()
            .that()
            .resideInAPackage("..interfaces..")
            .should()
            .beInterfaces();


    @ArchTest
    static ArchRule applicationDataMustImplementInterfacesAndBeRecord = classes()
            .that()
            .resideInAPackage("..application.data..")
            .should()
            .beRecords();
}
