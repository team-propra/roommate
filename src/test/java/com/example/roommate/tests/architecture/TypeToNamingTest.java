package com.example.roommate.tests.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
public class TypeToNamingTest {
    @ArchTest
    static ArchRule interfaces = classes()
            .that()
            .areInterfaces()
            .should()
            .haveSimpleNameStartingWith("I");
}
