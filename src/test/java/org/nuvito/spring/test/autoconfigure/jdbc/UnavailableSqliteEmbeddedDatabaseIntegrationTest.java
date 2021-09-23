package org.nuvito.spring.test.autoconfigure.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.AbstractDriverBasedDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests, if the default (embedded) test database mechanism comes into play, if the
 * sqlite test database is not available (not on classpath)
 */
@DataJdbcTest(excludeAutoConfiguration = SqliteTestDatabaseAutoConfiguration.class)
class UnavailableSqliteEmbeddedDatabaseIntegrationTest {
    @Configuration
    static class TestConfiguration {}

    @Autowired
    private DataSource dataSource;

    @Test
    void givenNoSqliteAutoConfiguration_hsqlEmbeddedDataSourceShouldBeLoaded() {
        assertThat(dataSource).isNotNull().isInstanceOf(EmbeddedDatabase.class);

        AbstractDriverBasedDataSource proxiedDataSource = extractProxiedDataSource(dataSource);
        assertThat(proxiedDataSource.getUrl()).startsWith("jdbc:hsqldb:mem");
    }

    private AbstractDriverBasedDataSource extractProxiedDataSource(DataSource proxyDataSource) {
        try {
            return (AbstractDriverBasedDataSource) ReflectionTestUtils.getField(proxyDataSource, "dataSource");
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    String.format("Given proxyDataSource seems not to be a proxy, " +
                            "as there is no field 'dataSource'. Type is %s", proxyDataSource.getClass().getName()), e);
        }
    }
}
