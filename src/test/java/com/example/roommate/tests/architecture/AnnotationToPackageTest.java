package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.annotations.DomainService;
import com.example.roommate.annotations.RepositoryInterface;
import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.roommate")
@TestClass
public class AnnotationToPackageTest {
    @ArchTest
    static ArchRule domainServices = classes()
            .that()
            .areAnnotatedWith(DomainService.class)
            .should()
            .resideInAPackage("..roommate.domain.services");

    @ArchTest
    static ArchRule applicationServices = classes()
            .that()
            .areAnnotatedWith(ApplicationService.class)
            .should()
            .resideInAPackage("..roommate.services");

    @ArchTest
    static ArchRule repositoryImplementation = classes()
            .that()
            .areAnnotatedWith(Repository.class)
            .should()
            .resideInAPackage("..roommate.persistence..");

    @ArchTest
    static ArchRule repositoryInterface = classes()
            .that()
            .areAnnotatedWith(RepositoryInterface.class)
            .should()
            .resideInAPackage("..roommate.interfaces.repositories..");
}
