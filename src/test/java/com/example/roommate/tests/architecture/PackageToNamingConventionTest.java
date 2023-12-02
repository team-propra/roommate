package com.example.roommate.tests.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
public class PackageToNamingConventionTest {
    @ArchTest
    static ArchRule services = classes()
            .that()
            .resideInAPackage("com.example.roommate.services..")
            .should()
            .haveSimpleNameEndingWith("Service");

    @ArchTest
    static ArchRule domainServices = classes()
            .that()
            .resideInAPackage("com.example.roommate.domain.services..")
            .should()
            .haveSimpleNameEndingWith("DomainService");

    @ArchTest
    static ArchRule tests = classes()
            .that()
            .resideInAPackage("com.example.roommate.tests..")
            .and()
            .resideOutsideOfPackage("..utility..")
            .and()
            .resideOutsideOfPackage("..factories..")
            .should()
            .haveSimpleNameEndingWith("Test");


    @ArchTest
    static ArchRule factories = classes()
            .that()
            .resideInAPackage("com.example.roommate.tests.factories..")
            .should()
            .haveSimpleNameEndingWith("Factory");

    @ArchTest
    static ArchRule interfaces = classes()
            .that()
            .areInterfaces()
            .should()
            .haveSimpleNameStartingWith("I");
}
