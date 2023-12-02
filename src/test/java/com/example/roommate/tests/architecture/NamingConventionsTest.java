package com.example.roommate.tests.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
public class NamingConventionsTest {
    @ArchTest
    static ArchRule services = classes()
            .that()
            .resideInAPackage("com.example.roommate.services..")
            .and()
            .areAnnotatedWith(Service.class)
            .should()
            .haveSimpleNameEndingWith("Service");

    @ArchTest
    static ArchRule domainServices = classes()
            .that()
            .resideInAPackage("com.example.roommate.domain.services..")
            .and()
            .areAnnotatedWith(Service.class)
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
    static ArchRule interfaces = classes()
            .that()
            .areInterfaces()
            .should()
            .haveSimpleNameStartingWith("I");
}
