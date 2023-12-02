package com.example.roommate.tests.architecture;

import com.example.roommate.annotations.ApplicationService;
import com.example.roommate.annotations.DomainService;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import org.apache.catalina.startup.ClassLoaderFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

import static com.example.roommate.tests.architecture.utility.Correlations.classCorrelations;



public class AnnotationPackageNameTypeCorrelationTest {
    @TestFactory
    public Stream<DynamicTest> domainServices(){
        return classCorrelations("..roommate.domain.services.." ,DomainService.class, ArchConditions.haveSimpleNameEndingWith("DomainService"));
    }

    @TestFactory
    public Stream<DynamicTest> applicationServices(){
        return classCorrelations("..roommate.services..", ApplicationService.class, ArchConditions.haveSimpleNameEndingWith("Service"));
    }

    @TestFactory
    public Stream<DynamicTest> repositories(){
        return classCorrelations("..roommate.persistence..", Repository.class, ArchConditions.haveSimpleNameEndingWith("Repository"));
    }



}
