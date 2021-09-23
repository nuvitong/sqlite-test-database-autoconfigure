package org.nuvito.spring.test.autoconfigure.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests, if by setting {@link AutoConfigureTestDatabase.Replace#NONE} no test database replacement
 * takes place and the configured data source is used
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DisabledEmbeddedSqliteDatabaseIntegrationTest {
    @Configuration
    static class TestConfiguration {}

    @Autowired
    private DataSource dataSource;

    @Test
    void givenThatNoTestDatabaseShouldReplace_configuredDataSourceShouldBeCreated() {
        assertThat(dataSource).isNotNull().isInstanceOf(SQLiteDataSource.class);
        assertThat(((SQLiteDataSource) dataSource).getUrl()).isEqualTo("jdbc:sqlite:target/foo.db");
    }
}
