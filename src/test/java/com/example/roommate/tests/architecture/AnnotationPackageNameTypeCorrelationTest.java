package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.annotations.DomainService;
import com.sun.tools.javac.Main;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.lang.annotation.Annotation;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;



public class AnnotationPackageNameTypeCorrelationTest {
    @TestFactory
    public Stream<DynamicTest> domainServices(){
        return correlation("..roommate.domain.services.." ,DomainService.class, ClassesThat::areNotInterfaces, given->given.haveSimpleNameEndingWith("DomainService"));
    }

    @TestFactory
    public Stream<DynamicTest> applicationServices(){
        return correlation("..roommate.services..", ApplicationService.class, ClassesThat::areNotInterfaces, given->given.haveSimpleNameEndingWith("Service"));
    }
    
    static Stream<DynamicTest> correlation(String packageIdentifier, Class<? extends Annotation> annotation, Function<ClassesThat<GivenClassesConjunction>,GivenClassesConjunction> typeRequirement, Function<ClassesShould,ClassesShouldConjunction> nameRequirement){
        JavaClasses classes =
                new ClassFileImporter().importPackages("com.example.roommate");
        String annotationName = annotation.getSimpleName();
        
        ArchRule annotationPackage = classes()
            .that()
            .areAnnotatedWith(annotation)
            .should()
            .resideInAPackage(packageIdentifier);
        
        ArchRule packageAnnotation = classes()
                .that()
                .resideInAPackage(packageIdentifier)         
                .should()
                .beAnnotatedWith(annotation);
        
        return Stream.of(
                DynamicTest.dynamicTest(annotationName+" correlates to package: "+packageIdentifier, ()->annotationPackage.check(classes)),
                DynamicTest.dynamicTest("package "+packageIdentifier+" correlates to annotation: "+annotationName, ()->packageAnnotation.check(classes))
        );
    }
    
}
