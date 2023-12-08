package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.*;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

import static com.example.roommate.utility.archUnit.Correlations.classCorrelations;
import static com.example.roommate.utility.archUnit.Correlations.interfaceCorrelations;


@TestClass
public class AnnotationPackageNameTypeCorrelationTest {
    @TestFactory
    public Stream<DynamicTest> domainServices(){
        return classCorrelations("..roommate.domain.services.." ,DomainService.class, ArchConditions.haveSimpleNameEndingWith("DomainService"));
    }

    @TestFactory
    public Stream<DynamicTest> applicationServices(){
        return classCorrelations("..roommate.application.services..", ApplicationService.class, ArchConditions.haveSimpleNameEndingWith("ApplicationService"));
    }

    @TestFactory
    public Stream<DynamicTest> applicationDats(){
        return classCorrelations("..roommate.application.data..", ApplicationData.class, ArchConditions.haveSimpleNameEndingWith("ApplicationData"));
    }

    @TestFactory
    public Stream<DynamicTest> repositories(){
        return classCorrelations("..roommate.persistence.repositories..", Repository.class, ArchConditions.haveSimpleNameEndingWith("Repository"));
    }

    @TestFactory
    public Stream<DynamicTest> repositoryInterfaces(){
        return interfaceCorrelations("..roommate.interfaces.repositories..", RepositoryInterface.class, ArchConditions.and(ArchConditions.haveSimpleNameEndingWith("Repository"),ArchConditions.haveSimpleNameStartingWith("I")));
    }

    @TestFactory
    public Stream<DynamicTest> allInterfaces(){
        return interfaceCorrelations("..roommate.interfaces..", Interface.class, ArchConditions.haveSimpleNameStartingWith("I"));
    }

    @TestFactory
    public Stream<DynamicTest> tests(){
        return classCorrelations("..roommate.tests..", TestClass.class, ArchConditions.haveSimpleNameEndingWith("Test"));
    }

    @TestFactory
    public Stream<DynamicTest> factories(){
        return classCorrelations("..roommate.factories..", Factory.class, ArchConditions.haveSimpleNameEndingWith("Factory"));
    }



}
