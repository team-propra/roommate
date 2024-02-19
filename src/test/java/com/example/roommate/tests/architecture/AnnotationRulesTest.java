package com.example.roommate.tests.architecture;

// These are not properly covered yet and just here as a reminder

import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class AnnotationRulesTest {
    @ArchTest
    static ArchRule classesWithDirectServiceAnnotationShouldNotExist = classes()
            .that()
            .areNotAnnotations()
            .should()
            .notBeAnnotatedWith(Service.class);

    @ArchTest
    static ArchRule classesWithDirectComponentsShouldNotExist = classes()
            .that()
            .areNotAnnotations()
            .should()
            .notBeAnnotatedWith(Component.class);
    
}
