package com.example.roommate.architecture;

import com.sun.tools.javac.Main;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;


@AnalyzeClasses(packages = "com.example.roommate")
public class OnionComplianceTest {
    @ArchTest
    static ArchRule onion = onionArchitecture()
                .domainModels("domain")
//                .domainServices("example.domainservices")
                .applicationServices("services")
                .adapter("http", "controller")
                .adapter("persistence", "repositories");
}
