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
    
    static GivenClassesConjunction actuallyClasses = classes()
            .that()
            .areNotInterfaces();

    static GivenClassesConjunction actuallyInterfaces = classes()
            .that()
            .areInterfaces()
            .and()
            .areNotAnnotations();

    static ClassesThat<GivenClassesConjunction> actuallyAnnotations = classes()
            .that()
            .areAnnotations()
            .and();
    public static Stream<DynamicTest> classCorrelations(String packageIdentifier, Class<? extends Annotation> annotation, ArchCondition<JavaClass> nameRequirement){
        return correlations(packageIdentifier, annotation, nameRequirement, actuallyClasses);
    }

    public static Stream<DynamicTest> interfaceCorrelations(String packageIdentifier, Class<? extends Annotation> annotation, ArchCondition<JavaClass> nameRequirement) {
        return correlations(packageIdentifier, annotation, nameRequirement, actuallyInterfaces);
    }

    static Stream<DynamicTest> correlations(String packageIdentifier, Class<? extends Annotation> annotation, ArchCondition<JavaClass> nameRequirement, GivenClassesConjunction base) {
        JavaClasses classes =
                new ClassFileImporter().importPackages("com.example.roommate");
        String annotationName = annotation.getSimpleName();

        ArchRule annotationPackage = base
                .and()
                .areMetaAnnotatedWith(annotation)
                .should()
                .resideInAPackage(packageIdentifier);

        ArchRule annotationNaming = base
                .and()
                .areMetaAnnotatedWith(annotation)
                .should(nameRequirement);

        ArchRule packageAnnotation = base
                .and()
                .resideInAPackage(packageIdentifier)
                .should()
                .beMetaAnnotatedWith(annotation);

        ArchRule packageNaming = base
                .and()
                .resideInAPackage(packageIdentifier)
                .should(nameRequirement);


        ArchRule namingAnnotation = base
                .and(ArchConditionToDescribedPredicateAdapter.create(nameRequirement))
                .should()
                .beMetaAnnotatedWith(annotation);

        ArchRule namingPackage = base
                .and(ArchConditionToDescribedPredicateAdapter.create(nameRequirement))
                .should()
                .resideInAPackage(packageIdentifier);

        return Stream.of(
                DynamicTest.dynamicTest("annotation: <%s> correlates to package: <%s>".formatted(annotationName, packageIdentifier), () -> annotationPackage.check(classes)),
                DynamicTest.dynamicTest("annotation: <%s> correlates to naming: <%s>".formatted(annotationName, nameRequirement), () -> annotationNaming.check(classes)),
                DynamicTest.dynamicTest("package: <%s> correlates to annotation: <%s>".formatted(packageIdentifier, annotationName), () -> packageAnnotation.check(classes)),
                DynamicTest.dynamicTest("package: <%s> correlates to naming: <%s>".formatted(packageIdentifier, nameRequirement), () -> packageNaming.check(classes)),
                DynamicTest.dynamicTest("naming: <%s> correlates to annotation: <%s>".formatted(nameRequirement, annotationName), () -> namingAnnotation.check(classes)),
                DynamicTest.dynamicTest("naming: <%s> correlates to annotation: <%s>".formatted(nameRequirement, packageIdentifier), () -> namingPackage.check(classes))

        );
    }
}
