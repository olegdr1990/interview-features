package root;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

class Postgres {

    private static final PostgreSQLContainer<?> docker = new PostgreSQLContainer<>("postgres:15.1")
            .withDatabaseName("test-features")
            .withUsername("test-features")
            .withPassword("test-features");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext ctx) {
            docker.start();

            Map<String, String> extra = Map.of(
                "spring.datasource.url", docker.getJdbcUrl(),
                "spring.datasource.username", docker.getUsername(),
                "spring.datasource.password", docker.getPassword()
            );
            TestPropertyValues.of(extra).applyTo(ctx.getEnvironment());
        }
    }
}
