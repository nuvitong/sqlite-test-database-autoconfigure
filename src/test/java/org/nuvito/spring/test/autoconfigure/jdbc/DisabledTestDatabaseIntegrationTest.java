package org.nuvito.spring.test.autoconfigure.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests, if the test database won't be initialized, if no {@code @AutoConfigureTestDatabase} is annotated
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class DisabledTestDatabaseIntegrationTest {
    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {}

    @Autowired
    private DataSource dataSource;

    @Test
    void givenTestWithoutTestDatabase_configuredDatabaseShouldBeInitialized() {
        assertThat(dataSource)
                .isNotNull()
                .isInstanceOf(SQLiteDataSource.class);
        assertThat(((SQLiteDataSource) dataSource).getUrl()).isEqualTo("jdbc:sqlite:target/foo.db");
    }
}
