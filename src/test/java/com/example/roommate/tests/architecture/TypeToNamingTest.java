package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class TypeToNamingTest {
    @ArchTest
    static ArchRule interfaces = classes()
            .that()
            .areInterfaces()
            .and()
            .areNotAnnotations()
            .should()
            .haveSimpleNameStartingWith("I");
}
