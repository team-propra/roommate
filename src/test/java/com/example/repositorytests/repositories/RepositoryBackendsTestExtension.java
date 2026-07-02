package com.example.repositorytests.repositories;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.List;
import java.util.stream.Stream;

public class RepositoryBackendsTestExtension implements TestTemplateInvocationContextProvider {
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .map(method -> method.isAnnotationPresent(RepositoryBackendsTest.class))
                .orElse(false);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(
                invocationContext(RepositoryBackend.EPHEMERAL),
                invocationContext(RepositoryBackend.POSTGRES)
        );
    }

    private TestTemplateInvocationContext invocationContext(RepositoryBackend backend) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return backend.name().toLowerCase();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return List.of(new RepositoryFixtureResolver(backend));
            }
        };
    }

    private static final class RepositoryFixtureResolver implements ParameterResolver, AfterTestExecutionCallback {
        private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(RepositoryFixtureResolver.class);

        private final RepositoryBackend backend;

        private RepositoryFixtureResolver(RepositoryBackend backend) {
            this.backend = backend;
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return parameterContext.getParameter().getType().equals(RepositoryFixture.class);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            RepositoryFixture fixture = backend == RepositoryBackend.EPHEMERAL
                    ? new EphemeralRepositoryFixture()
                    : new PostgresRepositoryFixture();
            extensionContext.getStore(NAMESPACE).put(fixtureKey(extensionContext), fixture);
            return fixture;
        }

        @Override
        public void afterTestExecution(ExtensionContext context) throws Exception {
            RepositoryFixture fixture = context.getStore(NAMESPACE).remove(fixtureKey(context), RepositoryFixture.class);
            if (fixture != null) {
                fixture.close();
            }
        }

        private String fixtureKey(ExtensionContext context) {
            return backend.name() + ":" + context.getUniqueId();
        }
    }
}
