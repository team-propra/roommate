package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.*;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static com.example.roommate.utility.archUnit.Correlations.classCorrelations;


@TestClass
public class CorrelationTest {
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
    public Stream<DynamicTest> tests(){
        return classCorrelations("..roommate.tests..", TestClass.class, ArchConditions.haveSimpleNameEndingWith("Test"));
    }

    @TestFactory
    public Stream<DynamicTest> factories(){
        return classCorrelations("..roommate.factories..", Factory.class, ArchConditions.haveSimpleNameEndingWith("Factory"));
    }



}
