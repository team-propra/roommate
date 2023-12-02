package com.example.roommate.utility.archUnit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.ClassesThat;
import com.tngtech.archunit.lang.syntax.elements.GivenClassesConjunction;
import org.junit.jupiter.api.DynamicTest;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


public class Correlations {
    
    static ClassesThat<GivenClassesConjunction> actuallyClasses = classes()
            .that()
            .areNotInterfaces()
            .and();

    static ClassesThat<GivenClassesConjunction> actuallyInterfaces = classes()
            .that()
            .areInterfaces()
            .and()
            .areNotAnnotations()
            .and();

    static ClassesThat<GivenClassesConjunction> actuallyAnnotations = classes()
            .that()
            .areAnnotations()
            .and();
    public static Stream<DynamicTest> classCorrelations(String packageIdentifier, Class<? extends Annotation> annotation, ArchCondition<JavaClass> nameRequirement){
        JavaClasses classes =
                new ClassFileImporter().importPackages("com.example.roommate");
        String annotationName = annotation.getSimpleName();

        ArchRule annotationPackage = actuallyClasses
                .areAnnotatedWith(annotation)
                .should()
                .resideInAPackage(packageIdentifier);

        ArchRule annotationNaming = actuallyClasses
                .areAnnotatedWith(annotation)
                .should(nameRequirement);

        ArchRule packageAnnotation = actuallyClasses
                .resideInAPackage(packageIdentifier)
                .should()
                .beAnnotatedWith(annotation);

        ArchRule packageNaming = actuallyClasses
                .resideInAPackage(packageIdentifier)
                .should(nameRequirement);


        ArchRule namingAnnotation = actuallyClasses
                .areNotInterfaces()
                .and(ArchConditionToDescribedPredicateAdapter.create(nameRequirement))
                .should()
                .beAnnotatedWith(annotation);

        ArchRule namingPackage = actuallyClasses
                .areNotInterfaces()
                .and(ArchConditionToDescribedPredicateAdapter.create(nameRequirement))
                .should()
                .resideInAPackage(packageIdentifier);

        return Stream.of(
                DynamicTest.dynamicTest("annotation: <%s> correlates to package: <%s>".formatted(annotationName,packageIdentifier), ()->annotationPackage.check(classes)),
                DynamicTest.dynamicTest("annotation: <%s> correlates to naming: <%s>".formatted(annotationName,nameRequirement), ()->annotationNaming.check(classes)),
                DynamicTest.dynamicTest("package: <%s> correlates to annotation: <%s>".formatted(packageIdentifier,annotationName), ()->packageAnnotation.check(classes)),
                DynamicTest.dynamicTest("package: <%s> correlates to naming: <%s>".formatted(packageIdentifier,nameRequirement), ()->packageNaming.check(classes)),
                DynamicTest.dynamicTest("naming: <%s> correlates to annotation: <%s>".formatted(nameRequirement,annotationName), ()->namingAnnotation.check(classes)),
                DynamicTest.dynamicTest("naming: <%s> correlates to annotation: <%s>".formatted(nameRequirement,packageIdentifier), ()->namingPackage.check(classes))
                
        );
    }
}
