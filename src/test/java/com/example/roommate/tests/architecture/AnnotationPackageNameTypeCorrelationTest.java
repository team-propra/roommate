package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.annotations.DomainService;
import com.example.roommate.annotations.Factory;
import com.example.roommate.annotations.TestClass;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

import static com.example.roommate.utility.archUnit.Correlations.classCorrelations;



@TestClass
public class AnnotationPackageNameTypeCorrelationTest {
    @TestFactory
    public Stream<DynamicTest> domainServices(){
        return classCorrelations("..roommate.domain.services.." ,DomainService.class, ArchConditions.haveSimpleNameEndingWith("DomainService"));
    }

    @TestFactory
    public Stream<DynamicTest> applicationServices(){
        return classCorrelations("..roommate.services..", ApplicationService.class, ArchConditions.haveSimpleNameEndingWith("ApplicationService"));
    }

    @TestFactory
    public Stream<DynamicTest> repositories(){
        return classCorrelations("..roommate.persistence.repositories..", Repository.class, ArchConditions.haveSimpleNameEndingWith("Repository"));
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
