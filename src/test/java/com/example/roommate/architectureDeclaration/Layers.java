package com.example.roommate.architectureDeclaration;

import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class Layers {
    public static final String CONTROLLERS = "Controllers";
    public static final String APPLICATION_SERVICES = "ApplicationServices";
    public static final String APPLICATION_DATA = "ApplicationData";
    public static final String PERSISTENCE = "Persistence";
    public static final String DOMAIN = "Domain";
    public static final String DOMAIN_MODELS = "DomainModels";
    public static final String DOMAIN_ENTITIES = "DomainEntities";
    public static final String DOMAIN_VALUES = "DomainValues";
    public static final String DOMAIN_SERVICES = "DomainServices";
    public static final String TESTS = "Tests";
    public static final String FORMS = "Forms";
    public static final String FACTORIES = "Factory";
    public static final String EXAMPLES = "Examples";
    
    public static final Architectures.LayeredArchitecture all = layeredArchitecture().consideringAllDependencies()
                    .layer(CONTROLLERS).definedBy("com.example.roommate.controller..")
                    .layer(APPLICATION_SERVICES).definedBy("com.example.roommate.application.services..")
                    .layer(APPLICATION_DATA).definedBy("com.example.roommate.application.data..")
                    .layer(PERSISTENCE).definedBy("com.example.roommate.persistence..")
                    .layer(DOMAIN).definedBy("com.example.roommate.domain..")
                    .layer(DOMAIN_MODELS).definedBy("com.example.roommate.domain.models..")
                    .layer(DOMAIN_ENTITIES).definedBy("com.example.roommate.domain.models.entities..")
                    .layer(DOMAIN_VALUES).definedBy("com.example.roommate.values.domainValues..")
                    .layer(DOMAIN_SERVICES).definedBy("com.example.roommate.domain.services..")
                    .layer(TESTS).definedBy("com.example.roommate.tests..")
                    .layer(FORMS).definedBy("com.example.roommate.values.forms..")
                    .layer(FACTORIES).definedBy("com.example.roommate.factories..")
                    .layer(EXAMPLES).definedBy("com.example.roommate.examples..");
}
